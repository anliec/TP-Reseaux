package tp.socket;

import tp.protocol.Message;
import tp.server.FileGesture;
import tp.socket.ReceivingThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by nicolas on 14/12/15.
 */
public class SocketServer {

    private static final int DEFAULT_PORT = 4000;

    private LinkedList<ClientDescriptor> clientList = new LinkedList<ClientDescriptor>();
    private ServerSocket listenSocket;
    private LinkedList<Message> history = new LinkedList<Message>();
    private String historyPath;

    public SocketServer(int serverPort, String historyPath) throws IOException{
        history = FileGesture.loadHistory(historyPath);
        this.historyPath = historyPath;
        listenSocket = new ServerSocket(serverPort); //port
    }

    public void run() {
        try {
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from: " + clientSocket.getInetAddress());
                ReceivingThread ct = new ReceivingThread(clientSocket, clientList,history);
                ct.start();
            }
        }
        catch (Exception e){
            System.err.println("Error in SocketServer: " + e);
        }
        FileGesture.saveHistory(historyPath,history);
    }

    public static void main(String args[]){

        int serverPort = DEFAULT_PORT;
        if (args.length != 1) {
            System.out.println("Usage: java SocketServer <Server port>");
            System.out.println("Server port not found default one used: "+DEFAULT_PORT);
            serverPort = DEFAULT_PORT;
        }
        else{
            try{
                serverPort = Integer.parseInt(args[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {
            SocketServer server = new SocketServer(serverPort,"logs/histo.log");
            System.out.println("Server ready...");
            server.run();
        } catch (Exception e) {
            System.err.println("Error in SocketServer: " + e);
        }
    }
}
