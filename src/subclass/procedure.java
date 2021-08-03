package subclass;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class procedure {

    public static ArrayList<JSON> loadListFromJSON(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            ArrayList<JSON> list = gson.fromJson(reader,
                    new TypeToken<ArrayList<JSON>>(){}.getType());
            if (Objects.equals(list, null)) {
                return new ArrayList<>();
            }
            return list;
        }
    }
    public static JsonObject searchOnListToSet(JsonObject list, JsonArray keys) {
        JsonObject node = list;
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i).getAsString();
            if (!node.has(key)) {
                node.add(key, new JsonObject());
            }
            if (node.get(key).isJsonObject()) {
                node = node.get(key).getAsJsonObject();
            }
        }
        return node;
    }
    public static JsonElement searchOnListToGet(JsonObject list, JsonArray keys) {
        JsonObject node = list;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i).getAsString();
            if (!node.has(key)) {
                return null;
            }
            if (node.get(key).isJsonObject()) {
                node = node.get(key).getAsJsonObject();
            } else {
                if (i == keys.size() - 1) {
                    return node.get(key).getAsJsonPrimitive();
                }
            }
        }
        return node;
    }
    public static void writeJSONToFile(JsonElement list, Path path) {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            gson.toJson(list, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
