/**
 * @filename Client.java
 */
package tp.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.Scanner;
import java.util.Date;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;
import tp.protocol.RequestItf;

/**
 * @author pllefebvre
 * 
 */
public class Client {

	private String pseudo;
	private Registry registry;
	private ReceptionItf receptionStub;
	private RequestItf requestStub;

	public Client(String[] args) {
		
		String host = (args.length < 1) ? null : args[0];
		
		try {
			
			registry = LocateRegistry.getRegistry(host);
        	Reception reception = new Reception(this);
        	receptionStub = (ReceptionItf) UnicastRemoteObject.exportObject(reception, 0);
        
        	//connection a l'interface serveur
        	requestStub = (RequestItf) registry.lookup("Request1");
        
		} catch (Exception e) {
	            
	        System.err.println("Client exception: " + e.toString());
	        e.printStackTrace();
	    }
	}

	/**
	 * @param args
	 */
	public void run() {
		
		try {
			Scanner scanner = new Scanner(System.in);
            
            //procedure d'inscription
    		System.out.println("choisissez un pseudo :");
    		pseudo = scanner.nextLine();
    		
    		requestStub.login(receptionStub);
    		System.out.println("connecte, \n affichage des 10 derniers messages :");
    		{
    			Message[] last = requestStub.lastN(10);
    			for(Message message : last) {
    				if(message != null) System.out.println(message);
    			}
    		}
    		
    		// systeme de commande d'envoi de message
    		String cmd = "";
    		boolean on = true;
    		while(on) {
    			
    			System.out.println("interface :");
    			cmd = scanner.nextLine();
    			
    			switch (cmd) {
    				
    				case "/m" :
    					
    					System.out.println("Message :");
    					cmd = scanner.nextLine();
    					requestStub.send(createMessage(cmd));
    					break ;
    					
    				case "/l" :
    					
    					System.out.println("nombre de messages a afficher :");
    					int n = scanner.nextInt();
    					{
    		    			Message[] last = requestStub.lastN(n);
    		    			for(Message message : last) {
    		    				if(message != null) System.out.println(message);
    		    			}
    		    		}
    					break;
    					
    				case "/q" :
    					
    					scanner.close();
    		    		
    		    		//quand le client quitte le chat, il est retire de la liste du server
    		    		requestStub.logout(receptionStub);
    		    		on = false;
    					break ;
    					
    				default :
    					
    					break;
    			}
    		}
    		
        } catch (Exception e) {
            
        	System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

	}

	/**
	 * Methode qui enregistre le message passse en parametre dans l'historique
	 * et l'affiche
	 * 
	 * @param aMessage
	 *            le message en question
	 */

	public void addMessage(Message aMessage) {

		System.out.println(aMessage);
	}

	/**
	 * methode de création d'un message a pertir d'une chaine de caracteres
	 * 
	 * @param text
	 *            la chaine de caracteres
	 * @return le message en question
	 */

	private Message createMessage(String text) {

		return new Message(pseudo, new Date(), text);
	}

}
