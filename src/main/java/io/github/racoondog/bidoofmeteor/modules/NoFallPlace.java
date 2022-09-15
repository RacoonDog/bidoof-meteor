package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import io.github.racoondog.bidoofmeteor.mixininterface.IJesus;
import io.github.racoondog.bidoofmeteor.mixininterface.INoFall;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.movement.Jesus;
import meteordevelopment.meteorclient.systems.modules.movement.NoFall;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@SuppressWarnings("ConstantConditions")
@Environment(EnvType.CLIENT)
public class NoFallPlace extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> toggleNoFallWhenSafe = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-nofall-when-safe")
        .description("Deactivate Meteor's NoFall when placing blocks that completely negate fall damage.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> anchor = sgGeneral.add(new BoolSetting.Builder()
        .name("anchor")
        .description("Centers the player and reduces movement when falling.")
        .defaultValue(true)
        .build()
    );

    private final Setting<NoFall.PlaceMode> mode = sgGeneral.add(new EnumSetting.Builder<NoFall.PlaceMode>()
        .name("place-mode")
        .description("Whether place mode places before you die or before you take damage.")
        .defaultValue(NoFall.PlaceMode.BeforeDamage)
        .build()
    );

    private final Setting<Boolean> smart = sgGeneral.add(new BoolSetting.Builder()
        .name("smart")
        .description("Don't use liquids when near water breakable blocks.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> removeAfter = sgGeneral.add(new BoolSetting.Builder()
        .name("remove-after")
        .description("Remove block after clutching.")
        .defaultValue(true)
        .build()
    );

    private boolean isFalling = false;
    private Boolean breakableCheck = null;
    private boolean reactivateNoFall = false;

    public NoFallPlace() {
        super(BidoofMeteor.CATEGORY, "no-fall-place", "Prevent fall damage by placing blocks.");
    }

    @Override
    public void onActivate() {
        verifyUseability();
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (reactivateNoFall) {
            Modules.get().get(NoFall.class).toggle();
            reactivateNoFall = false;
        }

        if (mc.player.fallDistance > 3) {
            List<NoFallItem> possibilities = new ArrayList<>(NoFallItem.LIST);

            isFalling = true;
            verifyUseability();

            //return if in creative, is above survivable water or has not fallen far enough
            if (mc.player.getAbilities().creativeMode) return;
            if (EntityUtils.isAboveWater(mc.player) && !hasWaterJesus() && !hasFrostWalker()) return;
            if (!mode.get().test(mc.player.fallDistance)) return;

            //check if about to land
            BlockHitResult result = mc.world.raycast(new RaycastContext(mc.player.getPos(), mc.player.getPos().subtract(0, 5, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));

            if (result == null || result.getType() != HitResult.Type.BLOCK) return;

            if (anchor.get()) PlayerUtils.centerPlayer();

            Iterator<NoFallItem> iterator = possibilities.iterator();
            while (iterator.hasNext()) {
                NoFallItem entry = iterator.next();

                if (!entry.canUse.getAsBoolean()) iterator.remove();
                if (smart.get() && entry.isLiquid) {
                    if (breakableCheck == null) breakableCheck(result.getBlockPos().up());
                    if (breakableCheck) entry.skipped = true;
                }
            }

            possibilities.sort(Comparator.comparingInt(o -> o.priority));

            NoFallItem chosen = null;
            FindItemResult itemResult = null;
            for (var entry : possibilities) {
                if (entry.skipped) continue;
                if (deathCheck(entry.effectiveness)) continue;
                if (!entry.canPlace.test(result.getBlockPos().up())) continue;
                itemResult = InvUtils.findInHotbar(entry.item);
                if (itemResult.found()) {
                    chosen = entry;
                    break;
                }
            }
            if (chosen == null) {
                if (smart.get()) {
                    //No suitable item found, trying again while ignoring smart fluid check
                    for (var entry : possibilities) {
                        if (!entry.skipped) continue;
                        if (!entry.canPlace.test(result.getBlockPos().up())) continue;
                        itemResult = InvUtils.findInHotbar(entry.item);
                        if (itemResult.found()) {
                            chosen = entry;
                            break;
                        }
                    }
                } else return;
            }

            clutch(chosen, itemResult);
        } else if (isFalling) {


            //reset
            breakableCheck = null;
            isFalling = false;
        }
    }

    private void breakableCheck(BlockPos center) {
        for (var blockPos : BlockPos.iterateOutwards(center, 5, 1, 5)) {
            if (!mc.world.getBlockState(blockPos).getMaterial().blocksMovement()) {
                breakableCheck = true;
                return;
            }
        }
        breakableCheck = false;
    }

    private boolean deathCheck(float effectiveness) {
        float height = mc.player.fallDistance * effectiveness;
        return height > Math.max(PlayerUtils.getTotalHealth(), 2);
    }

    private void clutch(NoFallItem noFallItem, FindItemResult findItemResult) {
        Rotations.rotate(mc.player.getYaw(), 90, 10, true, () -> {
            boolean toggleJesus = noFallItem.toggleJesus.getAsBoolean();
            NoFall noFall = Modules.get().get(NoFall.class);
            boolean toggleNoFall = toggleNoFallWhenSafe.get() && noFall.isActive();

            if (toggleJesus) Modules.get().get(Jesus.class).toggle();
            if (toggleNoFall) {
                noFall.toggle();
                reactivateNoFall = true;
            }

            if (noFallItem.doShift) mc.player.setSneaking(true);
            else if (noFallItem.preventShift) mc.player.setSneaking(false);

            if (findItemResult.isOffhand()) {
                use(Hand.OFF_HAND);
            } else {
                InvUtils.swap(findItemResult.slot(), true);
                use(Hand.MAIN_HAND);
                InvUtils.swapBack();
            }

            if (toggleJesus) Modules.get().get(Jesus.class).toggle();
        });
    }

    private void use(Hand hand) {
        if (mc.crosshairTarget == null) return;
        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) mc.interactionManager.interactBlock(mc.player, hand, (BlockHitResult) mc.crosshairTarget);
        mc.interactionManager.interactItem(mc.player, hand);
    }

    private void verifyUseability() {
        if (((INoFall) Modules.get().get(NoFall.class)).isModeBlockPlace()) {
            this.toggle();
            ChatUtils.warning("No Fall Place is not compatible with No Fall modes Bucket and AirPlace.");
        }
    }

    protected static boolean canUseWater() {
        return !MinecraftClient.getInstance().player.world.getDimension().ultrawarm();
    }

    protected static boolean hasFrostWalker() {
        ItemStack boots = MinecraftClient.getInstance().player.getInventory().getArmorStack(0);
        if (boots.isEmpty() || !boots.hasEnchantments()) return false;
        return EnchantmentHelper.getLevel(Enchantments.FROST_WALKER, boots) >= 1;
    }

    protected static boolean hasWaterJesus() {
        Jesus jesus = Modules.get().get(Jesus.class);
        return jesus.isActive() && ((IJesus) jesus).getWaterMode().equals(Jesus.Mode.Solid);
    }

    protected static boolean hasPowderSnowJesus() {
        return Modules.get().get(Jesus.class).canWalkOnPowderSnow();
    }

    protected static boolean hasLeatherBoots() {
        return MinecraftClient.getInstance().player.getInventory().getArmorStack(0).isOf(Items.LEATHER_BOOTS);
    }

    public static final class NoFallItem {
        public static final List<NoFallItem> LIST = new ArrayList<>();

        @PreInit
        public static void init() {
        }

        private static final NoFallItem WATER_BUCKET = of(Items.WATER_BUCKET).canUse(() -> canUseWater() && !hasFrostWalker() && !hasWaterJesus()).priority(1).toggleJesus(NoFallPlace::hasWaterJesus).liquid().canPlace((blockPos -> !(MinecraftClient.getInstance().world.getBlockState(blockPos.down()).getBlock() instanceof FluidFillable))).safe().build();
        private static final NoFallItem POWDER_SNOW_BUCKET = of(Items.POWDER_SNOW_BUCKET).canUse(() -> !hasPowderSnowJesus() && !hasLeatherBoots()).priority(2).toggleJesus(NoFallPlace::hasPowderSnowJesus).safe().build();
        private static final NoFallItem COBWEB = of(Items.COBWEB).priority(1).canPlace((blockPos -> {
            for (var pos : BlockPos.iterateOutwards(blockPos, 3, 1, 3)) {
                if (!MinecraftClient.getInstance().world.getFluidState(pos).isEmpty()) return false;
            }
            return true;
        })).safe().build();
        private static final NoFallItem TWISTING_VINE = of(Items.TWISTING_VINES).priority(2).canPlace((blockPos -> {
            BlockState state = MinecraftClient.getInstance().world.getBlockState(blockPos.down());
            return state.isOf(Blocks.TWISTING_VINES) || state.isOf(Blocks.TWISTING_VINES_PLANT) || state.isSideSolidFullSquare(MinecraftClient.getInstance().world, blockPos, Direction.UP);
        })).safe().build();
        private static final NoFallItem SCAFFOLDING = of(Items.SCAFFOLDING).priority(2).canPlace((blockPos -> ScaffoldingBlock.calculateDistance(MinecraftClient.getInstance().world, blockPos) < 7)).crouch().safe().build();
        private static final NoFallItem WHITE_BED = of(Items.WHITE_BED).priority(5).canPlace((blockPos -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            Direction direction = player.getHorizontalFacing();
            //todo fix checking for replaceable block
            return player.world.getBlockState(blockPos.offset(direction)).canReplace(new ItemPlacementContext(player, player.getActiveHand(), Items.WHITE_BED.getDefaultStack(), new BlockHitResult(null, Direction.UP, blockPos.offset(direction), false)))
                && player.world.getBlockState(blockPos.down()).isSideSolidFullSquare(player.world, blockPos.down(), Direction.UP) && player.world.getBlockState(blockPos.down().offset(direction)).isSideSolidFullSquare(player.world, blockPos.down().offset(direction), Direction.UP);
        })).effectiveness(0.5f).build();
        private static final NoFallItem SLIME_BLOCK = of(Items.SLIME_BLOCK).priority(2).preventCrouch().safe().build();
        private static final NoFallItem HAY_BLOCK = of(Items.HAY_BLOCK).priority(3).effectiveness(0.2f).build();
        private static final NoFallItem HONEY_BLOCK = of(Items.HONEY_BLOCK).priority(3).effectiveness(0.2f).build();
        private static final NoFallItem SWEET_BERRY = of(Items.SWEET_BERRIES).priority(3).canPlace((blockPos -> {
            BlockState state = MinecraftClient.getInstance().world.getBlockState(blockPos.down());
            return state.isIn(BlockTags.DIRT) || state.isOf(Blocks.FARMLAND);
        })).crouch().safe().build();


        public final Item item;
        public final BooleanSupplier canUse;
        public final int priority;
        public final BooleanSupplier toggleJesus;
        public final boolean isLiquid;
        public final Predicate<BlockPos> canPlace;
        public final boolean doShift;
        public final boolean preventShift;
        public final boolean safe;
        public final float effectiveness;
        public boolean skipped = false;

        public NoFallItem(Item item, BooleanSupplier canUse, int priority,
                          BooleanSupplier toggleJesus, boolean isLiquid, Predicate<BlockPos> canPlace,
                          boolean doShift, boolean preventShift, boolean safe, float effectiveness) {
            this.item = item;
            this.canUse = canUse;
            this.priority = priority;
            this.toggleJesus = toggleJesus;
            this.isLiquid = isLiquid;
            this.canPlace = canPlace;
            this.doShift = doShift;
            this.preventShift = preventShift;
            this.safe = safe;
            this.effectiveness = effectiveness;
        }

        private static NoFallItemBuilder of(Item item) {
            return new NoFallItemBuilder(item);
        }

        private static class NoFallItemBuilder {
            private final Item item;
            private BooleanSupplier canUse = () -> true;
            private int priority;
            private BooleanSupplier toggleJesus = () -> false;
            private boolean isLiquid = false;
            private Predicate<BlockPos> canPlace = blockPos -> true;
            private boolean doShift = false;
            private boolean preventShift = false;
            private boolean safe = false;
            private float effectiveness = 0;

            private NoFallItemBuilder(Item item) {
                this.item = item;
            }

            private NoFallItemBuilder canUse(BooleanSupplier canUse) {
                this.canUse = canUse;
                return this;
            }

            private NoFallItemBuilder priority(int priority) {
                this.priority = priority;
                return this;
            }

            private NoFallItemBuilder toggleJesus(BooleanSupplier supplier) {
                this.toggleJesus = supplier;
                return this;
            }

            private NoFallItemBuilder liquid() {
                this.isLiquid = true;
                return this;
            }

            private NoFallItemBuilder canPlace(Predicate<BlockPos> canPlace) {
                this.canPlace = canPlace;
                return this;
            }

            private NoFallItemBuilder crouch() {
                this.doShift = true;
                return this;
            }

            private NoFallItemBuilder preventCrouch() {
                this.preventShift = true;
                return this;
            }

            private NoFallItemBuilder safe() {
                this.safe = true;
                return this;
            }

            private NoFallItemBuilder effectiveness(float effectiveness) {
                this.effectiveness = effectiveness;
                return this;
            }

            private NoFallItem build() {
                NoFallItem out = new NoFallItem(this.item, this.canUse, this.priority, this.toggleJesus, this.isLiquid, this.canPlace, this.doShift, this.preventShift, this.safe, this.effectiveness);
                LIST.add(out);
                return out;
            }
        }
    }
}
