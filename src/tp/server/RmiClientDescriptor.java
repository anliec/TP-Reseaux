package tp.server;

import tp.protocol.ReceptionItf;

/**
 * Created by nicolas on 03/01/16.
 */
public class RmiClientDescriptor {
    private ReceptionItf reception;
    private String pseudo;

    public RmiClientDescriptor(ReceptionItf aReception, String clientPseudo){
        reception = aReception;
        pseudo = clientPseudo;
    }

    public ReceptionItf getReception(){
        return reception;
    }

    public String getPseudo(){
        return pseudo;
    }
}
