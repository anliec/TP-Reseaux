package tp.socket;

import tp.protocol.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * client designed to send and receive message to/from a server through socket connection
 */
public class SocketClient {

    private Socket echoSocket = null;
    private PrintStream socOut = null;
    private BufferedReader stdIn = null;
    private BufferedReader socIn = null;
    private LinkedList<Message> history = new LinkedList<Message>();
    private ClientThread readingThread;
    private String userName;

    /**
     * main constructor
     * @param anUserName pseudo of the currant client
     * @param serverIP ip of the server to connect
     * @param serverPort port of the server to connect
     **/
    public SocketClient(String anUserName,String serverIP, int serverPort) throws Exception {
        init(anUserName,serverIP,serverPort);
    }

    /**
     * initialisation procedure. extension of the constructor (comon part of different constructor)
     */
    private void init(String anUserName,String serverIP, int serverPort) throws Exception {
        userName = anUserName;

        echoSocket = new Socket(serverIP,serverPort);
        socIn = new BufferedReader( new InputStreamReader(echoSocket.getInputStream()));
        socOut= new PrintStream(echoSocket.getOutputStream());
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        sendConnectionRequest();
        run();
    }

    /**
     * begin the reception of message from the server
     */
    public void run() {
        if(readingThread == null)
        {
            readingThread = new ClientThread(history,socIn);
            readingThread.start();
        }
    }

    /**
     * send a message to the server
     * @param message message to send
     */
    public void sendMessage(Message message){
        socOut.println(message.toSocketServer());
    }

    /**
     * send a connection request to the server
     */
    public void sendConnectionRequest() {
        socOut.println("CONNECT "+userName);
    }

    /**
     * send a disconnection request to the server
     */
    public void sendDisconnectionRequest(){
        socOut.println("QUIT");
    }

    /**
     * do every things to do on exiting
     * @throws IOException
     */
    public void close() throws IOException
    {
        readingThread.close();
        sendDisconnectionRequest();
        socOut.close();
        socIn.close();
        stdIn.close();
        echoSocket.close();
    }

    /**
     * @return a reference of the client history
     */
    public LinkedList<Message> getHistory(){
        return history;
    }

    /**
     * @return the name (or pseudo) of the currant user
     */
    public String getUserName(){
        return userName;
    }
}
