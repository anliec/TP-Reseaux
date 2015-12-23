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
		FileReader fr;
		
		try {
			
			fr = new FileReader(histoFile);
			
			int i = 0;
			String client = "";
			String date = "";
			String message = "";
			int select = 0;
			
			while((i = fr.read()) != -1) {
				
				if((char)i != '|') {
					
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
							
						case 3 :
						
							history.add(new Message(client, new Date(Long.parseLong(date)), message));
							message = "";
							date = "";
							client = "";
							select = 0;
							break;
							
						default :
							break;
					} //fin switch select
					
				} else {
					
					select++;
				} //fin si i != '|'
			} //fin while fr.read();
			
			fr.close();
		
		} catch (FileNotFoundException e) {
		      
			e.printStackTrace();
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return history;
	}
	
	public static void saveHistory(String file, LinkedList<Message> history) {
		
		File histoFile = new File(file);
		FileWriter fw;
		
		try {
			
			fw = new FileWriter(histoFile);
			
			ListIterator<Message> iter = history.listIterator();
			Message next;
			String str = "";
			while(iter.hasNext()) {
				
				next = iter.next();
				str += next.getIdClient();
				str += "|";
				str += String.valueOf(next.getDate().getTime());
				str += "|";
				str += next.getMessage();
				
			}//fin while iter.hasNext();
			
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
