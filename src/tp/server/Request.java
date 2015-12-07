/**
 * @filename Request.java
 */
package tp.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

import tp.protocol.ReceptionItf;
import tp.protocol.Message;
import tp.protocol.RequestItf;

/**
 * @author pllefebvre
 *
 */
public class Request implements RequestItf {
	
	private LinkedList<Message> history;
	private LinkedList<String> pseudoClients;
	/**
	 * 
	 */
	public Request() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see tp.protocol.RequestItf#send(tp.protocol.Message)
	 */
	@Override
	public void send(Message aMessage) throws RemoteException {
		// TODO Auto-generated method stub
		history.add(aMessage);
		
		for(int i = 0; i < pseudoClients.size(); i++) {
			
			try {
	            
	        	Registry registry = LocateRegistry.getRegistry();
	            ReceptionItf stub = (ReceptionItf) registry.lookup(pseudoClients.get(i));
	            stub.receive(aMessage);
	            
	        } catch (Exception e) {
	            
	        	System.err.println("Server exception : Client : " + pseudoClients.get(i) + " not found ");
	        	logout(pseudoClients.get(i));
	            e.printStackTrace();
	        }
		}
	}

	/* (non-Javadoc)
	 * @see tp.protocol.RequestItf#login(java.lang.String)
	 */
	@Override
	public int login(String pseudo) throws RemoteException {
		// TODO Auto-generated method stub
		return 1;
	}

	/* (non-Javadoc)
	 * @see tp.protocol.RequestItf#logout(long)
	 */
	@Override
	public int logout(String pseudo) throws RemoteException {
		// TODO Auto-generated method stub
		return 1;
	}

}
