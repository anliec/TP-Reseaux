/**
 * @filename Reception.java
 */
package tp.client;

import java.rmi.RemoteException;

import tp.protocol.Message;
import tp.protocol.ReceptionItf;

/**
 * @author pllefebvre
 * Classe de generation d'interface de reception d'un client
 */

public class ReceptionCore implements ReceptionItf {

    private ClientCore client;
    /**
     * Constructeur par de la reception du client
     * @param client le client dont c'est la reception
     */
    public ReceptionCore(ClientCore client) {
        this.client = client;
    }

    /* (non-Javadoc)
     * @see tp.protocol.ReceptionItf#receive(tp.protocol.Message)
     */
    public void receive(Message aMessage) throws RemoteException {
        client.addMessage(aMessage);
    }

}
