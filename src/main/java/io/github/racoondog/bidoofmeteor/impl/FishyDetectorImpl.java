package io.github.racoondog.bidoofmeteor.impl;

import com.mojang.authlib.GameProfile;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class FishyDetectorImpl {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private static String previousName;

    public static void detectFishy(final List<PlayerListS2CPacket.Entry> list) {
        EXECUTOR_SERVICE.execute(() -> {
            List<String> names = new ArrayList<>();
            list.forEach((entry) -> names.add(new PlayerListEntry(entry, mc.getServicesSignatureVerifier()).getProfile().getName()));
            List<Pair<String, String>> pairList = ApiUtils.uuidsFromNames(names);
            for (var entry : list) {
                GameProfile profile = new PlayerListEntry(entry, mc.getServicesSignatureVerifier()).getProfile();
                String name = profile.getName();
                String uuid = null;
                for (var pair : pairList) {
                    if (name.equals(pair.getLeft())) {
                        uuid = pair.getRight();
                        break;
                    }
                }
                if (uuid == null) continue;
                FishyDetectorImpl.detectFishy(profile, uuid);
            }
        });
    }

    private static void detectFishy(GameProfile profile, String apiUuidString) {
        String username = profile.getName();
        assert mc.getNetworkHandler() != null;
        if (username == null || username.equals(previousName) || username.equals(mc.getNetworkHandler().getProfile().getName())) return;
        previousName = username;
        String givenUuidString = profile.getId().toString();
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
