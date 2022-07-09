package io.github.racoondog.bidoofmeteor.mixin.meteor;

import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.hud.elements.ActiveModulesHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(value = ActiveModulesHud.class, remap = false)
public abstract class ActiveModulesHudMixin {
    @Shadow @Final
    private SettingGroup sgGeneral;

    @Shadow @Final
    private Setting<ActiveModulesHud.ColorMode> colorMode;

    private Setting<Double> saturation;

    private Setting<Double> brightness;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInit(CallbackInfo ci) {
        saturation = sgGeneral.add(new DoubleSetting.Builder()
            .name("saturation")
            .defaultValue(1.0d)
            .sliderRange(0.0d, 1.0d)
            .decimalPlaces(2)
            .visible(() -> colorMode.get().equals(ActiveModulesHud.ColorMode.Rainbow))
            .build()
        );
        brightness = sgGeneral.add(new DoubleSetting.Builder()
            .name("brightness")
            .defaultValue(1.0d)
            .sliderRange(0.0d, 1.0d)
            .decimalPlaces(2)
            .visible(() -> colorMode.get().equals(ActiveModulesHud.ColorMode.Rainbow))
            .build()
        );
    }

    @ModifyArgs(method = "renderModule", at = @At(value = "INVOKE", target = "Ljava/awt/Color;HSBtoRGB(FFF)I"))
    private void modifyArgs(Args args) {
        args.set(1, saturation.get().floatValue());
        args.set(2, brightness.get().floatValue());
    }
}
