/**
 * @filename Server.java
 */
package tp.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;
import tp.protocol.RequestItf;

/**
 * @author Pierre-Louis
 *         Class to instanciante the server
 */

public class Server {
	
	private final static String HISTO_FILE_NAME = "logs\\histo.log";
	
	private LinkedList<Message> history; // plus optimise en ajout/suppression
	private ArrayList<ReceptionItf> receptionClients; // plus optimise en acces
	private RequestItf requestStub;
	private Registry registry;
	
	/**
	 * Default Server Constructor.
	 * Initialize the messages' history and create a reference to a register
	 */
	public Server() {
		
		history = FileGesture.loadHistory(HISTO_FILE_NAME);
		receptionClients = new ArrayList<ReceptionItf>();
		
		try {
			registry = LocateRegistry.createRegistry(1099); // lance le registre
		} catch (RemoteException e) {
			System.err.println("Server exception" + e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method which connects to the registry and run the server
	 */
	public void run() {
		
		try {
			Request request = new Request(this);
			requestStub = (RequestItf) UnicastRemoteObject.exportObject(
			        request, 0);
			
			// Bind the remote object's stub in the registry
			registry.bind("Request1", requestStub);
			
			System.out.println("Server ready");
			
		} catch (Exception e) {
			
			System.err.println("Server exception: " + e);
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		String cmd = "";
		
		while (cmd != "c") {
			cmd = sc.nextLine();
		}
		sc.close();
		close();
		System.out.println("yo");
		System.exit(0);
	}
	
	/**
	 * Method which closes the server saves the conversation history (the
	 * messages' list sent to the server) in the file histo.log
	 */
	public void close() {
		
		FileGesture.saveHistory(HISTO_FILE_NAME, history);
	}
	
	/**
	 * Method which adds a message to the server, which resends the message to
	 * all the clients
	 * 
	 * @param aMessage
	 *            the message
	 */
	public void addMessage(Message aMessage) {
		
		history.add(aMessage);
		System.out.println(aMessage);
		for (int i = 0; i < receptionClients.size(); i++) {
			
			try {
				
				receptionClients.get(i).receive(aMessage);
				
			} catch (Exception e) {
				
				System.err.println("Server exception : Client : "
				        + receptionClients.get(i) + " not found ");
				removeClient(receptionClients.get(i));
				System.err.println("Server exception: " + e);
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Method which adds a client to the server
	 * 
	 * @param pseudo
	 *            the interface (instance of ReceptionItf) of the client
	 * @return 1 if the client has been correctly added, else 0, such as when
	 *         the interface already exists
	 */
	public int addClient(ReceptionItf pseudo) {
		
		ListIterator<ReceptionItf> li = receptionClients.listIterator();
		while (li.hasNext() && !li.next().equals(pseudo))
			;
		
		if (!li.hasNext()) {
			
			receptionClients.add(pseudo);
			System.out.println("Client : " + pseudo + " added");
			return 1;
			
		} else {
			
			System.err.println("Client : " + pseudo + " already exists");
			return 0;
		}
	}
	
	/**
	 * Method which removes client from the server. This method removes the
	 * client interface access from the server and return a state code
	 * 
	 * @param receptionItf
	 *            the client interface
	 * @return 1 if the client was well connected to the server and has been
	 *         well disconnected, else 0.
	 */
	public int removeClient(ReceptionItf receptionItf) {
		
		ListIterator<ReceptionItf> li = receptionClients.listIterator();
		while (li.hasNext() && !li.next().equals(receptionItf))
			;
		
		int code = 0;
		try {
			
			li.remove();
			System.out.println("Client : " + receptionItf + " removed");
			code = 1;
			
		} catch (Exception e) {
			
			System.err.println("Client : " + receptionItf + " not in server");
			System.err.println("Server exception: " + e);
			e.printStackTrace();
			
		}
		
		if (receptionClients.isEmpty())
		    close();
		return code;
	}
	
	/**
	 * Method which returns the last n messages of the conversation carried by
	 * the server
	 * 
	 * @param n
	 * @return the table of the n last messages
	 */
	
	public Message[] lastN(int n) {
		
		int last = Math.max(history.size(), 0);
		return history.subList(Math.max(last - n, 0), last).toArray(
		        new Message[n]);
	}
}
