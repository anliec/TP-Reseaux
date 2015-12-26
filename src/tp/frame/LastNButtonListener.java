/**
 * 
 */
package tp.frame;

import java.awt.event.ActionEvent;

/**
 * @author Pierre-Louis
 *
 */
public class LastNButtonListener extends ButtonListener {

	/**
	 * Constructeur de l'ecouteur du bouton "n derniers messages" de la fenetre de chat
	 * @param window la fenetre de chat
	 */
	public LastNButtonListener(DiscussionWindow window) {
		super(window);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		parent.dispLastN();
	}

}
