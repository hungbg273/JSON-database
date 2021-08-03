package client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import server.SubThread;
import subclass.request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 34522;
    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
        )
         {
//            System.out.println(args[1]);
            Gson gson = new Gson();
            JsonElement request;
            System.out.println("Client started!");
            if ("-in".equals(args[0])) {
                String s ="C:\\Users\\Admin\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\client\\data\\";
                Path paths = Paths.get(s + args[1]);
                System.out.println(paths.toString());
                Scanner scanner = new Scanner(paths);
                String req = "";
                while(scanner.hasNext()) {
//                    System.out.println(req);
                    req = req + scanner.nextLine();
                }
                req = req.trim();
//                System.out.println(req);
                request = gson.fromJson(req, JsonElement.class);
            } else {
                String req = "{\"type\":" + "\"" + args[1] + "\"";
                if (args[1] != "exit") {
                    req = req + ", \"key\":" + "\"" + args[3] + "\"";
                    if (args[1] != "get") {
                        req = req + ", \"value\":" + "\"" + args[5] + "\"";
                    }
                }
                req = req + "}";
                System.out.println(req);
                request = gson.fromJson(req, JsonElement.class);
            }
            String s = gson.toJson(request);
            System.out.println("Sent: " + request.toString());
//            int n = scanner.nextInt();
            output.writeUTF(s); // sending message to the server
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Class c1 = Class.forName("SubThread");
            c1.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}