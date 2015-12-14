package tp.client;

import tp.protocol.Message;

import javax.swing.*;
import javax.swing.text.StyledDocument;

/**
 * Created by nicolas on 14/12/15.
 */
public class DiscussionWindow {
    private JTextPane TP_messages;
    private StyledDocument doc_message;
    private JPanel panel1;
    private JButton Bt_Send;
    private JTextField TF_messageSend;

    public DiscussionWindow()
    {
        doc_message = TP_messages.getStyledDocument();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }

    public void addMessage(Message message){
        //doc_message.insertString(doc_message.getLength(),message.getIdClient()+": "+message.getMessage(),);
    }
}
