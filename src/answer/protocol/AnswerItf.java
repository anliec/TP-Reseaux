/**
 * 
 */

package answer.protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author pllefebvre
 *
 */
public interface AnswerItf extends Remote {
	
	String sayAnswer() throws RemoteException;
	String sayAnswer(String ans) throws RemoteException;
}
