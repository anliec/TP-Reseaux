package tp.frame.clientSocket;

import tp.client.Client;
import tp.protocol.Message;
import tp.socket.SocketClient;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.Timer;

/**
 * Created by nicolas on 30/12/15.
 */
public class ClientSocket extends JFrame{
    private JList lst_otherClient;
    private JButton sendButton;
    private JTextField tf_message;
    private JTextPane tp_history;
    private JPanel WindowPanel;

    private StyledDocument docHistory;
    private Style defaultStyle;
    DefaultListModel listModel;

    private SocketClient client;
    private LinkedList<String> listOtherClient = new LinkedList<>();
    private String updatedMessageView;
    private String selectedClient;

    private int lastHistoryIndex=-1;

    private  javax.swing.Timer UpdateTimer;

    /*public ClientSocket(){
        client = new SocketClient("test","127.0.0.1",4000);
        init();
    }*/

    public ClientSocket(String clientPseudo, String serverIP, int serverPort){
        client = new SocketClient(clientPseudo,serverIP,serverPort);
        init();
    }

    private void init(){
        //init config
        listOtherClient.add("all");
        selectedClient = "all";
        docHistory = tp_history.getStyledDocument();
        defaultStyle = tp_history.getStyle("default");
        listModel = new DefaultListModel();
        listModel.addElement(listOtherClient.getFirst());
        lst_otherClient.setModel(listModel);
        updateHistory();
        lst_otherClient.setSelectedIndex(0);

        //timer management
        ActionListener timerTick = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateHistory();
            }
        };
        UpdateTimer = new javax.swing.Timer(200,timerTick);
        UpdateTimer.start();

        //UI action listener
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                client.sendDisconnectionRequest();
            }
        });
        lst_otherClient.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(lst_otherClient.getSelectedIndex() == -1)
                {
                    lst_otherClient.setSelectedIndex(0);
                }
                selectedClient = lst_otherClient.getSelectedValue().toString();
                System.out.println(selectedClient);
                resetHistoryView();
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendMessage();
                updateHistory();
            }
        });
    }

    ///=======================constructor end================================================

    public void display(){
        updateTitle();
        this.setContentPane(WindowPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void updateTitle(){
        this.setTitle("Chat - "+client.getUserName());
    }

    private void updateHistory(){
        LinkedList<Message> history = client.getHistory();
        updatedMessageView = new String();
        LinkedList<String> oldConnectedClient = (LinkedList<String>)listOtherClient.clone();

        for(int i=lastHistoryIndex+1 ; i<history.size() ; i++) {
            messageHandler(history.get(i));
            lastHistoryIndex = i;
        }

        //update other client list
        for (String oc:listOtherClient) {
            if(!oldConnectedClient.contains(oc)){
                listModel.addElement(oc);
            }
        }
        for (String ooc:oldConnectedClient) {
            if(!listOtherClient.contains(ooc)){
                listModel.removeElement(ooc);
            }
        }
        //update message view
        try{
            docHistory.insertString(docHistory.getLength(),updatedMessageView,defaultStyle);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void resetHistoryView(){
        lastHistoryIndex = -1; //reset to load full history
        tp_history.setText("");
        updateHistory();
    }

    private void messageHandler(Message message){
        if (message.getIdClient() == null) {
            return;
        }
        switch (message.getIdClient()){
            case "server":
                String text=message.getMessage();
                if(text.substring(text.length()-13).equals(" signed in...")){
                    addOtherClient(text.substring(0,text.length()-13));
                }
                else if(text.substring(text.length()-14).equals(" signed out...")){
                    removeOtherClient(text.substring(0,text.length()-14));
                }
                //don't break to show connection / disconnection message on view
            default:
                addMessageToDisplay(message);
                break;
        }
    }

    private void addOtherClient(String clientName){
        if(clientName.equals(client.getUserName())) {
            return;
        }
        for (String oc:listOtherClient) {
            if(oc.equals(clientName)){
                return;
            }
        }
        listOtherClient.add(clientName);
    }

    private void removeOtherClient(String clientName){
        listOtherClient.remove(clientName);
    }

    private void addMessageToDisplay(Message message){
        if(message.getIdClient().equals(selectedClient) ||
                message.getPseudoClientReceiver().equals(selectedClient) ||
                message.getIdClient().equals("server") || // only show the currently selected client ?
                selectedClient.equals("all")
                ){
            updatedMessageView += message.toString();
            updatedMessageView += '\n';
        }
    }

    private void sendMessage(){
        if(tf_message.getText().length() > 0) {
            Message toSend = new Message(client.getUserName(),new Date(),tf_message.getText(),selectedClient);
            tf_message.setText("");
            client.sendMessage(toSend);
        }
    }

    /*public JPanel getContentPane(){
        return WindowPanel;
    }*/

    /*public static void main(String[] args) {
        ClientSocket frame = new ClientSocket("test","127.0.0.1",4000);
        frame.display();
    }*/
}
