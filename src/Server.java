import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private ServerSocket serverSocket;
    private Socket socket;
    private String path = "/Users/alideffo/Desktop/FRA UAS/";

    public static void main(String[] args) throws Exception {

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
                server.deleteDirectory(messageReceived);
            }
            else if(textToCommand[0].equals("addFile")){
                server.addFile(messageReceived);
            }
            else if(textToCommand[0].equals("deleteFile")){
                server.deleteFile(messageReceived);
            }
            else if(textToCommand[0].equals("getFile")){
                message = server.getFile(messageReceived);
                server.printWriter.println(message.toString());
            }
            else if(textToCommand[0].equals("closeProgram")){
                server.closeConnection();
            }

            if(!textToCommand[0].equals("closeProgram")){
                server.printWriter.println("=== Command ended ===");
            }

        }
    }



    public void closeConnection() throws IOException {
        //bufferedReader.close();
        //printWriter.close();
        serverSocket.close();
        //socket.close();

        printWriter.println("Connection successfully closed!");
        System.out.println("Connection successfully closed!");
    }



    public StringBuilder directoryListing(String message){
        StringBuilder directories = new StringBuilder();
        String[] splitMessage = message.split(";");
        File directory;

        String text = splitMessage[1];
        if(text.equals("")){
            directory = new File(path);
        }
        else{
            directory = new File(path + text);
        }

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

    public void deleteDirectory(String message) {
        String[] splitMessage = message.split(";");

        String text = splitMessage[1];

        File directory = new File(path + text);

        if(directory.exists()){
            for(File file : directory.listFiles()){
                file.delete();
            }
            directory.delete();
            printWriter.println("Directory " + "\"" + directory.getName() + "\"" + " deleted!");
        } else{
            printWriter.println("Directory does not exists");
        }
    }

    public void addFile(String message) throws IOException {
        String[] splitMessage = message.split(";");

        String fileName = splitMessage[1];
        String text = splitMessage[2];

        File file = new File(path, fileName + ".txt");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                printWriter.println("File already exists");
            }
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

        bufferedWriter.write(text);
        bufferedWriter.close();

        printWriter.println("File created and text in it!");
    }



    public void deleteFile(String message) {
        String[] splitMessage = message.split(";");

        String text = splitMessage[1];

        File file = new File(path + text);

        if(file.exists()){
            file.delete();
            printWriter.println("File deleted!!");
        } else{
            printWriter.println("Could not find file!");
        }
    }



    private StringBuilder getFile(String message) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();


        String[] splitMessage = message.split(";");

        String text = splitMessage[1];

        FileReader fileReader = new FileReader(path+text);

        int pos;

        while((pos = fileReader.read()) != -1){
            stringBuilder.append((char) pos);
        }

        fileReader.close();

        return stringBuilder;
    }

}
