package tp.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import tp.protocol.Message;

/**
 * @filename FileGesture.java
 */

/**
 * @author pllefebvre
 *
 */
public abstract class FileGesture {
	
	public static LinkedList<Message> loadHistory(String file) {
		
		File histoFile = new File(file);
		LinkedList<Message> history = new LinkedList<Message>();
		
		try {
			
			FileReader fr = new FileReader(histoFile);
			
			int i = 0;
			ListIterator<Message> iter = history.listIterator();
			String client = "";
			String date = "";
			long timeDate = 0;
			String Message = "";
			int select = 0;
			
			while((i = fr.read()) != -1) {
				
				if((char)i != '|') {
					
					
				}
			}
		
		} catch (FileNotFoundException e) {
		      
			e.printStackTrace();
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return history;
	}

}
