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
	public int login(String pseudo) throws RemoteException;
	public int logout(String pseudo) throws RemoteException;

}
