/**
 * @filename Client.java
 */
package tp.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;
import tp.protocol.RequestItf;

/**
 * @author pllefebvre
 *
 */
public class Client {

	private ArrayList<Message> history;
	
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
    		String pseudo = "";
    		int islogin = 0;
    		while(islogin == 0)
    		{
    		
    			System.out.println("choisissez un pseudo :");
    			pseudo = scanner.nextLine();
    			islogin = requestStub.login(pseudo); // a terminer pour tester si le pseudo est valide
    			registry.bind(pseudo, receptionStub); // relie au registre l'interface client avec son pseudo correspondant
    		}
    		
    		// systeme de commande d'envoi de message --  a terminer
    		String cmd = "";
    		while(cmd != "quit") {
    			
    			System.out.println("interface :");
    			cmd = scanner.nextLine();
    			
    			switch (cmd) {
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
	 * Methode qui enregistre le message passse en parametre dans l'historique et l'affiche
	 * @param aMessage le message en question
	 */
	public void addMessage(Message aMessage) {
		
		history.add(aMessage);
		System.out.println(aMessage);
	}

}
