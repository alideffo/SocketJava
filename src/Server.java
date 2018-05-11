import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private ServerSocket serverSocket;
    private Socket socket;
    private String path = "/Users/alideffo/Desktop/FRA UAS/4. Semester/";

    public static void main(String[] args) throws IOException {

        Server server = new Server();
        server.serverSocket = new ServerSocket(6039);


        File file = new File(server.path);
        String[] listFiles = file.list();

        System.out.println("Waiting for Client to connect...");

        server.socket = server.serverSocket.accept();
        System.out.println("Connected to a Client!");

        String messageReceived = null;
        String messageSent = null;


        while(true){
            StringBuilder message = new StringBuilder();
            server.bufferedReader = new BufferedReader(new InputStreamReader(server.socket.getInputStream()));
            server.printWriter = new PrintWriter(server.socket.getOutputStream(), true);

            messageReceived = server.bufferedReader.readLine();
            String[] textToCommand = messageReceived.split(";");

            if(textToCommand[0].equals("directoryListing")){
                message = server.directoryListing(messageReceived);
                server.printWriter.println(message.toString());
            }
            else if(textToCommand[0].equals("addDirectory")){
                server.addDirectory(messageReceived);
            }
            else if(textToCommand[0].equals("deleteDirectory")){
                //server.
            }
            server.printWriter.println("=== Command ended ===");
        }
    }


    /*
    public void closeConnection() throws IOException {
        bufferedReader.close();
        printWriter.close();
        serverSocket.close();
        socket.close();

        System.out.println("Connection successfully closed!");
    }

*/

    public StringBuilder directoryListing(String message){
        StringBuilder directories = new StringBuilder();
        String[] splitMessage = message.split(";");

        String text = splitMessage[1];

        File directory = new File(path + text);
        String[] listFiles = directory.list();
        for(String pos : listFiles){
            directories.append(pos).append("\n");
        }
        return directories;
    }

    public void addDirectory(String message) {
        String[] splitMessage = message.split(";");

        String text = splitMessage[1];

        File directory = new File(path + text);

        if(!directory.exists()){
            directory.mkdir();
            printWriter.println("Directory: " + "\"" + directory.getName() + "\"" + " created!");
        } else{
            printWriter.println("Directory not created");
        }
    }



}
