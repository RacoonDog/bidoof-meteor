package io.github.racoondog.bidoofmeteor.mixininterface;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IStringReader {
    String readUsername();
    String readUUID();
}
