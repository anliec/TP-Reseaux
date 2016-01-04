/**
 * @filename Reception.java
 */
package tp.protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author pllefebvre
 *         Client Reception Interface for the RMI Chat Program, which describes all the remote methods that can
 *         be called by a remote server to a client
 */
public interface ReceptionItf extends Remote {
	
	/**
	 * Method called to make the client associated to this Interface receive a new message from the conversation
	 * which concerns the client
	 * 
	 * @param aMessage
	 *            the Message to receive
	 * @throws RemoteException
	 *             the errors of linking by the registry and remote methods
	 */
	
	public void receive(Message aMessage) throws RemoteException;
}
