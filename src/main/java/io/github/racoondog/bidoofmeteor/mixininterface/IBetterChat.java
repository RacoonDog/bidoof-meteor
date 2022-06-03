package io.github.racoondog.bidoofmeteor.mixininterface;

import meteordevelopment.meteorclient.settings.Setting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IBetterChat {
    Setting<Boolean> getCachePlayerHeads();
}
