/**
 * @filename MainServer.java
 */
package tp.server;

/**
 * @author pllefebvre
 * Main class of the server program, create and run a server on the port 1099
 */
public class MainServer {

	/**
	 * Main method of the server program
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
		server.run();
		
	}

}
