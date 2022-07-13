package io.github.racoondog.bidoofmeteor;

import meteordevelopment.starscript.value.Value;
import meteordevelopment.starscript.value.ValueMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;

import static meteordevelopment.meteorclient.MeteorClient.mc;
import static meteordevelopment.meteorclient.utils.misc.MeteorStarscript.ss;

@Environment(EnvType.CLIENT)
public class BidoofStarscript {
    private static final Random random = new LocalRandom(System.currentTimeMillis());

    private static double fakeX = genRandom(10000000, 20000000);
    private static double fakeY = genRandom(10, 20);
    private static double fakeZ = genRandom(10000000, 20000000);

    public static void init() {
        ss.set("bidoof", Value.map(new ValueMap()
            .set("_toString", () -> Value.string("boof"))
            .set("version", () -> Value.string(FabricLoader.getInstance().getModContainer("bidoof-meteor").isPresent() ? FabricLoader.getInstance().getModContainer("bidoof-meteor").get().getMetadata().getVersion().getFriendlyString() : "boof"))
            .set("fake_x", () -> Value.number(mc.player != null ? fakeX + mc.player.getX(): fakeX))
            .set("fake_y", () -> Value.number(mc.player != null ? fakeY + mc.player.getY() : fakeY))
            .set("fake_z", () -> Value.number(mc.player != null ? fakeZ + mc.player.getZ() : fakeZ))
        ));
    }

    private static double genRandom(int min, int max) {
        double value = random.nextDouble() * (max - min) + min;
        if (random.nextBoolean()) value = -value;
        return value;
    }
}
