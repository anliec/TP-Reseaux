/**
 * @filename Request.java
 */
package tp.server;

import java.rmi.RemoteException;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;
import tp.protocol.RequestItf;

/**
 * @author pllefebvre
 *         Class which implements RequestItf to represent the server interface
 */
public class Request implements RequestItf {
	
	private Server server;
	
	/**
	 * Default Server Interface Constructor
	 * 
	 * @param server
	 *            the server which instantiates this interface
	 */
	
	public Request(Server server) {
		// TODO Auto-generated constructor stub
		this.server = server;
	}
	
	/*
	 * (non-Javadoc)
	 * @see tp.protocol.RequestItf#send(tp.protocol.Message)
	 */
	public void send(Message aMessage) throws RemoteException {
		// TODO Auto-generated method stub
		server.addMessage(aMessage);
	}
	
	/*
	 * (non-Javadoc)
	 * @see tp.protocol.RequestItf#login(tp.protocol.ReceptionItf)
	 */
	public int login(ReceptionItf pseudo, String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		return server.addClient(pseudo, clientName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see tp.protocol.RequestItf#logout(long)
	 */
	public int logout(ReceptionItf pseudo, String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		return server.removeClient(pseudo, clientName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see tp.protocol.RequestItf#lastN(int)
	 */
	@Override
	public Message[] lastN(int n, String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		return server.lastN(n, clientName);
	}
	
}
