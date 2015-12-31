package tp.frame.connection;

import tp.frame.clientSocket.ClientSocket;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

/**
 * Created by nicolas on 29/12/15.
 */
public class ConnectionWindow extends JFrame {
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
                    socketFrame = new ClientSocket(tf_pseudo.getText(),FTF_IP.getText(),(Integer)sp_Port.getValue());
                    socketFrame.display();
                    setVisible(false);
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

    private void display(){
        this.setTitle("Connection Settings");
        this.setContentPane(connectionWindow);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(300,300));
        this.setSize(new Dimension(300,300));
        this.pack();
        this.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) {
        ConnectionWindow MainWindows = new ConnectionWindow();
        MainWindows.display();
    }
}
