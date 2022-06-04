package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.events.game.ItemStackTooltipEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.render.BetterTooltips;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
            BidoofMeteor.LOG.info("Is tag null: %s".formatted(tag == null));
            if (event.itemStack.getItem().isEnchantable(event.itemStack) && tag != null) {
                int repairCost = tag.contains("RepairCost") ? tag.getInt("RepairCost") : 0;
                BidoofMeteor.LOG.info("Repair Cost: %s".formatted(repairCost));
                int uses = costToUses(repairCost);
                BidoofMeteor.LOG.info("Anvil Uses: %s".formatted(uses));
                event.list.add(new LiteralText("%sAnvil Uses: %s%d%s.".formatted(Formatting.GRAY, Formatting.RED, uses, Formatting.GRAY)));
            }
        }
    }

    @Unique
    private static int costToUses(int cost) {
        return switch (cost) {
            default -> cost;
            case 3 -> 2;
            case 7 -> 3;
            case 15 -> 4;
            case 31 -> 5;
        };
    }
}
