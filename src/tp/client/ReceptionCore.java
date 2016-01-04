/**
 * @filename Reception.java
 */
package tp.client;

import java.rmi.RemoteException;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;

/**
 * @author pllefebvre
 *         Class to generate the RMI Client Reception Interface (implements ReceptionItf)
 */

public class ReceptionCore implements ReceptionItf {
	
	private ClientCore client;
	
	/**
	 * Client Reception Interface Constructor, normally called by the Client himself
	 * 
	 * @param client
	 *            the Client to which this interface is associated (and which generates this interface)
	 */
	
	public ReceptionCore(ClientCore client) {
		this.client = client;
	}
	
	/*
	 * (non-Javadoc)
	 * @see tp.protocol.ReceptionItf#receive(tp.protocol.Message)
	 */
	public void receive(Message aMessage) throws RemoteException {
		client.addMessage(aMessage);
	}
	
}
