package tp.socket;

import tp.protocol.Message;

import java.io.BufferedReader;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by nicolas on 14/12/15.
 */
public class ClientThread extends Thread {

    private LinkedList<Message> history;
    private BufferedReader socIn;
    private boolean loop;

    public ClientThread(LinkedList<Message> messagesHistory, BufferedReader stdInput)
    {
        history = messagesHistory;
        socIn = stdInput;
    }

    public void run(){
        loop = true;
        String line;
        while (loop) {
            try{
                line= socIn.readLine();
                System.out.println("received: "+line);
                if(line.equals("."))
                {
                    loop = false;
                }
                else
                {
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

    public void close()
    {
        loop = false;
    }

}
