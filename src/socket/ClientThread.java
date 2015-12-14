package socket;

import tp.protocol.Message;

import java.io.BufferedReader;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by nicolas on 14/12/15.
 */
public class ClientThread extends Thread {

    public LinkedList<Message> history;
    public BufferedReader socIn;
    boolean loop;

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
                    int firstWordEnd = line.indexOf(" ");
                    String request = line.substring(0,firstWordEnd-1);
                    switch (request)
                    {
                        case "MESSAGE":
                            int endOfUser = line.indexOf(" TO ");
                            int endOfToUser = line.indexOf(" CONTENT ");
                            String user = line.substring(firstWordEnd+6,endOfUser);
                            String toUser = line.substring(endOfUser+4,endOfToUser);
                            String messageText = line.substring(endOfToUser+9);
                            Message message = new Message(user,new Date() ,messageText,toUser );
                            history.add(message);
                            break;
                        case "SIGIN":
                            //TODO implement sigin message to UI
                            break;
                        case "SIGOUT":
                            //TODO implement sigout message to UI
                            break;
                    }
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
