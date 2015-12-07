/**
 * @filename Message.java
 */
package tp.protocol;

import java.util.Date;
/**
 * @author pllefebvre
 *
 */
public class Message {

	private long idClient;
	private Date date;
	private String message;
	
	/**
	 * Constructeur par defaut de Message
	 */
	public Message(long client, Date aDate, String aMessage) {
		// TODO Auto-generated constructor stub
		idClient = client;
		date = aDate;
		message = aMessage;
	}
	
	/**
	 * @return the idClient
	 */
	public long getIdClient() {
		return idClient;
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
}
