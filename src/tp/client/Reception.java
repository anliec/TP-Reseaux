/**
 * @filename Reception.java
 */
package tp.client;

import java.rmi.RemoteException;
import java.util.LinkedList;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;

/**
 * @author pllefebvre
 *
 */

public class Reception implements ReceptionItf {

	private Client client;
	/**
	 * Constructeur par de la reception du client
	 * @param client le client dont c'est la reception
	 */
	public Reception(Client client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see tp.protocol.ReceptionItf#receive(tp.protocol.Message)
	 */
	public void receive(Message aMessage) throws RemoteException {
		// TODO Auto-generated method stub
		client.addMessage(aMessage);
	}

}
