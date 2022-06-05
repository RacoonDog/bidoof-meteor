package io.github.racoondog.bidoofmeteor.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AnvilTooltipsImpl {
    public static int costToUses(int cost) {
        return switch (cost) {
            default -> cost;
            case 3 -> 2;
            case 7 -> 3;
            case 15 -> 4;
            case 31 -> 5;
            case 63 -> 6;
        };
    }

    public static boolean isBookEmpty(NbtCompound tag) {
        return !tag.contains("StoredEnchantments");
    }

    public static int getRarity(Enchantment enchantment) {
        int rarity = switch (enchantment.getRarity()) {
            case COMMON -> 1;
            case UNCOMMON -> 2;
            case RARE -> 4;
            case VERY_RARE -> 8;
        };

        return Math.max(1, rarity / 2);
    }

    public static int getBaseCost(NbtList enchantments) {
        int cost = 0;
        for(int i = 0; i < enchantments.size(); ++i) {
            NbtCompound nbtCompound = enchantments.getCompound(i);
            Optional<Enchantment> enchantment = Registry.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound));
            if (enchantment.isEmpty()) continue;
            cost += getRarity(enchantment.get()) *EnchantmentHelper.getLevelFromNbt(nbtCompound);
        }
        return cost;
    }
}
