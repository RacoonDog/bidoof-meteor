package io.github.racoondog.bidoofmeteor.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AnvilTooltipsImpl {
    public static int costToUses(int cost) {
        return log2(cost + 1);
    }

    public static boolean isBookEmpty(NbtCompound tag) {
        return !tag.contains("StoredEnchantments");
    }

    private static int getRarity(Enchantment enchantment) {
        return switch (enchantment.getRarity()) {
            case COMMON, UNCOMMON -> 1;
            case RARE -> 2;
            case VERY_RARE -> 4;
        };
    }

    public static int getBaseCost(NbtList enchantments) {
        int cost = 0;
        for(int i = 0; i < enchantments.size(); ++i) {
            NbtCompound nbtCompound = enchantments.getCompound(i);
            Optional<Enchantment> enchantment = Registry.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound));
            if (enchantment.isEmpty()) continue;
            cost += getRarity(enchantment.get()) * EnchantmentHelper.getLevelFromNbt(nbtCompound);
        }
        return cost;
    }

    private static boolean isOptimized(NbtList enchantments, int anvilUses, boolean isBook) {
        if (anvilUses == 0) return true;
        int enchants = enchantments.size();
        int toReturn = (int) Math.pow(2, anvilUses - 1);
        return isBook ? enchants - 1 >= toReturn: enchants >= toReturn;
    }

    public static Formatting getFormatting(NbtList enchantments, int anvilUses, boolean isBook) {
        if (isOptimized(enchantments, anvilUses, isBook)) return Formatting.GREEN;
        else if (isOptimized(enchantments, anvilUses - 1, isBook)) return Formatting.YELLOW;
        return Formatting.RED;
    }

    private static int log2(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }
}
