package tp.socket;

import java.io.PrintStream;

/**
 * data struct used to keep together one client stream and is pseudo
 */
public class ClientDescriptor {

    private String clientPseudo;
    private PrintStream clientStream;

    /**
     * @param pseudo pseudo of the client
     * @param printStream stream of the client
     */
    public ClientDescriptor(String pseudo, PrintStream printStream){
        clientPseudo = pseudo;
        clientStream = printStream;
    }

    /**
     * @return the pseudo of the client
     */
    public String getPseudo() {
        return clientPseudo;
    }

    /**
     * @return the stream of the client
     */
    public PrintStream getStream() {
        return clientStream;
    }

}
