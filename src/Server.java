import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private ServerSocket serverSocket;
    private Socket socket;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.serverSocket = new ServerSocket(6942);
        String text;
        System.out.println("Waiting for Client to connect...");

        server.socket = server.serverSocket.accept();
        System.out.println("Connected to Client!");

        server.bufferedReader = new BufferedReader(new InputStreamReader(server.socket.getInputStream()));
        server.printWriter = new PrintWriter(server.socket.getOutputStream(), true);

        while((text = server.bufferedReader.readLine()) != null){
            server.printWriter.println(text.toUpperCase());
        }

        server.closeConnection();
    }

    private void closeConnection() throws IOException {
        bufferedReader.close();
        printWriter.close();
        serverSocket.close();
        socket.close();

        System.out.println("Connection successfully closed!");
    }
}
