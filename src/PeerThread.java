import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import java.time.LocalTime;
import javax.json.Json;
import javax.json.JsonObject;

public class PeerThread extends Thread{
    /**
     * Represents a connection
     */
    private BufferedReader reader;
    private ServerThread thread;

    public PeerThread(Socket socket, ServerThread thread) throws IOException {
        this.thread = thread;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {
        boolean flag = true;
        while (flag) {
            try {
                JsonObject jsonObject = Json.createReader(reader).readObject();
                if (jsonObject.containsKey("username")) {
                    // print msg to screen
                    LocalTime time = java.time.LocalTime.now();
                    System.out.println("[" + jsonObject.getString("username") + "@" + time + "]: " + jsonObject.getString("message"));

                    // give received indication by echoing a message back
                    StringWriter writer = new StringWriter();
                    Json.createWriter(writer).writeObject(Json.createObjectBuilder().add("time", time.toString()).build());
                    this.thread.sendMessage(writer.toString());

                } else if (jsonObject.containsKey("time")) {
                    // get received info
                    System.out.println("PROCESSED @ " + jsonObject.getString("time"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
                interrupt();
            }
        }
    }
}
