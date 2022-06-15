package io.github.racoondog.bidoofmeteor.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiUtils {
    private static final String USERNAME_TO_UUID = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String UUID_TO_NAME_HISTORY = "https://api.mojang.com/user/profiles/%s/names";
    private static final String UUID_TO_PROFILE_AND_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

    private static JsonElement getJson(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            result.append(line);
        }
        return JsonParser.parseString(result.toString());
    }

    public static String uuidFromName(String name) throws IOException {
        String url = USERNAME_TO_UUID.formatted(name);
        JsonElement json = getJson(url);
        if (json == null) return null;
        JsonObject object = json.getAsJsonObject();
        return object.get("id").getAsString();
    }

    public static Map<String, Long> nameHistoryFromUuid(String uuid) throws IOException {
        String url = UUID_TO_NAME_HISTORY.formatted(uuid);
        JsonElement json = getJson(url);
        if (json == null) return null;
        JsonArray array = json.getAsJsonArray();
        Iterator<JsonElement> iterator = array.iterator();
        Map<String, Long> output = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            JsonObject object = iterator.next().getAsJsonObject();
            String name = object.get("name").getAsString();
            long time = object.has("changedToAt") ? object.get("changedToAt").getAsLong() : 0;
            output.put(name, time);
        }
        return output;
    }

    public static JsonObject profileFromUuid(String uuid) throws IOException {
        String url = UUID_TO_PROFILE_AND_SKIN.formatted(uuid);
        JsonElement json = getJson(url);
        if (json == null) return null;
        return json.getAsJsonObject();
    }
}
