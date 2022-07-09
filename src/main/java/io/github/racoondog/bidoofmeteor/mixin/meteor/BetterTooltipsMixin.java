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
import net.minecraft.text.Text;
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
            boolean isBook = event.itemStack.getItem().equals(Items.ENCHANTED_BOOK);
            if (isBook && AnvilTooltipsImpl.isBookEmpty(tag)) return;
            if (event.itemStack.getItem().isEnchantable(event.itemStack) || isBook) {
                int repairCost = tag.contains("RepairCost") ? tag.getInt("RepairCost") : 0;
                int uses = AnvilTooltipsImpl.costToUses(repairCost);
                NbtList list = isBook ? tag.getList("StoredEnchantments", 10) : tag.getList("Enchantments", 10);
                if (list.isEmpty()) return;
                event.list.add(Text.literal("%sAnvil Uses: %s%d%s.".formatted(Formatting.GRAY, AnvilTooltipsImpl.getFormatting(list, uses, isBook), uses, Formatting.GRAY)));
                event.list.add(Text.literal("%sBase Cost: %s%d%s.".formatted(Formatting.GRAY, AnvilTooltipsImpl.getFormatting(list, uses, isBook), isBook ? AnvilTooltipsImpl.getBaseCost(list) + repairCost : repairCost, Formatting.GRAY)));
            }
        }
    }
}
