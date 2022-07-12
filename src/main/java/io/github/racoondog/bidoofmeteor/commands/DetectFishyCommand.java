package io.github.racoondog.bidoofmeteor.commands;

import com.mojang.brigadier.context.CommandContext;
import io.github.racoondog.bidoofmeteor.mixininterface.IClientPlayNetworkHandler;
import io.github.racoondog.bidoofmeteor.util.ApiUtils;
import io.github.racoondog.bidoofmeteor.util.ChatUtils;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class DetectFishyCommand {
    public static int detectFishy(CommandContext<CommandSource> context) {
        if (mc.getNetworkHandler() == null) return 0;
        ChatUtils.info("Detecting fishy...");
        int fishy = 0;
        Set<Map.Entry<UUID, PlayerListEntry>> entries = ((IClientPlayNetworkHandler)mc.getNetworkHandler()).getPlayerListEntries().entrySet();
        for (var entry : entries) {
            String username = entry.getValue().getProfile().getName();
            String givenUuidString = entry.getKey().toString();
            String apiUuidString = ApiUtils.uuidFromName(entry.getValue().getProfile().getName());
            if (apiUuidString == null) {
                ChatUtils.info("Fishy Detected: (highlight)%s (default)is not a valid in-use player name.", username);
                fishy += 1;
            } else {
                givenUuidString = givenUuidString.replace("-", "");
                if (!apiUuidString.equals(givenUuidString)) {
                    ChatUtils.info("Fishy Detected: UUID mismatch in player (highlight)%s(default). Api Expected: (highlight)%s(default), Wrongly Received: (highlight)%s", username, apiUuidString, givenUuidString);
                    fishy += 1;
                }
            }
        }
        if (fishy == 0) {
            ChatUtils.info("No fishy detected.");
        } else {
            ChatUtils.info("Detected (highlight)%s (default)instances of fishy.", fishy);
        }
        return 1;
    }
}
