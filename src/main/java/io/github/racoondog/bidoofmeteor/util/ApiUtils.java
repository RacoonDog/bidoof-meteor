package io.github.racoondog.bidoofmeteor.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.racoondog.bidoofmeteor.BidoofMeteor;
import meteordevelopment.meteorclient.utils.network.Http;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public final class ApiUtils {
    private static final String USERNAME_TO_UUID = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String USERNAMES_TO_UUIDS = "https://api.mojang.com/profiles/minecraft";
    private static final String UUID_TO_NAME_HISTORY = "https://api.mojang.com/user/profiles/%s/names";
    private static final String UUID_TO_PROFILE_AND_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

    private static <T> @Nullable T getJson(String urlString, Type type) {
        if (mc.isOnThread()) BidoofMeteor.LOG.warn("Sending API requests while on the main thread is stupid and you should feel bad.");
        return Http.get(urlString).sendJson(type);
    }

    private static <T> @Nullable T postJson(String urlString, Type type, Object body) {
        if (mc.isOnThread()) BidoofMeteor.LOG.warn("Sending API requests while on the main thread is stupid and you should feel bad.");
        return Http.post(urlString).bodyJson(body).sendJson(type);
    }

    public static @Nullable String uuidFromName(String name) {
        String url = USERNAME_TO_UUID.formatted(name);
        JsonObject object = getJson(url, JsonObject.class);
        if (object == null) return null;
        return object.get("id").getAsString();
    }

    private static @NotNull List<Pair<String, String>> unsafeUuidsFromNames(List<String> names) {
        JsonArray nameArray = new JsonArray();
        names.forEach(nameArray::add);
        JsonArray uuidArray = postJson(USERNAMES_TO_UUIDS, JsonArray.class, nameArray);
        if (uuidArray == null) return new ArrayList<>();
        List<Pair<String, String>> output = new ArrayList<>();
        uuidArray.iterator().forEachRemaining(jsonElement -> {
            JsonObject object = jsonElement.getAsJsonObject();
            output.add(new Pair<>(object.get("name").getAsString(), object.get("id").getAsString()));
        });
        return output;
    }

    public static @NotNull List<Pair<String, String>> uuidsFromNames(List<String> names) {
        List<Pair<String, String>> out = new ArrayList<>();
        if (names.size() == 0) return out;
        else if (names.size() == 1) out.add(new Pair<>(names.get(0), uuidFromName(names.get(0))));
        else if (names.size() <= 10) return unsafeUuidsFromNames(names);
        else ListUtils.partition(names, 10).forEach((list) -> out.addAll(unsafeUuidsFromNames(list)));
        return out;
    }

    public static @Nullable Map<String, Long> nameHistoryFromUuid(String uuid) {
        String url = UUID_TO_NAME_HISTORY.formatted(uuid);
        JsonArray array = getJson(url, JsonArray.class);
        if (array == null) return null;
        Map<String, Long> output = new LinkedHashMap<>();
        array.iterator().forEachRemaining(jsonElement -> {
            JsonObject object = jsonElement.getAsJsonObject();
            output.put(object.get("name").getAsString(), object.has("changedToAt") ? object.get("changedToAt").getAsLong() : 0);
        });
        return output;
    }

    public static @Nullable JsonObject profileFromUuid(String uuid) {
        String url = UUID_TO_PROFILE_AND_SKIN.formatted(uuid);
        return getJson(url, JsonObject.class);
    }
}
