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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.WaterFluid;
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
import java.util.function.BiPredicate;
import java.util.function.Supplier;

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
        .description("Disable jesus when using liquids, don't use water when near water breakable blocks, etc.")
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

                if (!entry.canUse.get()) iterator.remove();
                if (smart.get() && entry.isLiquid) {
                    //todo ignore breakableCheck if no other valid targets found
                    if (breakableCheck == null) breakableCheck(result.getBlockPos().up());
                    if (breakableCheck) iterator.remove();
                }
            }

            possibilities.sort(Comparator.comparingInt(o -> o.priority.get()));

            NoFallItem chosen = null;
            FindItemResult itemResult = null;
            for (var entry : possibilities) {
                //todo dont clutch if death is certain
                if (!entry.canPlace.test(result.getBlockPos().up(), mc.player.getHorizontalFacing())) continue;
                itemResult = InvUtils.findInHotbar(entry.item);
                if (itemResult.found()) {
                    chosen = entry;
                    break;
                }
            }
            if (chosen == null) return;

            clutch(chosen, itemResult);
        } else if (isFalling) {


            //reset
            breakableCheck = null;
            isFalling = false;
        }
    }

    private void breakableCheck(BlockPos center) {
        assert mc.world != null;
        for (var blockPos : BlockPos.iterateOutwards(center, 5, 1, 5)) {
            if (!mc.world.getBlockState(blockPos).getMaterial().blocksMovement()) {
                breakableCheck = true;
                return;
            }
        }
        breakableCheck = false;
    }

    private void clutch(NoFallItem noFallItem, FindItemResult findItemResult) {
        assert mc.player != null;
        Rotations.rotate(mc.player.getYaw(), 90, 10, true, () -> {
            assert mc.interactionManager != null;

            boolean toggleJesus = noFallItem.toggleJesus.get();
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
        assert mc.interactionManager != null;
        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) mc.interactionManager.interactBlock(mc.player, hand, (BlockHitResult) mc.crosshairTarget);
        mc.interactionManager.interactItem(mc.player, hand);
    }

    private void verifyUseability() {
        if (((INoFall) Modules.get().get(NoFall.class)).isModeBlockPlace()) {
            this.toggle();
            ChatUtils.warning("(highlight)No Fall Place(default) is not compatible with No Fall modes (highlight)Bucket(default) and (highlight)AirPlace(default).");
        }
    }

    protected static boolean canUseWater() {
        assert MinecraftClient.getInstance().player != null;
        return !MinecraftClient.getInstance().player.world.getDimension().ultrawarm();
    }

    protected static boolean hasFrostWalker() {
        assert MinecraftClient.getInstance().player != null;
        ItemStack boots = MinecraftClient.getInstance().player.getInventory().getArmorStack(0);
        if (boots.isEmpty() || !boots.hasEnchantments()) return false;
        return EnchantmentHelper.getLevel(Enchantments.FROST_WALKER, boots) >= 1;
    }

    protected static boolean hasWaterJesus() {
        Jesus jesus = Modules.get().get(Jesus.class);
        return jesus.isActive() && ((IJesus) jesus).getWaterMode().equals(Jesus.Mode.Solid);
    }

    protected static boolean hasLavaJesus() {
        Jesus jesus = Modules.get().get(Jesus.class);
        return jesus.isActive() && ((IJesus) jesus).getLavaMode().equals(Jesus.Mode.Solid);
    }

    protected static boolean hasPowderSnowJesus() {
        return Modules.get().get(Jesus.class).canWalkOnPowderSnow();
    }

    protected static int getLavaPriority() {
        assert MinecraftClient.getInstance().player != null;
        if (MinecraftClient.getInstance().player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) return 1;
        return 9 - EnchantmentHelper.getEquipmentLevel(Enchantments.FIRE_PROTECTION, MinecraftClient.getInstance().player) * 2;
    }

    protected static boolean hasLeatherBoots() {
        assert MinecraftClient.getInstance().player != null;
        return MinecraftClient.getInstance().player.getInventory().getArmorStack(0).isOf(Items.LEATHER_BOOTS);
    }

    public record NoFallItem(Item item, Supplier<Boolean> canUse, Supplier<Integer> priority,
                             Supplier<Boolean> toggleJesus, boolean isLiquid, BiPredicate<BlockPos, Direction> canPlace,
                             boolean doShift, boolean preventShift, boolean safe) {
        public static final List<NoFallItem> LIST = new ArrayList<>();

        @PreInit public static void init() {}

        private static final NoFallItem WATER_BUCKET = of(Items.WATER_BUCKET).canUse(() -> canUseWater() && !hasFrostWalker() && !hasWaterJesus()).priority(1).toggleJesus(NoFallPlace::hasWaterJesus).liquid().canPlace(((blockPos, direction) -> {
            assert MinecraftClient.getInstance().world != null;
            return !(MinecraftClient.getInstance().world.getBlockState(blockPos.down()).getBlock() instanceof FluidFillable);
        })).safe().build();
        private static final NoFallItem LAVA_BUCKET = of(Items.LAVA_BUCKET).canUse(() -> !hasLavaJesus()).priority(NoFallPlace::getLavaPriority).toggleJesus(NoFallPlace::hasLavaJesus).liquid().build();
        private static final NoFallItem POWDER_SNOW_BUCKET = of(Items.POWDER_SNOW_BUCKET).canUse(() -> !hasPowderSnowJesus() && !hasLeatherBoots()).priority(2).toggleJesus(NoFallPlace::hasPowderSnowJesus).safe().build();
        private static final NoFallItem COBWEB = of(Items.COBWEB).priority(1).canPlace(((blockPos, direction) -> {
            //Check for nearby fluids
            return true;
        })).safe().build();
        private static final NoFallItem TWISTING_VINE = of(Items.TWISTING_VINES).priority(2).canPlace(((blockPos, direction) -> {
            assert MinecraftClient.getInstance().world != null;
            BlockState state = MinecraftClient.getInstance().world.getBlockState(blockPos.down());
            return state.isOf(Blocks.TWISTING_VINES) || state.isOf(Blocks.TWISTING_VINES_PLANT) || state.isSideSolidFullSquare(MinecraftClient.getInstance().world, blockPos, Direction.UP);
        })).safe().build();
        private static final NoFallItem SCAFFOLDING = of(Items.SCAFFOLDING).priority(2).canPlace(((blockPos, direction) -> {
            assert MinecraftClient.getInstance().world != null;
            return ScaffoldingBlock.calculateDistance(MinecraftClient.getInstance().world, blockPos) < 7;
        })).crouch().safe().build();
        private static final NoFallItem WHITE_BED = of(Items.WHITE_BED).priority(5).canPlace(((blockPos, direction) -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;
            //todo fix checking for replaceable block
            return player.world.getBlockState(blockPos.offset(direction)).canReplace(new ItemPlacementContext(player, player.getActiveHand(), Items.WHITE_BED.getDefaultStack(), new BlockHitResult(null, Direction.UP, blockPos.offset(direction), false)))
                && player.world.getBlockState(blockPos.down()).isSideSolidFullSquare(player.world, blockPos.down(), Direction.UP) && player.world.getBlockState(blockPos.down().offset(direction)).isSideSolidFullSquare(player.world, blockPos.down().offset(direction), Direction.UP);
        })).build();
        private static final NoFallItem SLIME_BLOCK = of(Items.SLIME_BLOCK).priority(2).preventCrouch().safe().build();
        private static final NoFallItem HAY_BLOCK = of(Items.HAY_BLOCK).priority(3).build();
        private static final NoFallItem HONEY_BLOCK = of(Items.HONEY_BLOCK).priority(3).build();
        private static final NoFallItem SWEET_BERRY = of(Items.SWEET_BERRIES).priority(3).canPlace(((blockPos, direction) -> {
            assert MinecraftClient.getInstance().world != null;
            BlockState state = MinecraftClient.getInstance().world.getBlockState(blockPos.down());
            return state.isIn(BlockTags.DIRT) || state.isOf(Blocks.FARMLAND);
        })).crouch().safe().build();

        private static NoFallItemBuilder of(Item item) {
                return new NoFallItemBuilder(item);
            }

        private static class NoFallItemBuilder {
            private final Item item;
            private Supplier<Boolean> canUse = () -> true;
            private Supplier<Integer> priority;
            private Supplier<Boolean> toggleJesus = () -> false;
            private boolean isLiquid = false;
            private BiPredicate<BlockPos, Direction> canPlace = (blockPos, direction) -> true;
            private boolean doShift = false;
            private boolean preventShift = false;
            private boolean safe = false;

            private NoFallItemBuilder(Item item) {
                this.item = item;
            }
            private NoFallItemBuilder canUse(Supplier<Boolean> canUse) {
                this.canUse = canUse;
                return this;
            }

            private NoFallItemBuilder priority(Supplier<Integer> prioritySupplier) {
                this.priority = prioritySupplier;
                return this;
            }

            private NoFallItemBuilder priority(int priority) {
                this.priority = () -> priority;
                return this;
            }

            private NoFallItemBuilder toggleJesus(Supplier<Boolean> supplier) {
                this.toggleJesus = supplier;
                return this;
            }

            private NoFallItemBuilder liquid() {
                this.isLiquid = true;
                return this;
            }

            private NoFallItemBuilder canPlace(BiPredicate<BlockPos, Direction> canPlace) {
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

            private NoFallItem build() {
                NoFallItem out = new NoFallItem(this.item, this.canUse, this.priority, this.toggleJesus, this.isLiquid, this.canPlace, this.doShift, this.preventShift, this.safe);
                LIST.add(out);
                return out;
            }
        }
    }
}
