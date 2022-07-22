package io.github.racoondog.bidoofmeteor.mixin.meteor;

import io.github.racoondog.bidoofmeteor.mixininterface.IBetterChat;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.misc.BetterChat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = BetterChat.class, remap = false)
public abstract class BetterChatMixin implements IBetterChat {
    @Shadow @Final private SettingGroup sgGeneral;
    @Shadow @Final private Setting<Boolean> playerHeads;

    private Setting<Boolean> cachePlayerHeads;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void cachePlayerHeads(CallbackInfo ci) {
        cachePlayerHeads = sgGeneral.add(new BoolSetting.Builder()
            .name("cache-player-heads")
            .description("Caches player heads in order to show them even after they leave.")
            .defaultValue(true)
            .visible(playerHeads::get)
            .build()
        );
    }

    @Override
    public Setting<Boolean> getCachePlayerHeads() {
        return this.cachePlayerHeads;
    }
}
