import javax.json.Json;
import java.io.*;
import java.net.Socket;

public class Peer {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(">> Enter name");
        String setupValues = reader.readLine();

        // create the server thread
        ServerThread serverThread = new ServerThread("0");
        serverThread.start();
        new Peer().updateListenToPeers(reader, setupValues, serverThread);
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
                    new PeerThread(socket).start();
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
            System.out.println("> CONNECTED (e:exit, c:change)");
            boolean flag = true;
            while (flag) {
                String message = reader.readLine();
                if (message.equals("e")) {
                    flag = false;
                    break;
                } else if (message.equals("c")) {
                    updateListenToPeers(reader, name, thread);
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
