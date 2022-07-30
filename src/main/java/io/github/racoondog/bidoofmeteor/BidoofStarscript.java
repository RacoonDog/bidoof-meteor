package io.github.racoondog.bidoofmeteor;

import io.github.racoondog.bidoofmeteor.util.Constants;
import io.github.racoondog.bidoofmeteor.util.ListUtils;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.starscript.Starscript;
import meteordevelopment.starscript.value.Value;
import meteordevelopment.starscript.value.ValueMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSeed;

import java.util.stream.Collectors;

import static meteordevelopment.meteorclient.MeteorClient.mc;
import static meteordevelopment.meteorclient.utils.misc.MeteorStarscript.ss;

@Environment(EnvType.CLIENT)
public class BidoofStarscript {
    private static final Random random = new LocalRandom(RandomSeed.getSeed());

    public static double fakeX = genRandom(10000000, 20000000);
    public static double fakeY = genRandom(10, 20);
    public static double fakeZ = genRandom(10000000, 20000000);

    @PreInit
    public static void init() {
        ss.set("bidoof", Value.map(new ValueMap()
            .set("_toString", () -> Value.string("boof"))
            .set("version", () -> Value.string(FabricLoader.getInstance().getModContainer("bidoof-meteor").isPresent() ? FabricLoader.getInstance().getModContainer("bidoof-meteor").get().getMetadata().getVersion().getFriendlyString() : "boof"))
            .set("fake_x", () -> Value.number(mc.player != null ? fakeX + mc.player.getX(): fakeX))
            .set("fake_y", () -> Value.number(mc.player != null ? fakeY + mc.player.getY() : fakeY))
            .set("fake_z", () -> Value.number(mc.player != null ? fakeZ + mc.player.getZ() : fakeZ))
        ));

        ss.set("fancy", BidoofStarscript::fancy);

        ValueMap mods = new ValueMap();
        for (var mod : FabricLoader.getInstance().getAllMods()) {
            ModMetadata meta = mod.getMetadata();
            String id = meta.getId();
            String name = meta.getName();
            String author = ListUtils.commaSeparatedList(meta.getAuthors(), Person::getName);
            String version = meta.getVersion().getFriendlyString();
            mods.set(id, new ValueMap()
                .set("_toString", () -> Value.string(name))
                .set("author", () -> Value.string(author))
                .set("version", () -> Value.string(version))
            );
        }
        ss.set("mod", mods
            .set("_toString", () -> Value.number(FabricLoader.getInstance().getAllMods().size()))
            .set("list", () -> Value.string(ListUtils.commaSeparatedList(FabricLoader.getInstance().getAllMods(), mod -> mod.getMetadata().getName())))
        );
    }

    public static Value fancy(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("fancy() requires 1 argument, got %d.", argCount);
        String a = ss.popString("Argument to fancy() needs to be a string.");
        StringBuilder sb = new StringBuilder();
        for (char ch : a.toCharArray()) {
            sb.append(Constants.FANCY.getOrDefault(ch, ch));
        }
        return Value.string(sb.toString());
    }

    private static double genRandom(int min, int max) {
        double value = random.nextDouble() * (max - min) + min;
        if (random.nextBoolean()) value = -value;
        return value;
    }
}
