import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadThread extends Thread {
    // spwaned by serverthreads
    private ServerThread thread;
    private Socket socket;
    private PrintWriter writer;

    public ServerThreadThread(Socket socket, ServerThread thread) {
        this.thread = thread;
        this.socket = socket;
    }

    public PrintWriter getPrintWriter() {
        return writer;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                thread.sendMessage(reader.readLine());
            }
        } catch (Exception e) {
            thread.getServerThreadThreads().remove(this);
        }
    }
}
