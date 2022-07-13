package io.github.racoondog.bidoofmeteor.mixin;

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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements IClientPlayNetworkHandler {
    @Shadow
    @Final
    private Map<UUID, PlayerListEntry> playerListEntries;

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

    @Inject(method = "onPlayerList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/SocialInteractionsManager;setPlayerOnline(Lnet/minecraft/client/network/PlayerListEntry;)V"))
    private void fishydetector(PlayerListS2CPacket packet, CallbackInfo ci) {
        for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
            PlayerListEntry playerListEntry = new PlayerListEntry(entry, mc.getServicesSignatureVerifier());
            String username = playerListEntry.getProfile().getName();
            assert mc.getNetworkHandler() != null;
            if (username.equals(mc.getNetworkHandler().getProfile().getName())) return;
            String givenUuidString = playerListEntry.getProfile().getId().toString();
            String apiUuidString = ApiUtils.uuidFromName(username);
            String anomalyText = "(highlight)No Anomalies(default).";
            if (apiUuidString == null) {
                anomalyText = "(highlight)%s (default)is not a valid in-use player name.".formatted(username);
            } else {
                givenUuidString = givenUuidString.replace("-", "");
                if (!apiUuidString.equals(givenUuidString)) {
                    anomalyText = "UUID mismatch. Api Expected: (highlight)%s(default), Wrongly Received: (highlight)%s(default).".formatted(apiUuidString, givenUuidString);
                }
            }
            ChatUtils.info("New player detected: (highlight)%s(default), %s", username, anomalyText);
        }
    }

    @Override
    public Map<UUID, PlayerListEntry> getPlayerListEntries() {
        return playerListEntries;
    }
}
