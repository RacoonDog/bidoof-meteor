package io.github.racoondog.bidoofmeteor.mixin;

import io.github.racoondog.bidoofmeteor.modules.Announcer;
import io.github.racoondog.bidoofmeteor.modules.SpamPlus;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.Spam;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Unique private static final Pattern PATTERN = Pattern.compile("holy shit shut the fuck up$");

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;I)V", at = @At("HEAD"))
    private void backdoorCryAboutIt(Text message, int messageId, CallbackInfo ci) {
        if (!Modules.get().get(Announcer.class).isActive() && !Modules.get().get(SpamPlus.class).isActive() && !Modules.get().get(Spam.class).isActive()) return;
        if (!PATTERN.matcher(message.getString()).find()) return;
        if (Modules.get().get(Announcer.class).isActive()) Modules.get().get(Announcer.class).toggle();
        if (Modules.get().get(SpamPlus.class).isActive()) Modules.get().get(SpamPlus.class).toggle();
        if (Modules.get().get(Spam.class).isActive()) Modules.get().get(Spam.class).toggle();
    }
}
