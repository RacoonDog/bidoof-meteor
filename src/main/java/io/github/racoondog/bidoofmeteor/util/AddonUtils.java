package io.github.racoondog.bidoofmeteor.util;

import meteordevelopment.meteorclient.addons.AddonManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

@Environment(EnvType.CLIENT)
public final class AddonUtils {
    private static final List<String> ADDON_NAMES = AddonManager.ADDONS.stream().map(addon -> addon.name).toList();

    public static boolean areAddonsPresent(String... addons) {
        for (var str : addons) {
            if (ADDON_NAMES.contains(str)) return true;
        }
        return false;
    }
}
