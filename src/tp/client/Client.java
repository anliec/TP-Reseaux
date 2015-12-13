/**
 * @filename Client.java
 */
package tp.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import example.hello.protocol.HelloItf;
import tp.protocol.Message;

/**
 * @author pllefebvre
 *
 */
public class Client {

	private ArrayList<Message> history;
	
	private Client(String args[]) {
		
		history = new ArrayList<Message>();
		
		String host = (args.length < 1) ? null : args[0];
		try {
            
        	Registry registry = LocateRegistry.getRegistry(host);
            HelloItf stub = (HelloItf) registry.lookup("Request1");
            String response = stub.sayHello();
            System.out.println("response: " + response);
        
        } catch (Exception e) {
            
        	System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client(args);
	}
	
	public void addMessage(Message aMessage) {
		
		history.add(aMessage);
	}

}
