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

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class FishyDetectorImpl {
    private static String previousName;

    public static void detectFishy(final List<PlayerListS2CPacket.Entry> list) {
        ApiUtils.API_EXECUTOR_SERVICE.execute(() -> {
            List<String> names = new ArrayList<>();
            for (var entry : list) {
                String name = new PlayerListEntry(entry, mc.getServicesSignatureVerifier(), false).getProfile().getName();
                if (name.contains(".")) {
                    ChatUtils.info("New player detected: (highlight)%s(default), (highlight)%s (default)is not a valid in-use player name", name, name);
                    continue;
                }
                names.add(name);
            }
            List<Pair<String, String>> pairList = ApiUtils.uuidsFromNames(names);
            for (var entry : list) {
                GameProfile profile = new PlayerListEntry(entry, mc.getServicesSignatureVerifier(), false).getProfile();
                String username = profile.getName();
                assert mc.getNetworkHandler() != null;
                if (username == null || username.equals(previousName) || username.equals(mc.getNetworkHandler().getProfile().getName())) return;
                previousName = username;

                String apiUuidString = null;
                for (var pair : pairList) {
                    if (username.equals(pair.getLeft())) {
                        apiUuidString = pair.getRight();
                        break;
                    }
                }

                String givenUuidString = profile.getId().toString();
                String anomalyText = "(highlight)No Anomalies(default).";
                if (apiUuidString == null) anomalyText = "(highlight)%s (default)is not a valid in-use player name.".formatted(username);
                else {
                    givenUuidString = givenUuidString.replace("-", "");
                    if (!apiUuidString.equals(givenUuidString)) {
                        anomalyText = "UUID mismatch. Api Expected: (highlight)%s(default), Wrongly Received: (highlight)%s(default).".formatted(apiUuidString, givenUuidString);
                    }
                }
                ChatUtils.info("New player detected: (highlight)%s(default), %s", username, anomalyText);
            }
        });
    }
}
