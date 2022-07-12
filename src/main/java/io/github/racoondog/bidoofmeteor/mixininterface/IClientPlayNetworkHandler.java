package io.github.racoondog.bidoofmeteor.mixininterface;

import net.minecraft.client.network.PlayerListEntry;

import java.util.Map;
import java.util.UUID;

public interface IClientPlayNetworkHandler {
    Map<UUID, PlayerListEntry> getPlayerListEntries();
}
