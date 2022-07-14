package io.github.racoondog.bidoofmeteor.modules;

import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FishyDetector extends Module {
    public FishyDetector() {
        super(BidoofMeteor.CATEGORY, "fishy-detector", "Detects anomalies in players.");
    }

    public static boolean active() {
        return Modules.get().get(FishyDetector.class).isActive();
    }
}
