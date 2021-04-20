import java.awt.Color;
import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;

/**
 * This class builds the board for a game of "Connect Four".
 */
public class ConnectFourView extends GCompound implements View {
	/** The variable "rows" represents the number of rows in the board. */
	public static final int rows = 7;

	/** The variable "columns" represents the number of columns in the board. */
	public static final int columns = 6;

	/** The GRect "frame" represents the visual frame. */
	private final GRect frame;

	/**
	 * The GOval "startingPiece" represents the startingPiece at the top of the
	 * board.
	 */
	private final GOval startingPiece;

	/** The GLabel "label" represents the label at the bottom of the board. */
	private GLabel label;

	/** The Color "startingPieceColor" represents the color of the startingPiece. */
	private Color startingPieceColor = Color.YELLOW;

	/**
	 * The method "ConnectFourView" creates the Graphical view of the game.
	 */
	public ConnectFourView() {

		// Adds the starting token floating over the frame.
		startingPiece = new GOval(10, 15, 40, 40);
		startingPiece.setFilled(true);
		startingPiece.setFillColor(startingPieceColor);
		add(startingPiece);

		// Creates the blue frame.
		frame = new GRect(5, 60, 50 * rows + 5, 50 * columns);
		frame.setFilled(true);
		frame.setFillColor(Color.BLUE);
		add(frame);

		// create Label
		label = new GLabel("Player 1");
		label.setColor(Color.BLACK);
		label.setFont("SansSerif-36");
		add(label, 50 * (rows / 2) - 30, 50 * (columns + 2));

		// Puts white spaces for the play pieces into the frame depending on the amount
		// of rows and columns.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				double x = i * 50;
				double y = j * 50;
				GOval pieceFields = new GOval(x + 10, y + 65, 40, 40);
				pieceFields.setFilled(true);
				pieceFields.setFillColor(Color.WHITE);
				add(pieceFields);
			}
		}
	}

	/**
	 * The method "update" updates the whole graphical view to the new date from the
	 * model.
	 */
	@Override
	public void update(Model board) {
		// change the label if there is winner.
		if (board.getWin()) {
			label.setLabel("Player " + board.getValue(board.getNewPosX(), board.getNewPosY()) + " won!");
			label.setLocation(50 * (rows / 2) - 75, 50 * (columns + 2));
		}

		// Update the location of the starting piece.
		startingPiece.setLocation(board.getX() * 50 + 10, 15);

		// Create the new placed pieces.
		if (board.getCount() == 1) {
			GOval newPiece = new GOval(board.getNewPosX() * 50 + 10, board.getNewPosY() * 50 + 15, 40, 40);
			newPiece.setFilled(true);
			newPiece.setFillColor(startingPieceColor);
			add(newPiece);
		}

		// change the color of the startingPiece.
		if (board.getColor() == 1 && !board.getWin()) {
			startingPieceColor = Color.RED;
			label.setLabel("Player 1");
		} else if (board.getColor() == 2 && !board.getWin()) {
			startingPieceColor = Color.YELLOW;
			label.setLabel("Player 2");
		}

		startingPiece.setFillColor(startingPieceColor);
	}
}