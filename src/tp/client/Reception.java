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

	private LinkedList<Message> history;
	
	/**
	 * Constructeur par defaut de reception
	 */
	public Reception() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see tp.protocol.ReceptionItf#receive(tp.protocol.Message)
	 */
	@Override
	public void receive(Message aMessage) throws RemoteException {
		// TODO Auto-generated method stub
		history.add(aMessage);
		
	}

}
