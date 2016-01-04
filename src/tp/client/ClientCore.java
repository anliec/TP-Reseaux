/**
 * @filename Client.java
 */
package tp.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Date;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;
import tp.protocol.RequestItf;

/**
 * @author pllefebvre
 *         Class of the system core of the client program for the RMI Chat, which describe the connection
 *         and the communication with the server
 */
public class ClientCore {
	
	private String pseudo;
	private Registry registry;
	private ReceptionItf receptionStub;
	private RequestItf requestStub;
	private LinkedList<Message> history = new LinkedList<>();
	
	/**
	 * Client Core parameterized constructor
	 * 
	 * @param clientPseudo
	 *            the String which contains the pseudo of the client
	 * @param hostIp
	 *            the IP address of the server on which the client connects to
	 * @throws Exception
	 *             the errors of connections to the server or the creation of the registry
	 */
	
	public ClientCore(String clientPseudo, String hostIp) throws Exception {
		pseudo = clientPseudo;
		registry = LocateRegistry.getRegistry(hostIp);
		// connection to the server interface
		requestStub = (RequestItf) registry.lookup("Request1");
		
		ReceptionCore reception = new ReceptionCore(this);
		receptionStub = (ReceptionItf) UnicastRemoteObject.exportObject(reception, 0);
		requestStub.login(receptionStub, pseudo);
		
		// displaying of the last 10 messages of the conversation
		Message[] last = lastN(10);
		for (Message message : last) {
			if (message != null) history.add(message);
			else break;
		}
	}
	
	/**
	 * method by which the client is disconnected from the server
	 */
	
	public void quit() {
		try {
			// when the client quit the chat, it is removed from the server list
			requestStub.logout(receptionStub, pseudo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method which save the Message given in parameters in a client messages history and
	 * display the message
	 * 
	 * @param aMessage
	 *            the Message
	 */
	
	public void addMessage(Message aMessage) {
		history.add(aMessage);
	}
	
	/**
	 * Method to create a instance of Message from a String
	 * 
	 * @param text
	 *            the String
	 * @return the created Message
	 */
	
	private Message createMessage(String text) {
		return new Message(pseudo, new Date(), text);
	}
	
	/**
	 * When this method is called, the instance of Client whose this method belongs
	 * to sends the text message in parameters to the server on which it is connected
	 * 
	 * @param string
	 *            the text message
	 */
	public void send(String string) {
		send(createMessage(string));
	}
	
	/**
	 * When this method is called, the instance of Client whose this method belongs
	 * to sends the instance of Message in parameters to the server on which it is connected
	 * 
	 * @param message
	 *            the instance of Message
	 */
	public void send(Message message) {
		try {
			requestStub.send(message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method which returns the n last messages of the conversation which concern the Client
	 * which calls the method
	 * 
	 * @param n
	 * @return the n last concerned Message in a table of Message instances
	 */
	
	public Message[] lastN(int n) {
		
		Message[] last = new Message[0];
		
		try {
			
			last = requestStub.lastN(n, pseudo);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return last;
	}
	
	/**
	 * @return the Message history of the client (Messages in the conversation that concerns the
	 *         client which calls the the method)
	 */
	
	public LinkedList<Message> getHistory() {
		return history;
	}
	
	/**
	 * @return the pseudo of the client which calls this method
	 */
	
	public String getPseudo() {
		return pseudo;
	}
}
