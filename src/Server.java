import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private ServerSocket serverSocket;
    private Socket socket;
    private String path = "/Users/alideffo/Desktop/FRA UAS/";

    /**
     * The main function of our program. Where all the command/functions are called
     * @param args The String argument
     * @throws Exception
     */
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


    /**
     * To close the server socket properly.
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        //bufferedReader.close();
        //printWriter.close();
        serverSocket.close();
        //socket.close();

        printWriter.println("Connection successfully closed!");
        System.out.println("Connection successfully closed!");
    }


    /**
     * To list the directory in the given path (Default path: /Users/alideffo/Desktop/FRA UAS/) (or subpath)
     * @param message The message the server received from the client
     * @return A Stringbuilder, which stores all the name of dir. and files from the given path.
     */
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


    /**
     * To add a directory (in a given path. Default Path: /Users/alideffo/Desktop/FRA UAS/)
     * @param message The message the server received from the client
     */
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

    /**
     * Delete a directory (from a given path. Default Path: /Users/alideffo/Desktop/FRA UAS/)
     * @param message The message the server received from the client
     */
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

    /**
     * Add a file in the given Path or Subpath. Default Path: /Users/alideffo/Desktop/FRA UAS/
     * @param message The message the server received from the client
     * @throws IOException
     */
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


    /**
     * Delete file in the given Path or Subpath. Default Path: /Users/alideffo/Desktop/FRA UAS/
     * @param message The message the server received from the client
     */
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


    /**
     * Show the Text in a given file from a given path. Default Path: /Users/alideffo/Desktop/FRA UAS/
     * @param message The message the server received from the client
     * @return A Stringbuilder in which all the Text from the chosen file is stored.
     * @throws Exception
     */
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
