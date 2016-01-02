package tp.frame.clientSocket;

import tp.client.Client;
import tp.protocol.Message;
import tp.socket.SocketClient;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

/**
 * window build around a socket client
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

    /**
     * main constructor
     * @param clientPseudo pseudo of the currant client
     * @param serverIP ip of the server to connect
     * @param serverPort port of the server to connect
     */
    public ClientSocket(String clientPseudo, String serverIP, int serverPort){
        client = new SocketClient(clientPseudo,serverIP,serverPort);
        init();

    }

    /**
     * initialisation procedure. extension of the constructor (comon part of different constructor)
     */
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
        tf_message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(e.getKeyChar() == '\n'){
                    sendMessage();
                    updateHistory();
                }
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

    /**
     * show up the client socket window as a main window
     */
    public void display(){
        updateTitle();
        this.setContentPane(WindowPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    /**
     * set the title of the window
     */
    private void updateTitle(){
        this.setTitle("Chat - "+client.getUserName());
    }

    /**
     * update the history panel by showing all the received message corresponding to the UI state
     */
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

    /**
     * force complete update of the history panel
     */
    private void resetHistoryView(){
        lastHistoryIndex = -1; //reset to load full history
        tp_history.setText("");
        updateHistory();
    }

    /**
     * handel the work to be done for a message.
     * @param message the message on witch the work will be done
     */
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

    /**
     * add a client to the UI left panel
     * @param clientName name of the client to add
     */
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

    /**
     * remove a client to the UI left panel
     * @param clientName name of the client to remove
     */
    private void removeOtherClient(String clientName){
        listOtherClient.remove(clientName);
    }

    /**
     * display if necessary the given message on the history panel
     * @param message message to display
     */
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

    /**
     * get the message from the UI and send it
     */
    private void sendMessage(){
        if(tf_message.getText().length() > 0) {
            Message toSend = new Message(client.getUserName(),new Date(),tf_message.getText(),selectedClient);
            tf_message.setText("");
            client.sendMessage(toSend);
        }
    }
}
