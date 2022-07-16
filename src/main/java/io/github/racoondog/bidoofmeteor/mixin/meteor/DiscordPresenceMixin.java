package io.github.racoondog.bidoofmeteor.mixin.meteor;

import meteordevelopment.discordipc.RichPresence;
import meteordevelopment.meteorclient.systems.modules.misc.DiscordPresence;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(DiscordPresence.class)
public abstract class DiscordPresenceMixin {
    @Redirect(method = "onTick", at = @At(value = "INVOKE", target = "Lmeteordevelopment/discordipc/RichPresence;setState(Ljava/lang/String;)V"))
    private void addStates(RichPresence rpc, String defaultState) {
        assert mc.currentScreen != null;
        String className = mc.currentScreen.getClass().getName();
        if (className.startsWith("com.wildfire.gui.screen")) rpc.setState("Changing options");
        else rpc.setState(defaultState);
    }
}
