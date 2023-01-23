import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String username;

    public ClientConnection(Socket socket, String username) {
        try {
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        try {
            out.write(username + "\n");
            out.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String msg = scanner.nextLine();
                out.write(username + ": " + msg + "\n");
                out.flush();

                if (msg.contains("/exit")) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        new Thread(() -> {
            String msg;
            while (socket.isConnected()) {
                try {
                    msg = in.readLine();
                    if (msg == null) {
                        break;
                    }
                    System.out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
