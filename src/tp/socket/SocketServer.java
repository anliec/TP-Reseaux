package tp.socket;

import tp.socket.ReceivingThread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by nicolas on 14/12/15.
 */
public class SocketServer {

    public static void main(String args[]){
        ServerSocket listenSocket;

        LinkedList<PrintStream> clientList = new LinkedList<PrintStream>();

        if (args.length != 1) {
            System.out.println("Usage: java SocketServer <Server port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from: " + clientSocket.getInetAddress());
                ReceivingThread ct = new ReceivingThread(clientSocket,clientList);
                ct.start();
            }
        } catch (Exception e) {
            System.err.println("Error in SocketServer: " + e);
        }
    }
}
