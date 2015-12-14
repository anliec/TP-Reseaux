/**
 * @filename Client.java
 */
package tp.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
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

	private ArrayList<Message> history;
	private String pseudo;

	private Client() {

		history = new ArrayList<Message>();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client = new Client();
		
		String host = (args.length < 1) ? null : args[0];
		try {
            
        	Registry registry = LocateRegistry.getRegistry(host);
            Reception reception = new Reception(client);
            ReceptionItf receptionStub = (ReceptionItf) UnicastRemoteObject.exportObject(reception, 0);
            
            //connect to the server itf
            RequestItf requestStub = (RequestItf) registry.lookup("Request1");
            
            Scanner scanner = new Scanner(System.in);
            
            //procedure d'inscription
    		System.out.println("choisissez un pseudo :");
    		client.pseudo = scanner.nextLine();
    		
    		requestStub.login(receptionStub);
    		
    		// systeme de commande d'envoi de message --  a terminer
    		String cmd = "";
    		boolean on = true;
    		while(on) {
    			
    			System.out.println("interface :");
    			cmd = scanner.nextLine();
    			
    			switch (cmd) {
    				
    				case "/m" :
    					
    					cmd = scanner.nextLine();
    					requestStub.send(client.createMessage(cmd));
    					break ;
    					
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

		history.add(aMessage);
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
