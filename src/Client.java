import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    /**
     * The start of our Client program.
     * @param args The String arguments we use
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        String text;
        Socket socket = new Socket("192.168.0.157", 6942);
        System.out.println("Connected to Server!");

        Scanner scanner = new Scanner(System.in); //For the entries

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        while(true){
            printWriter.println(scanner.nextLine());
            System.out.println(bufferedReader.readLine());
        }

    }
}
