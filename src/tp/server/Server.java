/**
 * 
 */
package tp.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;
import tp.protocol.RequestItf;

/**
 * @author Pierre-Louis
 * Classe du serveur
 */

public class Server {

	private LinkedList<Message> history; //plus optimise en ajout/suppression
	private ArrayList<String> pseudoClients; //plus optimise en acces
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Server server = new Server();
	}
	
	/**
	 * 
	 */
	private Server() { 
		
		history = new LinkedList<Message>();
		pseudoClients = new ArrayList<String>();
		
		try {
			
        	LocateRegistry.createRegistry(1099); //lance le registre
            Request obj = new Request(this);
            RequestItf stub = (RequestItf) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Request1", stub);

            System.err.println("Server ready");
			
        } catch (Exception e) {
        	
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}
	
	/**
	 * 
	 * @param aMessage
	 */
	public void addMessage(Message aMessage) {
		
		history.add(aMessage);
		
		for(int i = 0; i < pseudoClients.size(); i++) {
		
			try {
	            
	        	Registry registry = LocateRegistry.getRegistry();
	            ReceptionItf stub = (ReceptionItf) registry.lookup(pseudoClients.get(i));
	            stub.receive(aMessage);
	            
	        } catch (Exception e) {
	            
	        	System.err.println("Server exception : Client : " + pseudoClients.get(i) + " not found ");
	        	removeClient(pseudoClients.get(i));
	            e.printStackTrace();
	        }
		}
	}
	/**
	 * Methode d'ajout d'un client au serveur
	 * @param pseudo
	 * @return 1 si le client a ete correctement integre, 0 sinon, lorsque le pseudo est deja pris
	 */
	public int addClient(String pseudo) {
		
		ListIterator<String> li = pseudoClients.listIterator();
		while ( li.hasNext() && !li.next().equals(pseudo) );
		
		if(!li.hasNext()) {
			
			pseudoClients.add(pseudo);
			return 1;
		
		} else {
			
			System.err.println("pseudo " + pseudo + " already used");
			return 0;
		}
	}
	
	/**
	 * Methode de suppression d'un client du server
	 * @param pseudo le pseudo du client
	 * @return 1 si le client etait bien connecte au serveur et a donc bien ete retire, 0 sinon
	 */
	
	public int removeClient(String pseudo) {
		
		ListIterator<String> li = pseudoClients.listIterator();
		while ( li.hasNext() && !li.next().equals(pseudo) );
		
		if(li.hasNext()) {
			
			pseudoClients.remove(pseudo);
			return 1;
		
		} else {
			
			System.err.println("client " + pseudo + " not in server");
			return 0;
		}
	}
}
