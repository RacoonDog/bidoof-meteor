package io.github.racoondog.bidoofmeteor.mixin;

import io.github.racoondog.bidoofmeteor.modules.Jukebox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(MusicTracker.class)
public abstract class MusicTrackerMixin {
    @Redirect(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)V"))
    private void redirect(SoundManager instance, SoundInstance sound) {
        if (Jukebox.shouldCancelMusic(sound.getId())) return;
        instance.play(sound);
    }
}
