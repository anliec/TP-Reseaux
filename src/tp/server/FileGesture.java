/**
 * @filename FileGesture.java
 */

package tp.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import tp.protocol.Message;

/**
 * @author pllefebvre Abstract
 *         Class which contains methods to load and save messages history from/into a file
 */

public abstract class FileGesture {
	
	private final static char SEP = '|';
	
	/**
	 * method which loads a messages list in a LinkedList of Message, from a
	 * file in which a messages list was saved by the method saveHistory(String
	 * file, LinkedList<Message> history)
	 * 
	 * @param file
	 *            a String which contains the name of the file
	 * @return a LinkedList<Message> which contains all the messages originally
	 *         saved in the file
	 */
	
	public static LinkedList<Message> loadHistory(String file) {
		
		File histoFile = new File(file);
		LinkedList<Message> history = new LinkedList<Message>();
		FileReader fr;
		
		try {
			
			fr = new FileReader(histoFile);
			
			int i = 0;
			String client = "";
			String date = "";
			String message = "";
			int select = 0;
			
			while ((i = fr.read()) != -1) {
				
				if ((char) i != SEP) {
					
					switch (select) {
					
						case 0 :
							
							client += (char) i;
							break;
						
						case 1 :
							
							date += (char) i;
							break;
						
						case 2 :
							
							message += (char) i;
							break;
						
						default :
							break;
					} // fin switch select
					
				} else {
					
					if (select < 2) {
						
						select++;
						
					} else {
						
						history.add(new Message(client, new Date(Long
						        .parseLong(date)), message));
						message = "";
						date = "";
						client = "";
						select = 0;
					}
				} // fin si i != '|'
			} // fin while fr.read();
			
			fr.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("log file: "+file+" didn't exist");
			e.printStackTrace();
			System.out.println(file+" created");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return history;
	}
	
	/**
	 * Method which save a messages list (LinkedList<Messages>) in a file whose
	 * name is given in parameters
	 * 
	 * @param file
	 *            the name of the file
	 * @param history
	 *            the LinkedList of Messages
	 */
	
	public static void saveHistory(String file, LinkedList<Message> history) {
		
		File histoFile = new File(file);
		FileWriter fw;
		
		try {
			
			fw = new FileWriter(histoFile);
			
			ListIterator<Message> iter = history.listIterator();
			Message next;
			String str = "";
			while (iter.hasNext()) {
				
				next = iter.next();
				str += next.getIdClient();
				str += SEP;
				str += String.valueOf(next.getDate().getTime());
				str += SEP;
				str += next.getMessage();
				str += SEP;
				
			}// fin while iter.hasNext();
			
			fw.flush();
			fw.write(str);
			fw.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
