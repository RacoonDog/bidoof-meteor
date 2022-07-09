package io.github.racoondog.bidoofmeteor.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import meteordevelopment.meteorclient.utils.network.Http;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ApiUtils {
    private static final String USERNAME_TO_UUID = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String UUID_TO_NAME_HISTORY = "https://api.mojang.com/user/profiles/%s/names";
    private static final String UUID_TO_PROFILE_AND_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

    private static <T> T getJson(String urlString, Type type) {
        return Http.get(urlString).sendJson(type);
    }

    public static String uuidFromName(String name) {
        String url = USERNAME_TO_UUID.formatted(name);
        JsonObject object = getJson(url, JsonObject.class);
        if (object == null) return null;
        return object.get("id").getAsString();
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
