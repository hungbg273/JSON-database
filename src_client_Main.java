package client;

import com.google.gson.Gson;
import subclass.request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
//            System.out.println(args[1]);
            Gson gson = new Gson();
            request request;
            System.out.println("Client started!");
            if ("-in".equals(args[0])) {
                String s ="C:\\Users\\Administrator\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\client\\data\\";
                Path paths = Paths.get(s + args[1]);
                System.out.println(paths.toString());
                Scanner scanner = new Scanner(paths);
                request = gson.fromJson(scanner.nextLine(), subclass.request.class);
            } else {
                request = new request(args);
            }
            String s = gson.toJson(request);
            System.out.println("Sent: " + s);
//            int n = scanner.nextInt();
            output.writeUTF(s); // sending message to the server
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}