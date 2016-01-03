package tp.socket;

import tp.protocol.Message;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Date;

/**
 * Server thread, design to handle the message send by one client
 */
public class ReceivingThread extends Thread {

    private static final int UNKNOWN_TYPE_MESSAGE = 0;
    private static final int CONNECTION_MESSAGE = 1;
    private static final int DISCONNECTION_MESSAGE = 2;
    private static final int TEXT_MESSAGE = 3;

    private static final int HISTORY_SIZE_SEND=10;

    private LinkedList<Message> history;
    private Socket clientSocket;
    private LinkedList<ClientDescriptor> clientList;
    private String currantClientPseudo;
    private BufferedReader socIn = null;
    private PrintStream socOut = null;

    /**
     * main constructor
     * @param s socket associated to the client
     * @param refClientList a reference to the currently connected clients
     * @param aHistory a reference to the history of the messages on this server (receved message will
     *                 be added to it)
     */
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
            socOut = new PrintStream(clientSocket.getOutputStream());
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
        if(line == null) {
            return UNKNOWN_TYPE_MESSAGE;
        }
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

    /**
     * Handle the SEND [...] TO [...] instruction
     * @param line line got from the client
     */
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

    /**
     * send message to the right client (acording to the info on client)
     * @param message a message to send
     */
    public void sendMessageTo(Message message){
        //add message to history:
        history.add(message);
        //send the message to the right people:
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
            if(!message.getPseudoClientReceiver().equals(currantClientPseudo)){
                socOut.println(message.toSocketClient());
            }
        }
    }

    /**
     * handle a connection request.
     * @param line line got from the client
     */
    public void connectionHandler(String line){
        currantClientPseudo = line.substring(8);

        //TODO forgive same pseudo ?

        //send connection message to every one
        for (ClientDescriptor cd:clientList) {
            cd.getStream().println("SIGNIN "+currantClientPseudo);
        }
        try{
            //send the list of (other) client currently connected:
            for (ClientDescriptor cd:clientList) {
                if(!cd.getPseudo().equals(currantClientPseudo)){
                    socOut.println("SIGNIN "+cd.getPseudo());
                }
            }
            //send the last 10 message to the client:
            int i=history.size()-HISTORY_SIZE_SEND;
            if(i<0){
                i=0;
            }
            for ( ; i<history.size() ; i++){
                Message message = history.get(i);
                //only send the message if destinated to currant user
                if(message.getPseudoClientReceiver().equals("all") ||
                        message.getPseudoClientReceiver().equals(currantClientPseudo) ||
                        message.getIdClient().equals(currantClientPseudo)){
                    socOut.println(message.toSocketClient());
                }
                else{
                    i--;
                    if (i<0){
                        i=0;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * handle disconnection request.
     */
    public void disconnectionHandler() {
        for (int i = 0; i < clientList.size(); i++) {
            if(clientList.get(i).getPseudo().equals(currantClientPseudo)) {
                clientList.remove(i);
                i--;
                //if different client have the same pseudo, remove them all
            }
        }
        //send disconnection message to every one
        for (ClientDescriptor cd:clientList) {
            cd.getStream().println("SIGNOUT "+currantClientPseudo);
        }
    }
}


