import java.util.HashSet;
import java.util.Set;

/**
 * The class Model keeps all the methods and date of the game ConnectFour.
 */
public class Model {
	/**
	 * The class field "field" is there to save the position of the tokens. 6x6 to
	 * include the top row of the moving token.
	 */
	private int[][] field;
	/**
	 * The variable "color" is there to save the color of the token. 1 = yellow 2 =
	 * red
	 */
	private int color = 1;

	/** The variable "count" is used to check if the given method was run. */
	private int count = 0;

	/** The variable "win" is true if there is a winning constellation. */
	private boolean win = false;

	/**
	 * The variable "playerX" represents the x-coordinate of the moving piece at the
	 * top.
	 */
	private int playerX = 0;

	/** The variable "newPieceX" is the x-coordinate of the new placed piece. */
	private int newPieceX = 0;

	/** The variable "newPieceY" is the y-coordinate of the new placed piece. */
	private int newPieceY = 0;

	/** Set of views registered to be notified of world updates. */
	private final Set<ConnectFourView> views = new HashSet<>();

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	/**
	 * The Constructor of the class model initializes the field with the given size.
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Model(int x, int y) {
		if (x < 4 && y < 4) {
			throw new IllegalArgumentException("The given Size is to small to play");
		}
		this.field = new int[x][y];
		// initializes the startingPiece in the data field.
		field[0][0] = 1;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	/**
	 * The method "moveRight" moves the startPiece to the right.
	 */
	public void moveRight() {
		int pos = playerX;

		// if the token is at the top right position it should be moving to the top left
		// position.
		if (playerX == 6) {
			field[0][0] = field[6][0];
			field[6][0] = 0;
			playerX -= 6;
		}
		// else move it one cordinate to the right.
		else {
			field[pos + 1][0] = field[pos][0];
			field[pos][0] = 0;
			playerX += 1;
		}
		// reset the count to avoid the "place" method to be triggered in the "update"
		// method of the view.
		count = 0;
		updateViews();
	}

	/**
	 * The method moveLeft moves the startPiece to the left.
	 */
	public void moveLeft() {
		int pos = playerX;

		// if the token is at the top left position it should be moving to the top right
		// position.
		if (pos == 0) {
			field[6][0] = field[0][0];
			field[0][0] = 0;
			playerX += 6;
		}
		// else move the it one coordinate to the left.
		else {
			field[pos - 1][0] = field[pos][0];
			field[pos][0] = 0;
			playerX -= 1;
		}
		// reset the count to avoid the "place" method to be triggered in the "update"
		// method of the view.
		count = 0;
		updateViews();
	}

	/**
	 * The method place places the token at the next possible position of the
	 * column.
	 */
	public void place() {
		int posX = playerX;

		// Check if the column is already full.
		if (field[posX][1] != 0) {
			throw new IllegalArgumentException("This column is already full!");
		} else {
			// update the coordinates of the possible position in the grid.
			newPieceX = posX;
			newPieceY = searchPlaceY(posX);

			// place the new piece in the data field.
			placeAt(posX, searchPlaceY(posX), color);

			// change the color code for the next token.
			if (color == 1) {
				field[posX][0] = 2;
				color = 2;
			} else if (color == 2) {
				field[posX][0] = 1;
				color = 1;
			}

			// check the combinations.
			if (check()) {
				// fill the top layer in the data field to block the next input.
				for (int i = 0; i < 7; i++) {
					field[i][1] = color;
				}
				win = true;
			}
		}
		// set count to 1 to trigger the update method to place the new piece in the
		// view.
		count = 1;
		updateViews();
	}

	/**
	 * The method searchPlaceY searches the y-coordinate of the next free spot in
	 * the column.
	 * 
	 * @param x of the x-coordinate
	 * @return the value of the y-coordinate
	 */
	public int searchPlaceY(int x) {
		// checks the column of the x position of the starting piece on the next free
		// position on top of another piece.
		for (int i = 2; i < 7; i++) {
			if (field[x][i] == 1 || field[x][i] == 2) {
				return i - 1;
			}
		}
		return 6;
	}

	/**
	 * Checks all combinations on winning streaks of four.
	 */
	public boolean check() {
		if (checkRow() || checkColumn() || checkDiagRL() || checkDiagLR()) {
			return true;
		}
		return false;
	}

	/**
	 * The Method checkRow checks if there is a wining streak of four in the rows.
	 * 
	 * @return true if there is a winning streak.
	 */
	public boolean checkRow() {
		// counters for each colors to check on the winning streak.
		int counterRed = 0;
		int counterYellow = 0;

		for (int i = 1; i < 7; i++) {
			for (int k = 0; k < 7; k++) {
				// if there is a yellow token the streak of the red token is interrupted or the
				// yellow streak continues.
				if (field[k][i] == 1) {
					counterYellow += 1;
					counterRed = 0;
				}
				// if there is a red token the streak of the yellow token is interrupted or the
				// red streak continues.
				else if (field[k][i] == 2) {
					counterRed += 1;
					counterYellow = 0;
				}
				// if there is a a column which is not filled until this row there is no
				// connection between the tokens.
				else if (field[k][i] == 0) {
					// reset both counters.
					counterRed = 0;
					counterYellow = 0;
				}
				// if one counter is equals 4 there is a winning streak.
				if (counterRed == 4 || counterYellow == 4) {
					System.out.println("win1");
					return true;
				}
			}
			//reset the counters if one row is checked and a new one begins.
			counterYellow = 0;
			counterRed = 0;
		}
		return false;
	}

	/**
	 * The method checkColumn checks a column on a winning Streak of four.
	 * 
	 * @return true if there is a winning streak.
	 */
	public boolean checkColumn() {
		// counters for each colors to check on the winning streak.
		int counterRed = 0;
		int counterYellow = 0;

		for (int i = 0; i < 7; i++) {
			for (int k = 1; k < 7; k++) {
				// if there is a yellow token the streak of the red token is interrupted or the
				// yellow streak continues.
				if (field[i][k] == 1) {
					counterYellow += 1;
					counterRed = 0;
				}
				// if there is a red token the streak of the yellow token is interrupted or the
				// red streak continues.
				else if (field[i][k] == 2) {
					counterRed += 1;
					counterYellow = 0;
				}
				// if one counter is equals 4 there is a winning streak.
				if (counterRed == 4 || counterYellow == 4) {
					System.out.println("win2");
					return true;
				}
			}
			// reset both counters if the column is checked and a new one begins.
			counterRed = 0;
			counterYellow = 0;
		}
		return false;
	}

	/**
	 * The method checks if there is a wining streak of 4 in a diagonal form from
	 * left upper corner to the bottom right corner.
	 * 
	 * @return
	 */
	public boolean checkDiagRL() {
		// counters for each colors to check on the winning streak.
		int counterRed = 0;
		int counterYellow = 0;

		for (int i = 0; i < 7; i++) {
			for (int k = 1; k < 7; k++) {
				for (int t = 0; t < 7 - i && t < 7 - k; t++) {
					// move to the next diagonal position.
					if (field[i + t][k + t] == 1) {
						counterRed += 1;
						counterYellow = 0;
					} else if (field[i + t][k + t] == 2) {
						counterYellow += 1;
						counterRed = 0;
					}
					// return a win situation if there is streak of four.
					if (counterRed == 4 || counterYellow == 4) {
						System.out.println("win3");
						return true;
					}
				}
				// reset the counters if one diagonal is checked.
				counterRed = 0;
				counterYellow = 0;
			}
		}
		return false;
	}

	/**
	 * The method checks if there is a wining streak of 4 in a diagonal form from
	 * right upper corner to the bottom left corner.
	 * 
	 * @return
	 */
	public boolean checkDiagLR() {
		// counters for each colors to check on the winning streak.
		int counterRed = 0;
		int counterYellow = 0;

		for (int i = 6; i >= 0; i--) {
			for (int k = 1; k < 7; k++) {
				for (int t = 0; t <= i && t < 7 - k; t++) {
					// move to the next diagonal position.
					if (field[i - t][k + t] == 1) {
						counterRed += 1;
						counterYellow = 0;
					} else if (field[i - t][k + t] == 2) {
						counterYellow += 1;
						counterRed = 0;
					}
					// return a win situation if there is a streak of four.
					if (counterRed == 4 || counterYellow == 4) {
						System.out.println("win4");
						return true;
					}
				}
				// reset the counters if one diagonal is checked.
				counterRed = 0;
				counterYellow = 0;
			}
		}
		return false;
	}

	/**
	 * The method placeAt places the token at the given position in the array.
	 * 
	 * @param x
	 * @param y
	 * @param color
	 */
	public void placeAt(int x, int y, int color) {
		field[x][y] = color;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	// View Control
	/**
	 * The method registerView adds the given view and updates it one time. If the
	 * view is registered it will always receive the new updates and adapt.
	 *
	 * @param view the view to be registered
	 */
	public void registerView(ConnectFourView view) {
		views.add(view);
		view.update(this);
	}

	/**
	 * Updates all the changes of the new view.
	 */
	public void updateViews() {
		for (ConnectFourView view : views) {
			view.update(this);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	// Getter
	/**
	 * Getter for the Position of the start piece.
	 * 
	 * @return pos position of the StartPiece
	 */
	public int getPosStartPiece() {
		int pos = 0;
		// Check if the startPiece is at the right Border.
		for (int i = 0; i <= 6; i++) {
			if (field[0][i] != 0) {
				pos = i;
			}
		}
		return pos;
	}

	/**
	 * Getter for the color of the new token.
	 * 
	 * @return color color of the token.
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Getter for the count which checks if a token was placed.
	 * 
	 * @return count 
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Getter for the x-coordinate of the new token.
	 * 
	 * @return newPieceX 
	 */
	public int getNewPosX() {
		return newPieceX;
	}

	/**
	 * Getter for the y-coordinate of the new token.
	 * 
	 * @return
	 */
	public int getNewPosY() {
		return newPieceY;
	}

	/**
	 * Getter for the x-coordinate of the new StartPiece.
	 * 
	 * @return playerX
	 */
	public int getX() {
		return playerX;
	}

	/**
	 * Getter for the win situation.
	 * 
	 * @return win 
	 */
	public boolean getWin() {
		return win;
	}

	/**
	 * Getter for any value at any position of the field.
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return value at the given coordinates of the field.
	 */
	public int getValue(int x, int y) {
		return field[x][y];
	}
}