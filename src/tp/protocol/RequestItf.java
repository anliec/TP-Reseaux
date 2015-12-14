/**
 * @filename Request.java
 */
package tp.protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author pllefebvre
 *
 */
public interface RequestItf extends Remote {
	
	public void send(Message aMessage) throws RemoteException;
	public int login(ReceptionItf pseudo) throws RemoteException;
	public int logout(ReceptionItf pseudo) throws RemoteException;
	public Message[] lastN(int n) throws RemoteException;

}
