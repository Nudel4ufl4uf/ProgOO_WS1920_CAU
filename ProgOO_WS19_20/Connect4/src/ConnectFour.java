import acm.program.GraphicsProgram;
/**
 * The class ConnectFour is the main Method of the connectFour game used to start the game.
 */
public class ConnectFour extends GraphicsProgram {

	/**
	 * The method "init" initializes the model and the views.
	 */
	public void init() {
		Model model = new Model(7,7);
		
		ConnectFourView View = new ConnectFourView();
		add(View);
		model.registerView(View);
		
		ConnectFourController Controller = new ConnectFourController(model);
		getGCanvas().addKeyListener(Controller);
	}
	
	public static void main(String[] args) {
		new ConnectFour().start();
	}
}
