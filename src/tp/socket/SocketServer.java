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
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * server designed to send and receive message to/from clients through socket connection
 */
public class SocketServer extends Thread {

    private static final int DEFAULT_PORT = 4000;

    private LinkedList<ClientDescriptor> clientList = new LinkedList<ClientDescriptor>();
    private ServerSocket listenSocket;
    private LinkedList<Message> history = new LinkedList<Message>();
    private String historyPath;

    private boolean loop=true;

    /**
     * main constructor
     * @param serverPort the port the server listen to
     * @param historyPath where the server can found an history file (if the file don't exist a history file will
     *                    be created when exiting)
     * @throws IOException
     */
    public SocketServer(int serverPort, String historyPath) throws IOException{
        this.historyPath = historyPath;
        history = FileGesture.loadHistory(this.historyPath);
        listenSocket = new ServerSocket(serverPort); //port
        listenSocket.setSoTimeout(100);
    }

    /**
     * thread main loop: handle the connection of new client by creating specific thread.
     */
    public void run() {
        try {
            while (loop) {
                try {
                    Socket clientSocket = listenSocket.accept();
                    ReceivingThread ct = new ReceivingThread(clientSocket, clientList, history);
                    ct.start();
                }catch (SocketTimeoutException e){
                }
            }
        }
        catch (Exception e){
            System.err.println("Error in SocketServer: " + e);
        }
        FileGesture.saveHistory(historyPath,history);
    }

    /**
     * end the thread infinite loop
     */
    public void close(){
        loop=false;
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
            server.start();
            Scanner scanner = new Scanner(System.in);
            String command;
            do{
                command = scanner.nextLine();
            }while (!command.equals("exit"));
            server.close();
            //server.stop();
        } catch (Exception e) {
            System.err.println("Error in SocketServer: " + e);
        }

    }
}
