/**
 * @filename Client.java
 */
package tp.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
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
public class ClientCore {

    private String pseudo;
    private Registry registry;
    private ReceptionItf receptionStub;
    private RequestItf requestStub;
    private LinkedList<Message> history = new LinkedList<>();


    public ClientCore(String clientPseudo, String hostIp) throws Exception {
        pseudo = clientPseudo;
        //try {
            registry = LocateRegistry.getRegistry(hostIp);
            // connection a l'interface serveur
            requestStub = (RequestItf) registry.lookup("Request1");

            ReceptionCore reception = new ReceptionCore(this);
            receptionStub = (ReceptionItf) UnicastRemoteObject.exportObject(reception, 0);
            requestStub.login(receptionStub,pseudo);

            //affichage des 10 derniers messages
            Message[] last = lastN(10);
            for (Message message : last) {
                if (message != null)
                    history.add(message);
                else
                    break;
            }

        /*} catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }*/

    }

    public void quit(){
        try {
            // quand le client quitte le chat, il est retire de la
            // liste du server
            requestStub.logout(receptionStub,pseudo);
        }
        catch (Exception e){
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
     * @param string le message
     */
    public void send(String string){
        send(createMessage(string));
    }

    /**
     * le client dont on appelle cette methode envoie un message donne en
     * paramtre sous forme d'une String vers le serveur auquel il est connecte
     *
     * @param message le message
     */
    public void send(Message message) {
        try {
            requestStub.send(message);
        } catch (RemoteException e) {
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

            last = requestStub.lastN(n,pseudo);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return last;
    }

    public LinkedList<Message> getHistory(){
        return history;
    }

    public String getPseudo(){
        return pseudo;
    }
}
