/**
 * @filename Request.java
 */
package tp.server;

import java.rmi.RemoteException;
import java.util.Collection;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;
import tp.protocol.RequestItf;

/**
 * @author pllefebvre
 *
 */
public class Request implements RequestItf {
	
	private Server server;
	
	/**
	 * Classe de generation d'interface de requetes du serveur
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
	 * @see tp.protocol.RequestItf#login(tp.protocol.ReceptionItf)
	 */
	public int login(ReceptionItf pseudo) throws RemoteException {
		// TODO Auto-generated method stub
		return server.addClient(pseudo);
	}

	/* (non-Javadoc)
	 * @see tp.protocol.RequestItf#logout(long)
	 */
	public int logout(ReceptionItf pseudo) throws RemoteException {
		// TODO Auto-generated method stub
		return server.removeClient(pseudo);
	}

	/* (non-Javadoc)
	 * @see tp.protocol.RequestItf#lastN(int)
	 */
	@Override
	public Collection<Message> lastN(int n) throws RemoteException {
		// TODO Auto-generated method stub
		return server.lastN(n);
	}

}
