package tp.socket;

import tp.protocol.Message;

import java.io.BufferedReader;
import java.util.Date;
import java.util.LinkedList;

/**
 * Thread which handle the reception of new message send by the server
 */
public class ClientThread extends Thread {

    private LinkedList<Message> history;
    private BufferedReader socIn;
    private boolean loop;

    /**
     * default constructor
     * @param messagesHistory reference to the message history, in witch will be added the messages
     * @param stdInput input to listen to, for server communication
     */
    public ClientThread(LinkedList<Message> messagesHistory, BufferedReader stdInput)
    {
        history = messagesHistory;
        socIn = stdInput;
    }

    /**
     * thread main loop:
     * handle the reception of new message send by the server
     */
    public void run(){
        loop = true;
        String line;
        while (loop) {
            try{
                line= socIn.readLine();
                if(line.substring(0,25).equals("DISCONNECTED BY SERVER: ")) {
                    loop = false;
                }
                else {
                    Message message = new Message(line);
                    history.add(message);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * stop the thread infinite loop
     */
    public void close()
    {
        loop = false;
    }

}
