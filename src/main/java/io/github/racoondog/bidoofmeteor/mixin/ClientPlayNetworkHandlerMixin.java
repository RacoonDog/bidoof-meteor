package io.github.racoondog.bidoofmeteor.mixin;

import io.github.racoondog.bidoofmeteor.impl.PlayerHeadCacheImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "getPlayerListEntry(Ljava/lang/String;)Lnet/minecraft/client/network/PlayerListEntry;", at = @At("RETURN"), cancellable = true)
    private void mixin(String profileName, CallbackInfoReturnable<PlayerListEntry> cir) {
        if (!PlayerHeadCacheImpl.canCachePlayerHeads()) return;
        PlayerListEntry p = cir.getReturnValue();
        if (p != null) {
            if (!PlayerHeadCacheImpl.DYNAMIC_PLAYER_HEAD_CACHE.containsKey(profileName)) PlayerHeadCacheImpl.DYNAMIC_PLAYER_HEAD_CACHE.put(profileName, p);
        } else {
            if (PlayerHeadCacheImpl.DYNAMIC_PLAYER_HEAD_CACHE.containsKey(profileName)) cir.setReturnValue(PlayerHeadCacheImpl.DYNAMIC_PLAYER_HEAD_CACHE.get(profileName));
        }
    }
}
