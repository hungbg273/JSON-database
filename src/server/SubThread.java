package server;

import com.google.gson.*;
import subclass.JSON;
import subclass.Response;
import subclass.procedure;
import subclass.request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SubThread extends Thread{

    Socket socket;
    ServerSocket server;
    JsonObject list;
    String filename = "C:\\Users\\Administrator\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data.json";
    Path path = Paths.get(filename);
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock wl = rwl.writeLock();
    private final Lock rl = rwl.readLock();

    public SubThread(ServerSocket server, JsonObject list, Socket socket) {
//        super();
        this.list = list;
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
//        System.out.println(socket);
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String text = input.readUTF(); // reading a message
            Gson gson = new Gson();
            JsonElement request = gson.fromJson(text, JsonElement.class);
            JsonObject requestObject = request.getAsJsonObject();
            if (requestObject.get("type").getAsString().equals("exit")) {
                Response response = new Response("OK", null, null);
                String msg = gson.toJson(response);
                output.writeUTF(msg);
//                System.exit(0);
                server.close();
            }
            JsonElement json;
            JsonArray listKey = new JsonArray();
            if (requestObject.get("key").isJsonArray()) {
                listKey = requestObject.get("key").getAsJsonArray();
            } else {
                listKey.add(requestObject.get("key"));
            }

            if (requestObject.get("type").getAsString().equals("get")) {
                rl.lock();
                json = procedure.searchOnListToGet(list, listKey);
                if (Objects.equals(null, json)) {
                    Response response = new Response("ERROR", "No such key", null);
                    String msg = gson.toJson(response);
                    output.writeUTF(msg);
                } else {
                    Response response = new Response("OK", null, json);
                    String msg = gson.toJson(response);
                    output.writeUTF(msg);
                }
                rl.unlock();
            } else if (requestObject.get("type").getAsString().equals("set")) {
                wl.lock();
                int n = listKey.size();
                json = procedure.searchOnListToSet(list, listKey);
                JsonElement value = requestObject.get("value");
                if (json.getAsJsonObject().has(listKey.get(n - 1).getAsString())) {
                    json.getAsJsonObject().remove(listKey.get(n - 1).getAsString());
                }
                json.getAsJsonObject().add(listKey.get(n - 1).getAsString(), value);
                Response response = new Response("OK", null, null);
                String msg = gson.toJson(response);
                output.writeUTF(msg);
                wl.unlock();
            } else {
                System.out.println(1);
                wl.lock();
                int n = listKey.size();
                json = procedure.searchOnListToSet(list, listKey);
                if (!json.getAsJsonObject().has(listKey.get(n - 1).getAsString())) {
                    Response response = new Response("ERROR", "No such key", null);
                    String msg = gson.toJson(response);
                    output.writeUTF(msg);
                } else {
                    json.getAsJsonObject().remove(listKey.get(n - 1).getAsString());
                    Response response = new Response("OK", null, null);
                    String msg = gson.toJson(response);
                    output.writeUTF(msg);
                }
                wl.unlock();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}