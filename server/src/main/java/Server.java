import java.io.*;
import java.net.ServerSocket;
import java.util.Properties;


public class Server {

    public static void main(String[] args) {
        String msg = "Сервер запущен";
        System.out.println(msg);
        ServerConnection.logToFile(msg);
        try (ServerSocket serverSocket = new ServerSocket(returnPort())) {
            while (true) {
                try {
                    ServerConnection serverConnection = new ServerConnection(serverSocket.accept());
                    serverConnection.serverStart();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static int returnPort() {
        int port = 0;
        try (FileReader fr = new FileReader("settings.txt")) {
            Properties properties = new Properties();
            properties.load(fr);
            port = Integer.parseInt(properties.getProperty("PORT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;
    }
}


