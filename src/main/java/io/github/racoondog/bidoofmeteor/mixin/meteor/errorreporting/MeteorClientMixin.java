package io.github.racoondog.bidoofmeteor.mixin.meteor.errorreporting;

import io.github.racoondog.bidoofmeteor.util.ExceptionUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorClient.class, remap = false)
public abstract class MeteorClientMixin {
    @Redirect(method = "lambda$onInitializeClient$3", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/addons/MeteorAddon;getPackage()Ljava/lang/String;"))
    private static String reportOutdatedAddons(MeteorAddon instance) {
        try {
            return instance.getPackage();
        } catch (AbstractMethodError e) {
            return ExceptionUtils.packageFallback(instance, e);
        }
    }
}
