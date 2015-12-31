package tp.socket;

import tp.protocol.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by nicolas on 14/12/15.
 */
public class SocketClient {

    private static final int DEFAULT_PORT = 4000;

    private Socket echoSocket = null;
    private PrintStream socOut = null;
    private BufferedReader stdIn = null;
    private BufferedReader socIn = null;
    private LinkedList<Message> history = new LinkedList<Message>();
    private ClientThread readingThread;
    private String userName;

    public SocketClient(String anUserName,String serverIP, int serverPort, boolean holdOn)
    {
        init(anUserName,serverIP,serverPort);
        if(holdOn) {
            run(holdOn);
        }
    }

    public SocketClient(String anUserName,String serverIP, int serverPort)
    {
        init(anUserName,serverIP,serverPort);
    }

    public void init(String anUserName,String serverIP, int serverPort){
        userName = anUserName;
        try {
            // creation socket ==> connexion
            echoSocket = new Socket(serverIP,serverPort);
            socIn = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            socOut= new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            sendConnectionRequest();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverIP);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ serverIP);
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Connected to server: "+serverIP+" on port: "+serverPort);
        run();
    }

    public void run()
    {
        if(readingThread == null)
        {
            readingThread = new ClientThread(history,socIn);
            readingThread.start();
        }
    }

    public void run(boolean holdOn){
        run();
        if(holdOn){
            while(readingThread.isAlive())
            {
                try{
                    Thread.sleep(800);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            try{
                close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Message message){
        socOut.println(message.toSocketServer());
    }

    public void sendConnectionRequest() {
        socOut.println("CONNECT "+userName);
    }

    public void sendDisconnectionRequest(){
        socOut.println("QUIT");
    }

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
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException {
        int serverPort = DEFAULT_PORT;
        if (args.length > 2) {
            System.out.println("Usage: java SocketClient <Serve IP> <Server port>");
            System.exit(1);
        }
        else if (args.length == 1) {
            System.out.println("Usage: java SocketClient <Serve IP> <Server port>");
            System.out.println("Server port not found default one used: "+DEFAULT_PORT);
            serverPort = DEFAULT_PORT;
        }
        else{
            try{
                serverPort = Integer.parseInt(args[1]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        SocketClient client = new SocketClient("test",args[0], serverPort);

        Message message = new Message("",new Date(),"message de test","all");

        client.sendMessage(message);

        client.run(true);

        client.close();
    }

    public LinkedList<Message> getHistory(){
        return history;
    }

    public String getUserName(){
        return userName;
    }
}
