/**
 * 
 */
package tp.frame;

import java.awt.event.ActionListener;

/**
 * @author Pierre-Louis
 * 
 */
public abstract class ButtonListener implements ActionListener {

	protected DiscussionWindow parent;

	/**
	 * Constructeur par defaut d'un ecouteur de bouton de la fenetre de chat
	 * client
	 * 
	 * @param window
	 *            la fenetre
	 */

	public ButtonListener(DiscussionWindow window) {
		// TODO Auto-generated constructor stub
		this.parent = window;
	}

}
