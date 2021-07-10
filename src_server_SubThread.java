package server;

import com.google.gson.Gson;
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
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SubThread extends Thread{

    Socket socket;
    ServerSocket server;
    ArrayList<JSON> list;
    String filename = "C:\\Users\\Administrator\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data.json";
    Path path = Paths.get(filename);
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock wl = rwl.writeLock();
    private final Lock rl = rwl.readLock();

    public SubThread(ServerSocket server, ArrayList<JSON> list, Socket socket) {
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
            request request = gson.fromJson(text, subclass.request.class);
            if (request.getType().equals("exit")) {
                Response response = new Response("OK", null, null);
                String msg = gson.toJson(response);
                output.writeUTF(msg);
//                System.exit(0);
                server.close();
            }
            JSON json = new JSON();
            if (request.getType().equals("get")) {
                rl.lock();
                json = procedure.searchOnList(list, request.getKey());
                if (Objects.equals(null, json)) {
                    Response response = new Response("ERROR", "No such key", null);
                    String msg = gson.toJson(response);
                    output.writeUTF(msg);
                } else {
                    Response response = new Response("OK", null, json.getValue());
                    String msg = gson.toJson(response);
                    output.writeUTF(msg);
                }
                rl.unlock();
            } else if (request.getType().equals("set")) {
                wl.lock();
                json = procedure.searchOnList(list, request.getKey());
                if (Objects.equals(json, null)) {
                    json = new JSON(request.getKey(), request.getValue());
                    list.add(json);
                } else {
                    json.setValue(request.getValue());
                }
                Response response = new Response("OK", null, null);
                String msg = gson.toJson(response);
                output.writeUTF(msg);
                wl.unlock();
            } else {
                wl.lock();
                json = procedure.searchOnList(list, request.getKey());
                if (Objects.equals(null, json)) {
                    Response response = new Response("ERROR", "No such key", null);
                    String msg = gson.toJson(response);
                    output.writeUTF(msg);
                } else {
                    list.remove(json);
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
