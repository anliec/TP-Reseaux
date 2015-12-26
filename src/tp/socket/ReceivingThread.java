package tp.socket;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

/**
 * Created by nicolas on 14/12/15.
 */
public class ReceivingThread extends Thread {

    private Socket clientSocket;
    private LinkedList<PrintStream> clientList;

    ReceivingThread(Socket s, LinkedList<PrintStream> refClientList) {
        this.clientSocket = s;
        this.clientList = refClientList;
    }

    /**
     * receives a request from client then sends an echo to the client
     *
     * @param
     **/
    public void run() {
        try {
            BufferedReader socIn = null;
            socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            clientList.add(socOut);
            while (true) {
                String line = socIn.readLine();
                System.out.println(line);
                System.out.println(clientList.size());
                /*for (int i = 0; i < clientList.size(); i++) {
                    clientList.get(i).println(line);
                }*/
                for (PrintStream ps:clientList) {
                    ps.println(line);
                }
            }
        }
        catch (Exception e) {
            System.err.println("Error in ReceivingThread: " + e);
            e.printStackTrace();
        }
    }
}
