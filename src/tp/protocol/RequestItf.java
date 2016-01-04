/**
 * @filename Request.java
 */
package tp.protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author pllefebvre
 *         Server Request Interface for the RMI Chat Program, which describes all the remote methods that can
 *         be called by a remote client to the server
 */
public interface RequestItf extends Remote {
	
	/**
	 * Method called to send a Message in parameters to the server, which resends the Message to the good receivers
	 * 
	 * @param aMessage
	 *            the Message in question
	 * @throws RemoteException
	 *             the errors of communication with the registry and the remote methods
	 */
	
	public void send(Message aMessage) throws RemoteException;
	
	/**
	 * Method called by a client to log into the server
	 * 
	 * @param pseudo
	 *            the Reception Interface associated to the client (instance of ReceptionItf)
	 * @param ClientName
	 *            the pseudo of the Client (String)
	 * @return a code of processing verification : 1 if the client has been correctly logged in,
	 *         else 0, such as when the client is already connected to the server
	 * @throws RemoteException
	 *             the errors of communication with the registry and the remote methods
	 */
	
	public int login(ReceptionItf pseudo, String ClientName) throws RemoteException;
	
	/**
	 * Method called by a client to log out from the server
	 * 
	 * @param pseudo
	 *            the Reception Interface associated to the client (instance of ReceptionItf)
	 * @param ClientName
	 *            the pseudo of the Client (String)
	 * @return 1 if the client was well connected to the server and has been
	 *         well disconnected, else 0.
	 * @throws RemoteException
	 *             the errors of communication with the registry and the remote methods
	 */
	
	public int logout(ReceptionItf pseudo, String ClientName) throws RemoteException;
	
	/**
	 * Method which return the last n Messages of the conversation which concern the client of which
	 * the pseudo is given in parameters
	 * 
	 * @param n
	 * @param ClientName
	 *            the pseudo of the Client
	 * @return a table of the n concerned Messages
	 * @throws RemoteException
	 *             the errors of communication with the registry and the remote methods
	 */
	
	public Message[] lastN(int n, String ClientName) throws RemoteException;
	
}
