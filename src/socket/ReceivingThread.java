package socket;

import java.io.*;
import java.net.*;

/**
 * Created by nicolas on 14/12/15.
 */
public class ReceivingThread extends Thread {

    private Socket clientSocket;

    ReceivingThread(Socket s) {
        this.clientSocket = s;
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
            while (true) {
                String line = socIn.readLine();
                socOut.println(line);
            }
        }
        catch (Exception e) {
            System.err.println("Error in ReceivingThread: " + e);
        }
    }
}
