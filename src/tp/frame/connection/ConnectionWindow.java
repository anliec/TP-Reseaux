package tp.frame.connection;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Created by nicolas on 29/12/15.
 */
public class ConnectionWindow {
    private JButton connectButton;
    private JButton quitButton;
    private JRadioButton Rb_RMI;
    private JRadioButton Rb_Socket;
    ButtonGroup RbGroup = new ButtonGroup();
    private JFormattedTextField FTF_IP;
    private JFormattedTextField FTF_ServerPort;
    private JTextField textField1;
    private JPanel connectionWindow;
    private JLabel lb_port;

    public ConnectionWindow() {
        //set server port text field to only allow IP address: (disable because force the user to use: 127.000.000.001)
        /*try{
            MaskFormatter mf = new MaskFormatter("###.###.###.###");
            DefaultFormatterFactory factoryIP = new DefaultFormatterFactory(mf);
            FTF_IP.setFormatterFactory(factoryIP);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //set server port text field to only allow numbers:
        NumberFormat nf = NumberFormat.getIntegerInstance(); // Specify specific format here.
        NumberFormatter nff = new NumberFormatter(nf);
        DefaultFormatterFactory factoryInt = new DefaultFormatterFactory(nff);
        FTF_ServerPort.setFormatterFactory(factoryInt);

        //group the radio button to ensure that only one is selected:
        RbGroup.add(Rb_RMI);
        RbGroup.add(Rb_Socket);

        //listener:
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

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
                    FTF_ServerPort.setEnabled(true);
                    lb_port.setEnabled(true);
                }
            }
        });
        Rb_RMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Rb_RMI.isSelected())
                {
                    FTF_ServerPort.setEnabled(false);
                    lb_port.setEnabled(false);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat - Connection Setting");
        frame.setContentPane(new ConnectionWindow().connectionWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,300));
        frame.setSize(new Dimension(300,300));
        frame.pack();
        frame.setVisible(true);
    }
}
