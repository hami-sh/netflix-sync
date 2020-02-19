import javax.json.Json;
import java.io.*;
import java.net.Socket;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.*;

public class Peer {
    private String name;
    ServerThread thread;

    public static void main(String[] args) throws Exception {
        System.out.println(args[0]);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(">> Enter name");
        String name = reader.readLine();
        // create the server thread
        ServerThread serverThread = new ServerThread(args[0]);
        serverThread.start();
        Peer user = new Peer();
        user.name = name;
        user.thread = serverThread;
        user.updateListenToPeers(reader, name, serverThread);
    }

    public void updateListenToPeers(BufferedReader reader, String name, ServerThread thread) throws Exception {
        System.out.println("> Connect to ... hostname:port (space separated)");
        System.out.println("(s to skip)");
        String input = reader.readLine();
        String[] inputValues = input.split(" ");

        if (!input.equals("s")) {
            for (int i = 0; i < inputValues.length; i++) {
                String[] address = inputValues[i].split(":");
                Socket socket = null;
                // attempt to connect
                try {
                    socket = new Socket(address[0], Integer.parseInt(address[1]));
                    new PeerThread(socket, thread).start();
                } catch (Exception e) {
                    if (socket != null) {
                        socket.close();
                    } else {
                        System.out.println("INVALID INPUT, moving to next connection");
                    }
                }

            }
        }
        communicate(reader, name, thread);

    }

    public void communicate(BufferedReader reader, String name, ServerThread thread) {
        try {
            System.out.println("> (e:exit, c:change)");
            boolean flag = true;
            while (flag) {
                String message = reader.readLine();
                if (message.equals("e")) {
                    flag = false;
                    break;
                } else if (message.equals("c")) {
                    updateListenToPeers(reader, name, thread);
                } else if (message.equals("") || message.equals(" ")) {
                    // send key to webdriver
                } else {
                    StringWriter writer = new StringWriter();
                    // create and send the message
                    Json.createWriter(writer).writeObject(Json.createObjectBuilder().add(
                            "username", name).add("message", message).build());
                    thread.sendMessage(writer.toString());
                }
            }
            System.exit(0);
        } catch (Exception e) {}
    }
}
