package tp.socket;

import tp.protocol.Message;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Date;

/**
 * Created by nicolas on 14/12/15.
 */
public class ReceivingThread extends Thread {

    private static final int UNKNOWN_TYPE_MESSAGE = 0;
    private static final int CONNECTION_MESSAGE = 1;
    private static final int DISCONNECTION_MESSAGE = 2;
    private static final int TEXT_MESSAGE = 3;

    private LinkedList<Message> history;
    private Socket clientSocket;
    private LinkedList<ClientDescriptor> clientList;
    private String currantClientPseudo;
    private BufferedReader socIn = null;

    ReceivingThread(Socket s, LinkedList<ClientDescriptor> refClientList, LinkedList<Message> aHistory) {
        this.clientSocket = s;
        this.clientList = refClientList;
        this.history = aHistory;
    }

    /**
     * receives a request from client then sends an echo to the client
     *
     * @param
     **/
    public void run() {
        try {
            socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            waitForConnection();
            clientList.add(new ClientDescriptor(currantClientPseudo,socOut) );
            String line;
            do {
                line = socIn.readLine();
            }while (lineHandler(line) != DISCONNECTION_MESSAGE);
        }
        catch (Exception e) {
            System.err.println("Error in ReceivingThread: " + e);
            e.printStackTrace();
        }
    }

    /**
     * wait and handle a connection to the currant socket
     * @throws Exception
     */
    public void waitForConnection() throws Exception{
        String line;
        do {
            line = socIn.readLine();
        }while(getInstructionType(line) != CONNECTION_MESSAGE);
        connectionHandler(line);
    }

    /**
     * @param line a socket protocol text line
     * @return the id of the instruction received
     */
    public int getInstructionType(String line){
        switch (line.charAt(0))
        {
            case 'C': //connection
                return CONNECTION_MESSAGE;
            case 'S': //message
                return TEXT_MESSAGE;
            case 'Q': //disconnection
                return DISCONNECTION_MESSAGE;
            default:
                return UNKNOWN_TYPE_MESSAGE;
        }
    }

    /**
     * handle the line, making all that must be done
     * @param line a socket protocol text line
     * @return the id of the instruction received
     */
    public int lineHandler(String line){
        int instructionType = getInstructionType(line);
        switch (instructionType)
        {
            case CONNECTION_MESSAGE: //connection
                connectionHandler(line);
                break;
            case TEXT_MESSAGE: //message
                sendToHandler(line);
                break;
            case DISCONNECTION_MESSAGE: //disconnection
                disconnectionHandler();
                break;
            default:
                break;
        }
        return instructionType;
    }

    public void sendToHandler(String line){
        String pseudo, text;
        int pseudoBeginning, pseudoEnd, textBeginning;
        pseudoBeginning = line.indexOf(' ')+1;
        pseudoEnd = line.indexOf(' ', pseudoBeginning+1);
        textBeginning = pseudoEnd+9;
        pseudo = line.substring(pseudoBeginning,pseudoEnd);
        text = line.substring(textBeginning);
        sendMessageTo(new Message(currantClientPseudo,new Date(),text,pseudo));
    }

    public void sendMessageTo(Message message){
        if(message.getPseudoClientReceiver().equals("all"))
        {
            for (ClientDescriptor cd:clientList) {
                cd.getStream().println(message.toSocketClient());
            }
        }
        else{
            for (ClientDescriptor cd:clientList) {
                if(cd.getPseudo().equals(message.getPseudoClientReceiver())) {
                    cd.getStream().println(message.toSocketClient());
                }
            }
        }
    }

    public void connectionHandler(String line){
        currantClientPseudo = line.substring(8);

        //TODO forgive same pseudo ?

        //send connection message to every one
        for (ClientDescriptor cd:clientList) {
            cd.getStream().println("SIGNIN "+currantClientPseudo);
        }
        //send the last 10 message to the client:
        try{
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            for (int i=history.size()-1 ; i>history.size()-11 && i>0 ; i--){
                socOut.println(history.get(i).toSocketClient());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disconnectionHandler() {
        for (int i = 0; i < clientList.size(); i++) {
            if(clientList.get(i).getPseudo().equals(currantClientPseudo)) {
                clientList.remove(i);
                i--;
            }
        }
        //send disconnection message to every one
        for (ClientDescriptor cd:clientList) {
            cd.getStream().println("SIGNOUT "+currantClientPseudo);
        }
    }
}
