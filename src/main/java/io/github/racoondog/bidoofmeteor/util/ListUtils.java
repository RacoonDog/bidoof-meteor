package io.github.racoondog.bidoofmeteor.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class ListUtils {
    public static <T> @NotNull List<List<T>> partition(List<T> list, final int size) {
        List<List<T>> parts = new ArrayList<>();
        final int items = list.size();
        for (int i = 0; i < items; i += size) {
            parts.add(new ArrayList<>(
                list.subList(i, Math.min(items, i + size)))
            );
        }
        return parts;
    }

    public static <T> String commaSeparatedList(Collection<T> list) {
        return list.stream().map(Objects::toString).collect(Collectors.joining(", "));
    }

    public static <T> String commaSeparatedList(Collection<T> list, Function<? super T, String> mapper) {
        return list.stream().map(mapper).collect(Collectors.joining(", "));
    }
}
