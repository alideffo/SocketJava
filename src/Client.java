import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String text;
    private Socket socket;
    private Scanner scanner;

    /**
     * The start of our Client program.
     * @param args The String arguments we use
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Client client = new Client();

        client.socket = new Socket("127.0.0.1", 6039);
        System.out.println("Connected to Server!");

        client.scanner = new Scanner(System.in); //For the entries

        String text = "";
        String message = null;

        while(true){

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(client.socket.getOutputStream(),true);

            text = client.scanner.nextLine();
            printWriter.println(text);


            do {
                message = bufferedReader.readLine();
                System.out.println(message);


            }while(!message.equals("=== Command ended ==="));
        }

    }
}
