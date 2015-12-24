/**
 * @filename Server.java
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

	private final static String histoFileName = "logs\\histo.log";
	
	private LinkedList<Message> history; //plus optimise en ajout/suppression
	private ArrayList<ReceptionItf> receptionClients; //plus optimise en acces
	private RequestItf requestStub;
	private Registry registry;
	
	/**
	 * Constructeur par defaut du serveur, initialise l'historique de message et la liste de clients
	 * et le connecte au registre
	 */
	public Server() { 
		
		history = FileGesture.loadHistory(histoFileName);
		receptionClients = new ArrayList<ReceptionItf>();
		try {
			
        	LocateRegistry.createRegistry(1099); //lance le registre
            Request request = new Request(this);
            requestStub = (RequestItf) UnicastRemoteObject.exportObject(request, 0);

            // Bind the remote object's stub in the registry
            registry = LocateRegistry.getRegistry();
            registry.bind("Request1", requestStub);

            System.out.println("Server ready");
			
        } catch (Exception e) {
        	
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	
	}
	
	/**
	 * methode d'execution du server
	 */
	public void run() {
		
	}
	
	/**
	 * methode de fermeture du serveur
	 * enregistre l'historique des messages dans le fichier histo.log
	 */
	public void close() {
		
		FileGesture.saveHistory(histoFileName, history);
	}
	
	/**
	 * Methode d'ajout d'un message au serveur, qui le retransmet a tout les clients
	 * @param aMessage le message
	 */
	public void addMessage(Message aMessage) {
		
		history.add(aMessage);
		System.out.println(aMessage);
		
		for(int i = 0; i < receptionClients.size(); i++) {
		
			try {
	            
				receptionClients.get(i).receive(aMessage);
				   
	        } catch (Exception e) {
	            
	        	System.err.println("Server exception : Client : " + receptionClients.get(i) + " not found ");
	        	removeClient(receptionClients.get(i));
	        	System.err.println("Server exception: " + e.toString());
	        	e.printStackTrace();
	        }
			
		}
	}
	/**
	 * Methode d'ajout d'un client au serveur
	 * @param pseudo
	 * @return 1 si le client a ete correctement integre, 0 sinon, lorsque le pseudo est deja pris
	 */
	public int addClient(ReceptionItf pseudo) {
		
		ListIterator<ReceptionItf> li = receptionClients.listIterator();
		while ( li.hasNext() && !li.next().equals(pseudo) );
		
		if(!li.hasNext()) {
			
			receptionClients.add(pseudo);
			System.out.println("Client : " + pseudo + " added");
			return 1;
		
		} else {
			
			System.err.println("Client : " + pseudo + " already exists");
			return 0;
		}
	}
	
	/**
	 * Methode de suppression d'un client du server
	 * @param receptionItf le pseudo du client
	 * @return 1 si le client etait bien connecte au serveur et a donc bien ete retire, 0 sinon
	 */	
	public int removeClient(ReceptionItf receptionItf) {
		
		ListIterator<ReceptionItf> li = receptionClients.listIterator();
		while ( li.hasNext() && !li.next().equals(receptionItf) );
		
		try{
			
			li.remove();
			System.out.println("Client : " + receptionItf + " removed");
			if(receptionClients.isEmpty()) 
				close();
			return 1;
			
		} catch (Exception e) {
			
			System.err.println("Client : " + receptionItf + " not in server");
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
			if(receptionClients.isEmpty()) 
				close();
			return 0;
		}
		
		
	}
	
	public Message[] lastN(int n) {
		
		int last = Math.max(history.size(), 0);
		return  history.subList(Math.max(last - n, 0), last).toArray(new Message[n]);
	}
}
