package io.github.racoondog.bidoofmeteor.impl;

import io.github.racoondog.bidoofmeteor.mixininterface.IBetterChat;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.BetterChat;
import net.minecraft.client.network.PlayerListEntry;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerHeadCacheImpl {
    public static final LinkedHashMap<String, PlayerListEntry> DYNAMIC_PLAYER_HEAD_CACHE = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(final Map.Entry eldest) {
            return size() > 20; //max size to prevent high memory usage
        }
    };

    public static boolean canCachePlayerHeads() {
        BetterChat module = Modules.get().get(BetterChat.class);
        return module.isActive() && ((IBetterChat) module).getCachePlayerHeads().get();
    }
}
