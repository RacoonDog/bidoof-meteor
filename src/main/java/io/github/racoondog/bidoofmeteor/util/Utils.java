package io.github.racoondog.bidoofmeteor.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class Utils {
    public static <T> List<List<T>> partition(List<T> list, final int size) {
        List<List<T>> parts = new ArrayList<>();
        final int items = list.size();
        for (int i = 0; i < items; i += size) {
            parts.add(new ArrayList<>(
                list.subList(i, Math.min(items, i + size)))
            );
        }
        return parts;
    }
}
