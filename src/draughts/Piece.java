package draughts;

/**
 * Interface that allows creation of arrays with both Men and Kings
 * 
 */

public interface Piece {
	/**
	 * Converts the piece to String
	 * @return String
	 */
	public String toString();
	
	/**
	 * Check if the piece is an empty piece defined by "*".
	 * @return boolean
	 */
	public boolean isEmpty();
	
	/**
	 * Check if the piece is a white piece defined by "w" or "W".
	 * @return boolean
	 */
	public boolean isWhite();
	
	/**
	 * Check if the piece is a black piece defined by "b" or "B".
	 * @return boolean
	 */
	public boolean isBlack();
	
	/**
	 * Promotes the piece to a King
 	 * @return King
	 */
	public King promote();
	
	/**
	 * Checks if the piece is a King
	 * @return boolean
	 */
	public boolean isKing();
}
