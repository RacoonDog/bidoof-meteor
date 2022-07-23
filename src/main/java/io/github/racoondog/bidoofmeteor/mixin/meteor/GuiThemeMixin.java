package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.themes.IRecolorGuiTheme;
import meteordevelopment.meteorclient.gui.GuiTheme;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = GuiTheme.class, remap = false)
public abstract class GuiThemeMixin {
    @Shadow @Final @Mutable public String name;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyName(String name, CallbackInfo ci) {
        if (this instanceof IRecolorGuiTheme iRecolorGuiTheme) {
            this.name = iRecolorGuiTheme.getName();
        }
    }
}
