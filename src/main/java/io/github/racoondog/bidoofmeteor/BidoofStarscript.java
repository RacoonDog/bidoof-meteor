package io.github.racoondog.bidoofmeteor;

import com.google.common.math.BigIntegerMath;
import io.github.racoondog.bidoofmeteor.util.ListUtils;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.starscript.Starscript;
import meteordevelopment.starscript.value.Value;
import meteordevelopment.starscript.value.ValueMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSeed;

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

        ss.set("fma", BidoofStarscript::fma);
        ss.set("log", BidoofStarscript::log);
        ss.set("log10", BidoofStarscript::log10);
        ss.set("sqrt", BidoofStarscript::sqrt);
        ss.set("cbrt", BidoofStarscript::cbrt);
        ss.set("pow", BidoofStarscript::pow);
        ss.set("max", BidoofStarscript::max);
        ss.set("min", BidoofStarscript::min);
        ss.set("clamp", BidoofStarscript::clamp);
        ss.set("average", BidoofStarscript::average);
        ss.set("wrapDeg", BidoofStarscript::wrapDeg);
        ss.set("getLerp", BidoofStarscript::getLerp);
        ss.set("lerp", BidoofStarscript::lerp);
        ss.set("factorial", BidoofStarscript::factorial);

        ValueMap mods = new ValueMap();
        for (var mod : FabricLoader.getInstance().getAllMods()) {
            ModMetadata meta = mod.getMetadata();
            mods.set(meta.getId(), new ValueMap()
                .set("_toString", () -> Value.string(meta.getName()))
                .set("author", () -> Value.string(ListUtils.commaSeparatedList(meta.getAuthors(), Person::getName)))
                .set("version", () -> Value.string(meta.getVersion().getFriendlyString()))
            );
        }
        ss.set("mod", mods
            .set("_toString", () -> Value.number(FabricLoader.getInstance().getAllMods().size()))
            .set("list", () -> Value.string(ListUtils.commaSeparatedList(FabricLoader.getInstance().getAllMods(), mod -> mod.getMetadata().getName())))
        );
    }

    /**
     * Fused multiply add (1 * 2 + 3)
     */
    public static Value fma(Starscript ss, int argCount) {
        if (argCount != 3) ss.error("fma() requres 3 arguments, got %d.", argCount);
        double a = ss.popNumber("Argument 1 of fma() needs to be a number.");
        double b = ss.popNumber("Argument 2 of fma() needs to be a number.");
        double c = ss.popNumber("Argument 3 of fma() needs to be a number.");
        return Value.number(Math.fma(a, b, c));
    }

    public static Value log(Starscript ss, int argCount) {
        if (!(argCount == 1 || argCount == 2)) ss.error("log() requires 1 or 2 argument(s), got %d.", argCount);
        double a = ss.popNumber("Argument 1 to log() needs to be a number.");
        if (argCount == 1) {
            return Value.number(Math.log(a));
        } else {
            double b = ss.popNumber("Argument 2 to log() needs to be a number.");
            return Value.number(Math.log(a) / Math.log(b));
        }
    }

    public static Value log10(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("log10() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to log10() needs to be a number.");
        return Value.number(Math.log10(a));
    }

    public static Value sqrt(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("sqrt() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to sqrt() needs to be a number.");
        return Value.number(Math.sqrt(a));
    }

    public static Value cbrt(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("cbrt() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to cbrt() needs to be a number.");
        return Value.number(Math.cbrt(a));
    }

    public static Value pow(Starscript ss, int argCount) {
        if (argCount != 2) ss.error("pow() requires 2 arguments, got %d.", argCount);
        double a = ss.popNumber("Argument 1 to pow() needs to be a number.");
        double b = ss.popNumber("Argument 2 to pow() needs to be a number.");
        return Value.number(Math.pow(a, b));
    }

    public static Value max(Starscript ss, int argCount) {
        if (argCount != 2) ss.error("max() requires 2 arguments, got %d.", argCount);
        double a = ss.popNumber("Argument 1 to max() needs to be a number.");
        double b = ss.popNumber("Argument 2 to max() needs to be a number.");
        return Value.number(Math.max(a, b));
    }

    public static Value min(Starscript ss, int argCount) {
        if (argCount != 2) ss.error("min() requires 2 arguments, got %d.", argCount);
        double a = ss.popNumber("Argument 1 to min() needs to be a number.");
        double b = ss.popNumber("Argument 2 to min() needs to be a number.");
        return Value.number(Math.min(a, b));
    }

    public static Value clamp(Starscript ss, int argCount) {
        if (argCount != 3) ss.error("clamp() requires 2 arguments, got %d.", argCount);
        double a = ss.popNumber("Argument 1 to clamp() needs to be a number.");
        double b = ss.popNumber("Argument 2 to clamp() needs to be a number.");
        double c = ss.popNumber("Argument 3 to clamp() needs to be a number.");
        return Value.number(MathHelper.clamp(a, b, c));
    }

    public static Value average(Starscript ss, int argCount) {
        if (argCount < 2) ss.error("average() requires at least 2 arguments, got %d.", argCount);
        double sum = 0;
        for (int i = 1; i < argCount + 1; i++) {
            sum += ss.popNumber("All arguments to average() need to be a number.");
        }
        return Value.number(sum / argCount);
    }

    public static Value wrapDeg(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("wrapDeg() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to wrapDeg() needs to be a number.");
        return Value.number(MathHelper.wrapDegrees(a));
    }

    public static Value getLerp(Starscript ss, int argCount) {
        if (argCount != 3) ss.error("getLerp() requires 3 arguments, got %d.", argCount);
        double a = ss.popNumber("Argument 1 to getLerp() needs to be a number.");
        double b = ss.popNumber("Argument 2 to getLerp() needs to be a number.");
        double c = ss.popNumber("Argument 3 to getLerp() needs to be a number.");
        return Value.number(MathHelper.getLerpProgress(a, b, c));
    }

    public static Value lerp(Starscript ss, int argCount) {
        if (argCount != 3) ss.error("lerp() requires 3 arguments, got %d.", argCount);
        double a = ss.popNumber("Argument 1 to lerp() needs to be a number.");
        double b = ss.popNumber("Argument 2 to lerp() needs to be a number.");
        double c = ss.popNumber("Argument 3 to lerp() needs to be a number.");
        return Value.number(MathHelper.lerp(a, b, c));
    }

    public static Value factorial(Starscript ss, int argCount) {
        if (argCount != 1) ss.error("factorial() requires 1 argument, got %d.", argCount);
        double a = ss.popNumber("Argument to factorial() needs to be a number.");
        return Value.number(BigIntegerMath.factorial((int) a).doubleValue());
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] push(T[] list, T item) {
        int size = list.length;
        T[] newList = (T[]) new Object[size + 1];
        System.arraycopy(list, 0, newList, 0, size);
        newList[size + 1] = item;
        return newList;
    }

    private static Object getArg(Value arg) {
        if (arg.isString()) return arg.getString();
        else if (arg.isNumber()) return arg.getNumber();
        else if (arg.isBool()) return arg.getBool();
        else return null;
    }

    private static Value toVal(Object val) {
        if (val instanceof Number num) return Value.number(num.doubleValue());
        else if (val instanceof CharSequence cs) return Value.string(cs.toString());
        else if (val instanceof Boolean bool) return Value.bool(bool);
        else return Value.null_();
    }
    private static double genRandom(int min, int max) {
        double value = random.nextDouble() * (max - min) + min;
        if (random.nextBoolean()) value = -value;
        return value;
    }
}
