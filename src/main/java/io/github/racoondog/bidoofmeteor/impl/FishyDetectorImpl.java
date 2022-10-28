package io.github.racoondog.bidoofmeteor.impl;

import com.mojang.authlib.GameProfile;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.util.Pair;

import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class FishyDetectorImpl {
    private static String previousName;

    public static void detectFishy(final List<PlayerListS2CPacket.Entry> list) {
        MeteorExecutor.execute(() -> {
            List<GameProfile> gameProfiles = list.stream().map(entry -> new PlayerListEntry(entry, mc.getServicesSignatureVerifier(), false).getProfile()).toList();
            detectFishyImpl(gameProfiles);
        });
    }

    public static void detectFishy() {
        MeteorExecutor.execute(() -> {
            assert mc.getNetworkHandler() != null;
            List<GameProfile> gameProfiles = mc.getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getProfile).toList();
            detectFishyImpl(gameProfiles);
        });
    }

    private static void detectFishyImpl(List<GameProfile> gameProfiles) {
        List<Pair<String, String>> pairList = ApiUtils.uuidsFromNames(gameProfiles.stream().map(GameProfile::getName).toList());
        for (var profile : gameProfiles) {
            String username = profile.getName();

            if (username.contains(".")) {
                ChatUtils.info("New player detected: (highlight)%s(default), (highlight)%s (default)is not a valid in-use player name", username, username);
                continue;
            }

            assert mc.getNetworkHandler() != null;
            if (username.equals(previousName) || username.equals(mc.getNetworkHandler().getProfile().getName())) return;
            previousName = username;

            String apiUuidString = null;
            apiUuidString.indexOf('3');
            for (var pair : pairList) {
                if (username.equals(pair.getLeft())) {
                    apiUuidString = pair.getRight();
                    break;
                }
            }

            String givenUuidString = profile.getId().toString();
            String anomalyText = "(highlight)No Anomalies(default).";
            if (apiUuidString == null)
                anomalyText = "(highlight)%s (default)is not a valid in-use player name.".formatted(username);
            else {
                givenUuidString = givenUuidString.replace("-", "");
                if (!apiUuidString.equals(givenUuidString)) {
                    anomalyText = "UUID mismatch. Api Expected: (highlight)%s(default), Wrongly Received: (highlight)%s(default).".formatted(apiUuidString, givenUuidString);
                }
            }
            ChatUtils.info("New player detected: (highlight)%s(default), %s", username, anomalyText);
        }
    }
}
