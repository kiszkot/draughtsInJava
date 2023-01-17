package draughts;

/**
 * 
 * The definition of a Man or checker to play checkers (draughts)
 *
 */

public class Man implements Piece {
	
	/** The color of the piece, "b" for black, "w" for white, "*" empty */
	protected String color; 
	
	/** Boolean to check if it is a king */
	protected boolean isKing = false; 
	
	public Man() {color = "";};
	
	/**
	 * Defines a man with the given color usually "b" for black "w" for white
	 * @param color The color of the Man
	 */
	public Man(String color) {
		this.color = color;
	}
	
	/**
	 * Returns a string with the color of the Man
	 * @return String
	 */
	public String toString() {
		return this.color;
	}
	
	/**
	 * Checks if the current Man is an empty square
	 * @return boolean
	 */
	public boolean isEmpty() {
		return (color.compareTo("*") == 0) ? true : false;
	}
	
	/**
	 * Checks if the current Man is a white piece
	 * @return boolean
	 */
	public boolean isWhite() {
		return (color.compareToIgnoreCase("w") == 0) ? true : false;
	}
	
	/**
	 * Checks if the current Man is a black piece
	 */
	public boolean isBlack() {
		return (color.compareToIgnoreCase("b") == 0) ? true : false;
	}
	
	/**
	 * Promotes the current piece
	 * @return King
	 */
	public King promote() {
		return new King(color.toUpperCase());
	}
	
	/**
	 * Checks if the piece is a King
	 * @return boolean
	 */
	public boolean isKing() {
		return isKing;
	}
	
}
