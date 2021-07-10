package subclass;

import com.google.gson.Gson;
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
    public static JSON searchOnList(ArrayList<JSON> list, String key) {
        if (list.size() > 0) {
            for (JSON json : list) {
                if (Objects.equals(json.getKey(), key)) {
                    return json;
                }
            }
        }
        return null;
    }
    public static void writeJSONToFile(ArrayList<JSON> list, Path path) {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            gson.toJson(list, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
