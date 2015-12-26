package tp.frame;

import java.awt.Dimension;
import java.text.NumberFormat;

import tp.protocol.Message;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import tp.client.Client;

/**
 * Created by nicolas on 14/12/15.
 */
public class DiscussionWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextPane tpMessages;
	private StyledDocument docMessage;
	private Box mainBox; // a JPanel with BoxLayout as LayoutManager
	private Box commandBox;
	private Box bottomBox;
	private Box displayBox;
	private Box nBox;
	private JButton sendButton;
	private JButton nLastMessagesButton;
	private JFormattedTextField tfNLastMessages; // a formatted Text Field to
													// receive integers only
	private JTextPane tpCLientsConnected;
	private JLabel nEgal;
	private StyledDocument docClientsConnected;
	private Style defaultStyle;
	private JTextField tfMessageSend;

	private Client client;

	/**
	 * Constructeur d'un fenetre de chat client
	 * 
	 * @param chatProgram
	 *            le nom sous forme de String du programme de chat
	 * @param client
	 *            le client associe au chat
	 */

	public DiscussionWindow(String chatProgram, Client client) {
		// window configuration
		setTitle(chatProgram + " - Client");
		setSize(400, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.client = client;

		mainBox = Box.createVerticalBox();

		displayBox = Box.createHorizontalBox();

		tpMessages = new JTextPane();
		tpMessages.setPreferredSize(new Dimension(290, 500));
		docMessage = tpMessages.getStyledDocument();

		tpCLientsConnected = new JTextPane();
		tpCLientsConnected.setPreferredSize(new Dimension(100, 500));
		docClientsConnected = tpCLientsConnected.getStyledDocument();

		defaultStyle = tpMessages.getStyle("default");

		displayBox.add(tpMessages);
		displayBox.add(tpCLientsConnected);

		bottomBox = Box.createHorizontalBox();

		tfMessageSend = new JTextField("Tapez vos messages ici ...");
		tfMessageSend.setPreferredSize(new Dimension(300, 25));

		commandBox = Box.createVerticalBox();

		sendButton = new JButton("            Envoyer             ");
		sendButton.setPreferredSize(new Dimension(100, 35));
		sendButton.addActionListener(new SendButtonListener(this));
		nLastMessagesButton = new JButton("n derniers messages");
		nLastMessagesButton.setPreferredSize(new Dimension(120, 35));
		nLastMessagesButton.addActionListener(new LastNButtonListener(this));

		nBox = Box.createHorizontalBox();

		nEgal = new JLabel(" n = ");
		nEgal.setPreferredSize(new Dimension(30, 25));
		tfNLastMessages = new JFormattedTextField(
				NumberFormat.getIntegerInstance()); // only integers in this
													// JTextField
		tfNLastMessages.setPreferredSize(new Dimension(70, 25));

		nBox.add(nEgal);
		nBox.add(tfNLastMessages);

		commandBox.add(sendButton);
		commandBox.add(nLastMessagesButton);
		commandBox.add(nBox);

		bottomBox.add(tfMessageSend);
		bottomBox.add(commandBox);

		mainBox.add(displayBox);
		mainBox.add(bottomBox);

		setContentPane(mainBox);
		setVisible(true);

	}

	/**
	 * methode d'affichage d'un nouveau message dans la fenetre de chat
	 * @param message le message (instance de Message)
	 */
	
	public void addMessage(Message message) {

		try {
			docMessage.insertString(docMessage.getLength(), message.toString() + "\n",
					defaultStyle);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * methode d'affichage d'un message textuel quelconque sur la fenetre de chat
	 * @param string la string a afficher
	 */

	public void addInfoText(String string) {

		try {
			docMessage.insertString(docMessage.getLength(), string + "\n",
					defaultStyle);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * methode d'envoi au serveur auquel le client est connecte du message contenu dans la zone
	 * ou l'on tape les messages 
	 */

	public void send() {

		client.send(tfMessageSend.getText());
		tfMessageSend.setText(null);
	}
	
	/**
	 * methode d'affichage dans la zone d'affichage des n derniers messages de la conversation, 
	 * n etant le nombre indiqué dans la zone n =
	 */

	public void dispLastN() {

		int n = Integer.parseInt(tfNLastMessages.getText());
		Message[] messages = client.lastN(n);
		addInfoText(" Affichage des " + n + " derniers messages");
		for (Message m : messages) {
			addMessage(m);
		}
	}
	

	public void dispLastN(Message[] messages) {

		addInfoText("affichage des " + messages.length + " derniers messages");
		for (Message m : messages) {
			if(m != null)
				addMessage(m);
		}
	}
}
