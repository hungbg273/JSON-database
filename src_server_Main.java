package server;

import subclass.JSON;
import subclass.procedure;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Main {
    private static final int PORT = 34522;
    public static void main(String[] args) throws IOException {
        System.out.println("Server started!");
        ArrayList<JSON> list = new ArrayList<>();
        String filename = "C:\\Users\\Administrator\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json";
        Path path = Paths.get(filename);
        list = procedure.loadListFromJSON(path);
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket socket = server.accept();
                    Thread t = new SubThread(server, list, socket);
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
