/**
 * 
 */
package tp.frame.client;

import java.awt.event.ActionEvent;

/**
 * @author Pierre-Louis
 *
 */
public class SendButtonListener extends ButtonListener {

	/**
	 * @param window
	 */
	public SendButtonListener(DiscussionWindow window) {
		super(window);
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		parent.send();
	}

}
