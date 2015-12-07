/**
 * @filename Reception.java
 */
package tp.protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author pllefebvre
 *
 */
public interface ReceptionItf extends Remote {
	
	public void receive(Message aMessage) throws RemoteException;
}
