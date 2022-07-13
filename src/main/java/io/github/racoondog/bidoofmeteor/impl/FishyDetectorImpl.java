package io.github.racoondog.bidoofmeteor.impl;

import com.mojang.authlib.GameProfile;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class FishyDetectorImpl {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private static String previousName;

    public static void detectFishy(final GameProfile profile) {
        final String username = profile.getName();
        assert mc.getNetworkHandler() != null;
        if (username == null || username.equals(previousName) || username.equals(mc.getNetworkHandler().getProfile().getName())) return;
        previousName = username;
        EXECUTOR_SERVICE.execute(() -> {
            String givenUuidString = profile.getId().toString();
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
        });
    }
}
