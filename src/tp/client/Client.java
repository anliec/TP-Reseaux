/**
 * @filename Client.java
 */
package tp.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Date;

import tp.frame.client.DiscussionWindow;
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
	private DiscussionWindow window;

	/**
	 * 
	 * @param args
	 */

	public Client(String[] args) {

		String host = (args.length < 1) ? null : args[0];
		window = new DiscussionWindow("RMI Chat Program", this);

		try {

			registry = LocateRegistry.getRegistry(host);
			// connection a l'interface serveur
			requestStub = (RequestItf) registry.lookup("Request1");

		} catch (Exception e) {

			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */

	public void run() {

		try {
			Scanner scanner = new Scanner(System.in);

			// procedure d'inscription
			System.out.println("choisissez un pseudo :");
			pseudo = scanner.nextLine();

			Reception reception = new Reception(this);
			receptionStub = (ReceptionItf) UnicastRemoteObject.exportObject(reception, 0);
			requestStub.login(receptionStub);
			System.out.println("connecte, \n affichage des 10 derniers messages :");
			{
				Message[] last = lastN(10);
				for (Message message : last) {
					if (message != null)
						System.out.println(message);
				}
				window.dispLastN(last);
			}

			// systeme de commande d'envoi de message
			String cmd = "";
			boolean on = true;
			while (on) {

				System.out.println("interface :");
				cmd = scanner.nextLine();

				switch (cmd) {

					case "/m":

						System.out.println("Message :");
						cmd = scanner.nextLine();
						send(cmd);
						break;

					case "/l":

						System.out.println("nombre de messages a afficher :");
						int n = scanner.nextInt();
							Message[] last = lastN(n);
						for(Message message : last) {
							if(message != null) System.out.println(message);
						}
									
						break;

					case "/q":

						scanner.close();

						// quand le client quitte le chat, il est retire de la
						// liste du server
						requestStub.logout(receptionStub);
						on = false;
						break;

					default:

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

		window.addMessage(aMessage);
		System.out.println(aMessage);
	}

	/**
	 * methode de cr�ation d'un message a pertir d'une chaine de caracteres
	 * 
	 * @param text
	 *            la chaine de caracteres
	 * @return le message en question
	 */

	private Message createMessage(String text) {

		return new Message(pseudo, new Date(), text);
	}

	/**
	 * le client dont on appelle cette methode envoie un message donne en
	 * paramtre sous forme d'une String vers le serveur auquel il est connecte
	 * 
	 * @param string
	 *            le message
	 */

	public void send(String string) {

		try {
			requestStub.send(createMessage(string));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * methode qui retourne les n derniers messages de la conversation
	 * @param n
	 * @return le tableau des n derniers messages
	 */
	
	public Message[] lastN(int n){
		
		Message[] last = new Message[0];
		
		try {
			
			last = requestStub.lastN(n);
		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return last;
	}
}
