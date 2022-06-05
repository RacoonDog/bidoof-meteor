package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.impl.AnvilTooltipsImpl;
import meteordevelopment.meteorclient.events.game.ItemStackTooltipEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.render.BetterTooltips;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = BetterTooltips.class, remap = false)
public abstract class BetterTooltipsMixin {
    @Shadow @Final
    private SettingGroup sgOther;

    private Setting<Boolean> anvilUses;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        anvilUses = sgOther.add(new BoolSetting.Builder()
            .name("anvil-uses")
            .description("Shows the amount of times an item has been used in an anvil.")
            .defaultValue(true)
            .build()
        );
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    private void inject(ItemStackTooltipEvent event, CallbackInfo ci) {
        if (anvilUses.get()) {
            NbtCompound tag = event.itemStack.getNbt();
            if (tag == null) return;
            boolean bl = event.itemStack.getItem().equals(Items.ENCHANTED_BOOK);
            if ((event.itemStack.getItem().isEnchantable(event.itemStack) || bl)) {
                int repairCost = tag.contains("RepairCost") ? tag.getInt("RepairCost") : 0;
                int uses = AnvilTooltipsImpl.costToUses(repairCost);
                event.list.add(new LiteralText("%sAnvil Uses: %s%d%s.".formatted(Formatting.GRAY, Formatting.RED, uses, Formatting.GRAY)));
                if (bl && !AnvilTooltipsImpl.isBookEmpty(tag)) {
                    NbtList list = tag.getList("StoredEnchantments", 10);
                    if (list.isEmpty()) return;
                    event.list.add(new LiteralText("%sBase Cost: %s%d%s.".formatted(Formatting.GRAY, Formatting.RED, AnvilTooltipsImpl.getBaseCost(list) + repairCost, Formatting.GRAY)));
                } else {
                    event.list.add(new LiteralText("%sBase Cost: %s%d%s.".formatted(Formatting.GRAY, Formatting.RED, repairCost, Formatting.GRAY)));
                }
            }
        }
    }
}
