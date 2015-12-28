package tp.socket;

import java.io.PrintStream;

/**
 * Created by nicolas on 28/12/15.
 */
public class ClientDescriptor {

    private String clientPseudo;
    private PrintStream clientStream;

    public ClientDescriptor(String pseudo, PrintStream printStream){
        clientPseudo = pseudo;
        clientStream = printStream;
    }

    public String getPseudo() {
        return clientPseudo;
    }

    public PrintStream getStream() {
        return clientStream;
    }

}
