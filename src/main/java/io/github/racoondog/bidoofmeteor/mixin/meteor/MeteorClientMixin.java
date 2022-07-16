package io.github.racoondog.bidoofmeteor.mixin.meteor;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.misc.DiscordPresence;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorClient.class, remap = false)
public abstract class MeteorClientMixin {
    @Redirect(method = "lambda$onInitializeClient$1", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/systems/modules/misc/DiscordPresence;toggle()V"))
    private static void disableRPCByDefault(DiscordPresence instance) {}
}
