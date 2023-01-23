import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите Ваше имя: ");
        String username = scanner.nextLine();

        Socket socket = new Socket("localhost", returnPort());

        ClientConnection clientConnection = new ClientConnection(socket, username);
        clientConnection.read();
        clientConnection.write();
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
