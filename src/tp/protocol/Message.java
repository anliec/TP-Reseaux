/**
 * @filename Message.java
 */
package tp.protocol;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pllefebvre
 *
 */

public class Message implements Serializable {

	private static final long serialVersionUID = 1543219565001L;
	private String pseudoClient;
	private String pseudoClientReceiver;
	private Date date;
	private String message;
	
	/**
	 * Constructeur par defaut de Message
	 */
	public Message(String client, Date aDate, String aMessage) {
		// TODO Auto-generated constructor stub
		pseudoClient = client;
		date = aDate;
		message = aMessage;
		pseudoClientReceiver = "all";
	}

	public Message(String client, Date aDate, String aMessage, String to) {
		// TODO Auto-generated constructor stub
		pseudoClient = client;
		date = aDate;
		message = aMessage;
		pseudoClientReceiver = to;
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
	 * @return the pseudo of the client
	 */
	public String getIdClient() {
		
		return pseudoClient;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		
		return date;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		
		return message;
	}

	public String getPseudoClientReceiver() {
		return pseudoClientReceiver;
	}
}
