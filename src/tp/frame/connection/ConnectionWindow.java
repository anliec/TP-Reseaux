package tp.frame.connection;

import tp.frame.clientRMI.ClientRMI;
import tp.frame.clientSocket.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by nicolas on 29/12/15.
 */
public class ConnectionWindow extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 105L;
	private JButton connectButton;
    private JButton quitButton;
    private JRadioButton Rb_RMI;
    private JRadioButton Rb_Socket;
    ButtonGroup RbGroup = new ButtonGroup();
    private JFormattedTextField FTF_IP;
    private JSpinner sp_Port;
    private JTextField tf_pseudo;
    private JPanel connectionWindow;
    private JLabel lb_port;


    private ClientSocket socketFrame;
    private ClientRMI rmiFrame;

    /**
     * constructor: setup the UI (but don't display it)
     */
    public ConnectionWindow() {
        //set server port text field to only allow IP address: (disable because force the user to use: 127.000.000.001)
        /*try{
            MaskFormatter mf = new MaskFormatter("###.###.###.###");
            DefaultFormatterFactory factoryIP = new DefaultFormatterFactory(mf);
            FTF_IP.setFormatterFactory(factoryIP);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        SpinnerModel sp_PortModel = new SpinnerNumberModel(4000,0,99999999,1);
        sp_Port.setModel(sp_PortModel);

        //group the radio button to ensure that only one is selected:
        RbGroup.add(Rb_RMI);
        RbGroup.add(Rb_Socket);

        //listener:
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Rb_Socket.isSelected()){
                    try{
                        socketFrame = new ClientSocket(tf_pseudo.getText(),FTF_IP.getText(),(Integer)sp_Port.getValue());
                        socketFrame.display();
                        setVisible(false);
                    }catch (Exception e){
                        JOptionPane.showMessageDialog(socketFrame,
                                e.getMessage(),
                                "Server error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(Rb_RMI.isSelected()){
                    try{
                        rmiFrame = new ClientRMI(tf_pseudo.getText(),FTF_IP.getText());
                        rmiFrame.display();
                        setVisible(false);
                    }catch (Exception e){
                        JOptionPane.showMessageDialog(rmiFrame,
                                e.getMessage(),
                                "Server error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        Rb_Socket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Rb_Socket.isSelected())
                {
                    sp_Port.setEnabled(true);
                    lb_port.setEnabled(true);
                }
            }
        });
        Rb_RMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Rb_RMI.isSelected())
                {
                    sp_Port.setEnabled(false);
                    lb_port.setEnabled(false);
                }
            }
        });
    }

    /**
     * show up the connection window as a main window
     */
    private void display(){
        this.setTitle("Connection Settings");
        this.setContentPane(connectionWindow);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(300,300));
        this.setSize(new Dimension(300,300));
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ConnectionWindow MainWindows = new ConnectionWindow();
        MainWindows.display();
    }
}
