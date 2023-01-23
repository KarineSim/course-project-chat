import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ServerConnection {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    public static ArrayList<ServerConnection> connections = new ArrayList<>();
    private String username;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final String LOG_FILE = "file.log";


    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.username = in.readLine();
        connections.add(this);
    }

    public void serverStart() {
        new Thread(() -> {
            try {
                String message = "Зашел новый участник - " + username;
                sendToAllConnections(message);
                logToFile(message);

                String msg;
                while (socket.isConnected()) {
                    msg = in.readLine();
                    if (msg.contains("/exit")) {
                        msg = this + " вышел из чата";
                        sendToAllConnections(msg);
                        logToFile(msg);
                        connections.remove(this);
                        break;
                    }
                    sendToAllConnections(msg);
                    logToFile(msg);
                }
                close(socket, in, out);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void close(Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logToFile(String msg) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write("[" + LocalDateTime.now().format(FORMATTER) + "] " + msg + "\n");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToAllConnections(String msg) {
        for (ServerConnection connection : connections) {
            try {
                connection.out.write(msg + "\n");
                connection.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return username;
    }
}