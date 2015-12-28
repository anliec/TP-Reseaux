/**
 * @filename Message.java
 */
package tp.protocol;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pllefebvre
 *         Class of objects which represent the messages sent by the clients in the conversation
 *         with the user name of the client, the date at which the message has been send, the message text,
 *         and the receiver of the message
 */

public class Message implements Serializable {
	
	private static final long serialVersionUID = 10L;

	private String pseudoClient;
	private String pseudoClientReceiver;
	private Date date;
	private String message;
	
	/**
	 * "To all" Message Constructor
	 * 
	 * @param client
	 *            the user name of the client who create the message
	 * @param aDate
	 *            the message creation date
	 * @param aMessage
	 *            the message text
	 */
	
	public Message(String client, Date aDate, String aMessage) {
		
		pseudoClient = client;
		date = aDate;
		message = aMessage;
		pseudoClientReceiver = "all";
	}
	
	/**
	 * Constructor of a Message sent to a particular receiver
	 * 
	 * @param client
	 *            the user name of the client which sent the message
	 * @param aDate
	 *            the date of creation of the message
	 * @param aMessage
	 *            the text of the message
	 * @param to
	 *            the receiver (useful for the Socket Chat Program)
	 */
	
	public Message(String client, Date aDate, String aMessage, String to) {
		
		pseudoClient = client;
		date = aDate;
		message = aMessage;
		pseudoClientReceiver = to;
	}

	public Message(String socketClientStyleMessage){
		int firstWordEnd = socketClientStyleMessage.indexOf(" ");
		String request = socketClientStyleMessage.substring(0,firstWordEnd-1);
		switch (request)
		{
			case "MESSAGE":
				int endOfUser = socketClientStyleMessage.indexOf(" TO ");
				int endOfToUser = socketClientStyleMessage.indexOf(" CONTENT ");
				pseudoClient = socketClientStyleMessage.substring(firstWordEnd+6,endOfUser);
				pseudoClientReceiver = socketClientStyleMessage.substring(endOfUser+4,endOfToUser);
				message = socketClientStyleMessage.substring(endOfToUser+9);
				break;
			case "SIGIN":
				pseudoClient = "server";
				pseudoClientReceiver = "all";
				message = socketClientStyleMessage.substring(6)+" signed in...";
				//TODO implement sigin message to UI
				break;
			case "SIGOUT":
				pseudoClient = "server";
				pseudoClientReceiver = "all";
				message = socketClientStyleMessage.substring(7)+" signed out...";
				//TODO implement sigout message to UI
				break;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return pseudoClient + " > " + pseudoClientReceiver + " on : " + date +
		        " : \n" + message;
	}

	/**
	 * @return the text message to a client according to the socket protocol.
	 */
	public String toSocketClient() {
		return "MESSAGE FROM "+pseudoClient+" TO "+pseudoClientReceiver+" CONTENT "+message;
	}

	/**
	 * @return the text message to a server according to the socket protocol.
	 */
	public String toSocketServer(){
		return "SENDTO "+pseudoClientReceiver+" CONTENT "+message;
	}
	
	/**
	 * @return the user name of the client who sends the message
	 */
	public String getIdClient() {
		
		return pseudoClient;
	}
	
	/**
	 * @return the date of creation of the message
	 */
	public Date getDate() {
		
		return date;
	}
	
	/**
	 * @return the text of the message
	 */
	public String getMessage() {
		
		return message;
	}
	
	/**
	 * @return the receiver of the message (useful for Socket Chat Program)
	 */
	
	public String getPseudoClientReceiver() {
		return pseudoClientReceiver;
	}
}
