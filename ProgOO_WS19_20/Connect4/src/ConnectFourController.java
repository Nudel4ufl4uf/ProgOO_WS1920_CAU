import java.awt.event.*;

/**
 * The class "ConnectFourController" performs the actions which are perfomed by
 * the user by the keyboard input.
 */
public class ConnectFourController implements KeyListener {
	/** The world that should be updated */
	private Model model;

	/**
	 * The Constructor calls the initializer method and saves the value of the
	 * entered key.
	 */
	public ConnectFourController(Model model) {
		this.model = model;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * The method "keyPressed" performs the actions depending on the keyboard input.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_RIGHT:
			model.moveRight();
			break;

		case KeyEvent.VK_LEFT:
			model.moveLeft();
			break;

		case KeyEvent.VK_ENTER:

			model.place();
			if (model.check()) {
				// Stop game and place GLabel with "YOU WIN"

			}
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
