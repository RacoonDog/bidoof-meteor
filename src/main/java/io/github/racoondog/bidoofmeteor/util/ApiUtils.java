package io.github.racoondog.bidoofmeteor.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import meteordevelopment.meteorclient.utils.network.Http;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Pair;

import java.lang.reflect.Type;
import java.util.*;

@Environment(EnvType.CLIENT)
public class ApiUtils {
    private static final String USERNAME_TO_UUID = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String USERNAMES_TO_UUIDS = "https://api.mojang.com/profiles/minecraft";
    private static final String UUID_TO_NAME_HISTORY = "https://api.mojang.com/user/profiles/%s/names";
    private static final String UUID_TO_PROFILE_AND_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

    private static <T> T getJson(String urlString, Type type) {
        return Http.get(urlString).sendJson(type);
    }

    private static <T> T postJson(String urlString, Type type, Object body) {
        return Http.post(urlString).bodyJson(body).sendJson(type);
    }

    public static String uuidFromName(String name) {
        String url = USERNAME_TO_UUID.formatted(name);
        JsonObject object = getJson(url, JsonObject.class);
        if (object == null) return null;
        return object.get("id").getAsString();
    }

    private static List<Pair<String, String>> unsafeUuidsFromNames(List<String> names) {
        JsonArray postArray = new JsonArray();
        names.forEach(postArray::add);
        JsonArray array = postJson(USERNAMES_TO_UUIDS, JsonArray.class, postArray);
        if (array == null) return new ArrayList<>();
        List<Pair<String, String>> output = new ArrayList<>();
        array.iterator().forEachRemaining(jsonElement -> {
            JsonObject object = jsonElement.getAsJsonObject();
            output.add(new Pair<>(object.get("name").getAsString(), object.get("id").getAsString()));
        });
        return output;
    }

    public static List<Pair<String, String>> uuidsFromNames(List<String> names) {
        List<Pair<String, String>> out = new ArrayList<>();
        if (names.size() == 0) return out;
        else if (names.size() == 1) out.add(new Pair<>(names.get(0), uuidFromName(names.get(0))));
        else if (names.size() <= 10) return unsafeUuidsFromNames(names);
        else Utils.partition(names, 10).forEach((list) -> out.addAll(unsafeUuidsFromNames(list)));
        return out;
    }

    public static Map<String, Long> nameHistoryFromUuid(String uuid) {
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

    public static JsonObject profileFromUuid(String uuid) {
        String url = UUID_TO_PROFILE_AND_SKIN.formatted(uuid);
        return getJson(url, JsonObject.class);
    }
}
