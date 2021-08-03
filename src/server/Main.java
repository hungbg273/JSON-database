package server;

import com.google.gson.*;
import subclass.JSON;
import subclass.procedure;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    private static final int PORT = 34522;
    public static void main(String[] args) throws IOException {
        System.out.println("Server started!");
        JsonElement list;
        String filename = "C:\\Users\\Admin\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json";
        Path path = Paths.get(filename);
        String jsonFile = "";
        Scanner scanner = new Scanner(path);
        while (scanner.hasNext()) {
            jsonFile = jsonFile + scanner.nextLine();
        }
//        System.out.println(jsonFile);
        Gson gson = new Gson();
        list = gson.fromJson(jsonFile, JsonElement.class);
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket socket = server.accept();
                    Thread t = new SubThread(server, list.getAsJsonObject(), socket);
                    t.start();
                } finally {
                    if (server.isClosed()) {
                        System.out.println(list);
                        procedure.writeJSONToFile(list, path);
                        System.exit(0);
                    }
                }
            }
        }

    }
}