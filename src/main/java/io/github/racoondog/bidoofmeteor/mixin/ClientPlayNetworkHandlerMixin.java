package io.github.racoondog.bidoofmeteor.mixin;

import com.mojang.authlib.GameProfile;
import io.github.racoondog.bidoofmeteor.impl.FishyDetectorImpl;
import io.github.racoondog.bidoofmeteor.impl.PlayerHeadCacheImpl;
import io.github.racoondog.bidoofmeteor.mixininterface.IClientPlayNetworkHandler;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements IClientPlayNetworkHandler {
    @Shadow
    @Final
    private Map<UUID, PlayerListEntry> playerListEntries;

    @Shadow
    @Final
    private GameProfile profile;

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

    @Inject(method = "onPlayerList", at = @At(value = "TAIL"))
    private void fishydetector(PlayerListS2CPacket packet, CallbackInfo ci) {
        for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
            PlayerListEntry playerListEntry = new PlayerListEntry(entry, mc.getServicesSignatureVerifier());
            FishyDetectorImpl.detectFishy(playerListEntry.getProfile());
        }
    }

    @Override
    public Map<UUID, PlayerListEntry> getPlayerListEntries() {
        return playerListEntries;
    }
}
