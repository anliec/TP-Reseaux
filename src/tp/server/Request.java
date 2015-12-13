/**
 * @filename Request.java
 */
package tp.server;

import java.rmi.RemoteException;

import tp.protocol.Message;
import tp.protocol.RequestItf;

/**
 * @author pllefebvre
 *
 */
public class Request implements RequestItf {
	
	private Server server;
	
	/**
	 * 
	 */
	public Request(Server server) {
		// TODO Auto-generated constructor stub
		this.server = server;
	}

	/* (non-Javadoc)
	 * @see tp.protocol.RequestItf#send(tp.protocol.Message)
	 */
	public void send(Message aMessage) throws RemoteException {
		// TODO Auto-generated method stub
		server.addMessage(aMessage);
	}

	/* (non-Javadoc)
	 * @see tp.protocol.RequestItf#login(java.lang.String)
	 */
	public int login(String pseudo) throws RemoteException {
		// TODO Auto-generated method stub
		return server.addClient(pseudo);
	}

	/* (non-Javadoc)
	 * @see tp.protocol.RequestItf#logout(long)
	 */
	public int logout(String pseudo) throws RemoteException {
		// TODO Auto-generated method stub
		return server.removeClient(pseudo);
	}

}
