package draughts;

/**
 * Class describing functionality of a King an extension of class Man
 * 
 *
 */

public class King extends Man implements Piece {
	
	/**
	 * Constructor for king
	 * @param color The color of the King
	 */
	public King(String color) {
		this.color = color.toUpperCase();
		this.isKing = true;
	}
	
	/**
	 * Checks if the current King is a white piece
	 * @return boolean
	 */
	public boolean isWhite() {
		return (color.compareToIgnoreCase("w") == 0) ? true : false;
	}
	
	/**
	 * Checks if the current King is a black piece
	 */
	public boolean isBlack() {
		return (color.compareToIgnoreCase("b") == 0) ? true : false;
	}
	
	/**
	 * Returns a string with the color of the Man
	 * @return String
	 */
	public String toString() {
		return this.color;
	}
	
}
