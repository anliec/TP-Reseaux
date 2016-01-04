package tp.server;

import tp.protocol.ReceptionItf;

/**
 * Created by nicolas on 03/01/16.
 * Class which define a client descriptor for the RMI Server
 */

public class RmiClientDescriptor {
	
	private ReceptionItf reception;
	private String pseudo;
	
	/**
	 * RMI Client Descriptor parameterized constructor
	 * 
	 * @param aReception
	 *            the instance of ReceptionItf associated to the client
	 * @param clientPseudo
	 *            the client pseudo (String)
	 */
	
	public RmiClientDescriptor(ReceptionItf aReception, String clientPseudo) {
		reception = aReception;
		pseudo = clientPseudo;
	}
	
	/**
	 * @return the client Reception Interface described by the current instance
	 */
	
	public ReceptionItf getReception() {
		return reception;
	}
	
	/**
	 * @return the client pseudo described by the current instance
	 */
	
	public String getPseudo() {
		return pseudo;
	}
}
