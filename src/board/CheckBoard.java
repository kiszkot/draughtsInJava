package board;

import java.util.HashMap;
import draughts.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Class with standard 8x8 board functionality
 */

public class CheckBoard {
	
	/** Board dimensions */
	private int dimension = 8; 
	
	/** Number of Black pieces */
	private int blacks = 12;
	
	/** Number of White pieces */
	private int whites = 12;
	
	/** Board squares */
	private Piece[] squares = new Piece[32]; 
	
	/** List of possible ways for a piece to take */
	private List<Integer[]> waysList = new ArrayList<Integer[]>(); 
	
	/** List of jumped piece by each possible way */
	private List<Integer[]> jumpedList = new ArrayList<Integer[]>(); 
	
	/** Map to choose which way to take */
	private HashMap<Integer,Integer> take = new HashMap<Integer,Integer>(); 
	
	/**
	 * Constructor for basic 8x8 board.
	 */
	public CheckBoard() {
		for(int i=0; i<32; i++) {
			if(i < 12) squares[i] = new Man("b");
			else if(i >= 20) squares[i] = new Man("w");
			else squares[i] = new Man("*");
		}
	}
	
	/**
	 * Gets the dimensions of the board
	 * @return int
	 */
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * Function to return the winner, only accounts for number of pieces
	 * @return String
	 */
	public String whoWins() {
		if(whites == 0)	return "Blacks";
		else if(blacks == 0) return "Whites";
		else return "";
	}
	
	/**
	 * Converts the board to a String representation to display when playing.
	 * @return String
	 */
	@Override
	public String toString() {
		String ret = "";
		for(int i=0; i<dimension; i++) {
			if((i+1)%2 != 0) {
				for(int j=4*i; j<4*(i+1); j++) {
					ret = ret + "\u2591" + squares[j].toString();
				}
				ret = ret + "\n";
			}
			else {
				for(int j=4*i; j<4*(i+1); j++) {
					ret = ret + squares[j].toString() + "\u2591";
				}
				ret = ret + "\n";
			}
		}
		return ret;
	}
	
	/**
	 * Moves the selected square to the selected position, takes pieces in the way and promotes a piece if can be promoted.
	 * Invalid moves are taken care of in another function.
	 * @param from Position to move
	 * @param to Position where to move
	 */
	public void move(int from, int to) {
		Piece tmp = squares[from-1];
		squares[from-1] = squares[to-1];
		squares[to-1] = tmp;
		
		if(take.containsKey(to)) {
			Integer[] t = jumpedList.get(take.get(to));
			if(tmp.isBlack()) whites -= t.length;
			else if(tmp.isWhite()) blacks -= t.length;
			for(int i=0; i<t.length; i++) {
				squares[t[i]-1] = new Man("*");
			}
		}
		
		int line = (int)(to-1)/4;
		if(line == 7 && tmp.isBlack()) {
			squares[to-1] = tmp.promote();
		} else if(line == 0 && tmp.isWhite()) {
			squares[to-1] = tmp.promote();
		}
	}
	
	/**
	 * Returns an array of possible moves given a starting position.
	 * @param man The starting position
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 * @return String[]
	 */
	public String[] possibleMoves(int man, int player) {
		
		List<String> ret = new ArrayList<String>();
		take.clear();
		
		if(squares[man-1].isKing()) {
			possibleKingMoves(player, man, ret);
			return ret.toArray(new String[0]);
		}
		
		int line = (int)((man-1)/4);
		int tmpN = 0;
		
		Piece to = new Man();
		
		if(checkAdjacentEnemies(man, player)) {
			combo(man, player, ret);
			
			for(int i = 0; i < waysList.size(); i++) {
				ret.add(String.valueOf(waysList.get(i)[waysList.get(i).length - 1]));
				take.put(waysList.get(i)[waysList.get(i).length - 1], i);
			}
			
		} else {
			if(player == 1) { //white
				if((line+1)%2 == 0) { //even
					to = squares[man-4 - 1];
					tmpN = (int)(man-4 - 1)/4;
					if((tmpN == line+1 || tmpN == line -1) && tmpN < 8 && tmpN >= 0) {
						if(to.isEmpty()) ret.add(String.valueOf(man-4)); 
					}
					to = squares[man-5 - 1];
					tmpN = (int)(man-5 - 1)/4;
					if((tmpN == line+1 || tmpN == line -1) && tmpN < 8 && tmpN >= 0) {
						if(to.isEmpty()) ret.add(String.valueOf(man-5)); 
					}
				} else { //odd
					to = squares[man-4 - 1];
					tmpN = (int)(man-4 - 1)/4;
					if((tmpN == line+1 || tmpN == line -1) && tmpN < 8 && tmpN >= 0) {
						if(to.isEmpty()) ret.add(String.valueOf(man-4)); 
					}
					to = squares[man-3 - 1];
					tmpN = (int)(man-3 - 1)/4;
					if((tmpN == line+1 || tmpN == line -1) && tmpN < 8 && tmpN >= 0) {
						if(to.isEmpty()) ret.add(String.valueOf(man-3)); 
					}
				}
			} else { //black
				if((line+1)%2 == 0) { //even
					to = squares[man+4 - 1];
					tmpN = (int)(man+4 - 1)/4;
					if((tmpN == line+1 || tmpN == line -1) && tmpN < 8 && tmpN >= 0) {
						if(to.isEmpty()) ret.add(String.valueOf(man+4)); 
					}
					to = squares[man+3 - 1];
					tmpN = (int)(man+3 - 1)/4;
					if((tmpN == line+1 || tmpN == line -1) && tmpN < 8 && tmpN >= 0) {
						if(to.isEmpty()) ret.add(String.valueOf(man+3)); 
					}
				} else { //odd
					to = squares[man+4 - 1];
					tmpN = (int)(man+4 - 1)/4;
					if((tmpN == line+1 || tmpN == line -1) && tmpN < 8 && tmpN >= 0) {
						if(to.isEmpty()) ret.add(String.valueOf(man+4)); 
					}
					to = squares[man+5 - 1];
					tmpN = (int)(man+5 - 1)/4;
					if((tmpN == line+1 || tmpN == line -1) && tmpN < 8 && tmpN >= 0) {
						if(to.isEmpty()) ret.add(String.valueOf(man+5)); 
					}
				}
			}
		}
		return ret.toArray(new String[0]);
	}
	
	/**
	 * Returns an array of possible move for a given position given that the piece is a King
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 * @param position The starting position
	 * @param ret The list where to add the possible positions
	 */
	private void possibleKingMoves(int player, int position, List<String> ret) {
		Piece to = new Man();
		int line = (int)(position-1)/4;
		int tmpN = 0, tmpLine = 0, pos = 0;
		
		if(checkAdjacentEnemiesK(position, player)) {
			combo(position, player, ret);
			
			for(int i = 0; i < waysList.size(); i++) {
				ret.add(String.valueOf(waysList.get(i)[waysList.get(i).length - 1]));
				take.put(waysList.get(i)[waysList.get(i).length - 1], i);
			}
			
		} else {
			
			if(player == 1) { //white
				if((line + 1)%2 == 0) { //even
					tmpN = (int)((position+4)-1)/4;
					tmpLine = line; pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos+4 < 33) to = squares[pos+4 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
						tmpLine = tmpN;
						if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
						else { to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position+3)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos+3 < 33) to = squares[pos+3 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
						tmpLine = tmpN;
						if((tmpN+1)%2 == 0) { to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;}
						else { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position-4)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos-4 > 0) to = squares[pos-4 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
						if((tmpN + 1)%2 == 0) {
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-4 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-4)-1)/4;
							pos = pos-4;
						}
						else { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-3 > 0) to = squares[pos-3 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-3)-1)/4;
							pos = pos-3;
						}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position-5)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos-5 > 0) to = squares[pos-5 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos > 0 && !to.isWhite()) {
						if((tmpN + 1)%2 == 0) {
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-5 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-5)-1)/4;
							pos = pos-5;
						}
						else { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-4 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-4)-1)/4;
							pos = pos-4;
						}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
				} else { //odd
					tmpN = (int)((position+4)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos+4 < 33) to = squares[pos+4 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine-1 || tmpN == tmpLine+1) && tmpN < 8 && !to.isWhite()) {
						tmpLine = tmpN;
						if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
						else { to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos + 3;}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position+5)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos+5 < 33) to = squares[pos+5 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine-1 || tmpN == tmpLine+1) && tmpN < 8 && !to.isWhite()) {
						tmpLine = tmpN;
						if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
						else { to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position-4)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos-4 > 0) to = squares[pos-4 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos > 0 && !to.isWhite()) {
						if((tmpN + 1)%2 == 0) { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-5 > 0) to = squares[pos-5 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-5)-1)/4;
							pos = pos-5;
						}
						else { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-4 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-4)-1)/4;
							pos = pos-4;
						}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position-3)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos-3 > 0) to = squares[pos-3 - 1];
					else to = new Man(" ");
					while((tmpN == line-1 || tmpN == line+1) && pos > 0 && !to.isWhite()) {
						if((tmpN + 1)%2 == 0) { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-4 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-4)-1)/4;
							pos = pos-4;
						}
						else { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-3 > 0) to = squares[pos-3 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-3)-1)/4;
							pos = pos-3;
						}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}

				}
			} else { //black
				if((line + 1)%2 == 0) { //even
					tmpN = (int)((position+4)-1)/4;
					tmpLine = line; pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos+4 < 33) to = squares[pos+4 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
						tmpLine = tmpN;
						if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
						else { to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position+3)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos+3 < 33) to = squares[pos+3 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
						tmpLine = tmpN;
						if((tmpN+1)%2 == 0) { to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;}
						else { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position-4)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos-4 > 0) to = squares[pos-4 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
						if((tmpN + 1)%2 == 0) {
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-4 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-4)-1)/4;
							pos = pos-4;
						}
						else { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-3 > 0) to = squares[pos-3 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-3)-1)/4;
							pos = pos-3;
						}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position-5)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos-5 > 0) to = squares[pos-5 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos > 0 && !to.isBlack()) {
						if((tmpN + 1)%2 == 0) {
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-5 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-5)-1)/4;
							pos = pos-5;
						}
						else { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-4 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-4)-1)/4;
							pos = pos-4;
						}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
				} else { //odd
					tmpN = (int)((position+4)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos+4 < 33) to = squares[pos+4 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine-1 || tmpN == tmpLine+1) && tmpN < 8 && !to.isBlack()) {
						tmpLine = tmpN;
						if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
						else { to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos + 3;}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position+5)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos+5 < 33) to = squares[pos+5 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine-1 || tmpN == tmpLine+1) && tmpN < 8 && !to.isBlack()) {
						tmpLine = tmpN;
						if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
						else { to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position-4)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos-4 > 0) to = squares[pos-4 - 1];
					else to = new Man(" ");
					while((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos > 0 && !to.isBlack()) {
						if((tmpN + 1)%2 == 0) { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-5 > 0) to = squares[pos-5 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-5)-1)/4;
							pos = pos-5;
						}
						else { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-4 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-4)-1)/4;
							pos = pos-4;
						}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}
					
					tmpN = (int)((position-3)-1)/4;
					tmpLine = line;
					pos = position;
					if((tmpN == line-1 || tmpN == line+1) && pos-3 > 0) to = squares[pos-3 - 1];
					else to = new Man(" ");
					while((tmpN == line-1 || tmpN == line+1) && pos > 0 && !to.isBlack()) {
						if((tmpN + 1)%2 == 0) { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-4 > 0) to = squares[pos-4 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-4)-1)/4;
							pos = pos-4;
						}
						else { 
							if((tmpN == tmpLine-1 || tmpN == tmpLine+1) && pos-3 > 0) to = squares[pos-3 - 1];
							else to = new Man(" ");
							tmpLine = tmpN;
							tmpN = (int)((pos-3)-1)/4;
							pos = pos-3;
						}
						if(to.isEmpty()) ret.add(String.valueOf(pos));
					}

				}
			}
		}
	}
	
	/**
	 * Checks if the selected Man on the board is movable by the current player.
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 * @param man The selected starting position.
	 * @return boolean
	 */
	public boolean validMan(int player, int man) {
		Piece tmp = squares[man-1];
		if(player == 1 && tmp.isWhite()) {
			return true;
		} else if (player == 2 && tmp.isBlack()) {
			return true;
		} else return false;
	}
	
	/**
	 * Checks if the selected move is valid, that is contained in the possible moves array.
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 * @param move The selected move to be validated.
	 * @param moves The array containing the possible moves.
	 * @return boolean
	 */
	public boolean validMove(int player, int move, String[] moves) {
		boolean ret = false;
		for(int i=0; i<moves.length; i++) {
			if(Integer.parseInt(moves[i]) == move) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * Function that calculates the route where to take the most opposing Mans
	 * @param pos The position to start checking
	 * @param list The list to add the maximum taken final position
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 */
	private void combo(int pos, int player, List<String> list) {
		List<Integer> jumped = new ArrayList<Integer>();
		List<Integer> steps = new ArrayList<Integer>();
		List<Integer[]> ways = new ArrayList<Integer[]>();
		List<Integer[]> enemies = new ArrayList<Integer[]>();
		
		if(!squares[pos-1].isKing()) comboCount(player, pos, jumped, steps, ways, enemies);
		else comboCountK(player, pos, jumped, steps, ways, enemies);
		for(int i=0; i<ways.size()-1; i++) {
			if(ways.get(i).length < ways.get(i+1).length) {
				ways.remove(i);
				enemies.remove(i);
				i = -1;
			} else if(ways.get(i).length > ways.get(i+1).length) {
				ways.remove(i+1);
				enemies.remove(i+1);
				i = -1;
			}
		}
		this.waysList = ways;
		this.jumpedList = enemies;
	}
	
	/**
	 * Helping function to calculate the higher amount of taken opposing Mans
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 * @param pos The position where to calculate possible ways from
	 * @param jumped The list of jumped pieces.
	 * @param steps The list of steps made.
	 * @param ways The list of possible ways.
	 * @param enemies The list of enemies taken by each possible way.
	 */
	private void comboCount(int player, int pos, List<Integer> jumped, List<Integer> steps, List<Integer[]> ways, List<Integer[]> enemies) {
		int line = (int)((pos-1)/4);
		int tmpN = 0;
		Piece tmp;
		if(player == 1) { //white
			if((line+1)%2 == 0) { //even
				tmpN = (int)(pos+4 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos+4 > 0 && pos+4 < 33) tmp = squares[pos-1 + 4];
				else tmp = new Man(" ");
				if(tmp.isBlack() && !jumped.contains(pos+4)) {
					int nextLine = (int)(pos-1 + 9)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2)) {
						if(squares[pos-1 + 9].isEmpty()) {
							jumped.add(pos + 4);
							steps.add(pos + 9);
							comboCount(player, pos+9, jumped, steps, ways, enemies);
						}
					}
				} 
				
				tmpN = (int)(pos+3 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos+3 > 0 && pos+3 < 33) tmp = squares[pos-1 + 3];
				else tmp = new Man(" ");
				if(tmp.isBlack() && !jumped.contains(pos+3)) {
					int nextLine = (int)(pos-1 + 7)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2)) {
						if(squares[pos-1 + 7].isEmpty()) {
							jumped.add(pos + 3);
							steps.add(pos + 7);
							comboCount(player, pos+7, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos-4 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos-4 > 0 && pos-4 < 33) tmp = squares[pos-1 - 4];
				else tmp = new Man(" ");
				if(tmp.isBlack() && !jumped.contains(pos-4)) {
					int nextLine = (int)(pos-1 - 7)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2) && pos-7 > 0) {
						if(squares[pos-1 - 7].isEmpty()) {
							jumped.add(pos - 4);
							steps.add(pos - 7);
							comboCount(player, pos-7, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos-5 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos-5 > 0 && pos-5 < 33) tmp = squares[pos-1 - 5];
				else tmp = new Man(" ");
				if(tmp.isBlack() && !jumped.contains(pos-5)) {
					int nextLine = (int)(pos-1 - 9)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2) && pos-9 > 0) {
						if(squares[pos-1 - 9].isEmpty()) {
							jumped.add(pos - 5);
							steps.add(pos - 9);
							comboCount(player, pos-9, jumped, steps, ways, enemies);
						}
					}
				}
				if(!steps.isEmpty()) {
					ways.add(steps.toArray(new Integer[0])); steps.remove(steps.size()-1);
					enemies.add(jumped.toArray(new Integer[0])); jumped.remove(jumped.size()-1);
				} else	return;
				
			} else { //odd
				tmpN = (int)(pos+5 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos+5 > 0 && pos+5 < 33) tmp = squares[pos-1 + 5];
				else tmp = new Man(" ");
				if(tmp.isBlack() && !jumped.contains(pos+5)) {
					int nextLine = (int)(pos-1 + 9)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2)) {
						if(squares[pos-1 + 9].isEmpty()) {
							jumped.add(pos + 5);
							steps.add(pos + 9);
							comboCount(player, pos+9, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos+4 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos+4 > 0 && pos+4 < 33) tmp = squares[pos-1 + 4];
				else tmp = new Man(" ");
				if(tmp.isBlack() && !jumped.contains(pos+4)) {
					int nextLine = (int)(pos-1 + 7)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2)) {
						if(squares[pos-1 + 7].isEmpty()) {
							jumped.add(pos + 4);
							steps.add(pos + 7);
							comboCount(player, pos+7, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos-3 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos-3 > 0 && pos-3 < 33) tmp = squares[pos-1 - 3];
				else tmp = new Man(" ");
				if(tmp.isBlack() && !jumped.contains(pos-3)) {
					int nextLine = (int)(pos-1 - 7)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2) && pos-7 > 0) {
						if(squares[pos-1 - 7].isEmpty()) {
							jumped.add(pos - 3);
							steps.add(pos - 7);
							comboCount(player, pos-7, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos-4 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos-4 > 0 && pos-4 < 33) tmp = squares[pos-1 - 4];
				else tmp = new Man(" ");
				if(tmp.isBlack() && !jumped.contains(pos-4)) {
					int nextLine = (int)(pos-1 - 9)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2) && pos-9 > 0) {
						if(squares[pos-1 - 9].isEmpty()) {
							jumped.add(pos - 4);
							steps.add(pos - 9);
							comboCount(player, pos-9, jumped, steps, ways, enemies);
						}
					}
				} 
				if(!steps.isEmpty()) {
					ways.add(steps.toArray(new Integer[0])); steps.remove(steps.size()-1);
					enemies.add(jumped.toArray(new Integer[0])); jumped.remove(jumped.size()-1);
				} else	return;
			}
			
		} else { //black
			if((line+1)%2 == 0) { //even
				tmpN = (int)(pos+4 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos+4 > 0 && pos+4 < 33) tmp = squares[pos-1 + 4];
				else tmp = new Man(" ");
				if(tmp.isWhite() && !jumped.contains(pos+4)) {
					int nextLine = (int)(pos-1 + 9)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2)) {
						if(squares[pos-1 + 9].isEmpty()) {
							jumped.add(pos + 4);
							steps.add(pos + 9);
							comboCount(player, pos+9, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos+3 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos+3 > 0 && pos+3 < 33) tmp = squares[pos-1 + 3];
				else tmp = new Man(" ");
				if(tmp.isWhite() && !jumped.contains(pos+3)) {
					int nextLine = (int)(pos-1 + 7)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2)) {
						if(squares[pos-1 + 7].isEmpty()) {
							jumped.add(pos + 3);
							steps.add(pos + 7);
							comboCount(player, pos+7, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos-4 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos-4 > 0 && pos-4 < 33) tmp = squares[pos-1 - 4];
				else tmp = new Man(" ");
				if(tmp.isWhite() && !jumped.contains(pos-4)) {
					int nextLine = (int)(pos-1 - 7)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2) && pos-7 > 0) {
						if(squares[pos-1 - 7].isEmpty()) {
							jumped.add(pos - 4);
							steps.add(pos - 7);
							comboCount(player, pos-7, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos-5 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos-5 > 0 && pos-5 < 33) tmp = squares[pos-1 - 5];
				else tmp = new Man(" ");
				if(tmp.isWhite() && !jumped.contains(pos-5)) {
					int nextLine = (int)(pos-1 - 9)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2) && pos-9 > 0) {
						if(squares[pos-1 - 9].isEmpty()) {
							jumped.add(pos - 5);
							steps.add(pos - 9);
							comboCount(player, pos-9, jumped, steps, ways, enemies);
						}
					}
				}
				if(!steps.isEmpty()) {
					ways.add(steps.toArray(new Integer[0])); steps.remove(steps.size()-1);
					enemies.add(jumped.toArray(new Integer[0])); jumped.remove(jumped.size()-1);
				} else	return;
				
			} else { //odd
				tmpN = (int)(pos+5 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos+5 > 0 && pos+5 < 33) tmp = squares[pos-1 + 5];
				else tmp = new Man(" ");
				if(tmp.isWhite() && !jumped.contains(pos+5)) {
					int nextLine = (int)(pos-1 + 9)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2)) {
						if(squares[pos-1 + 9].isEmpty()) {
							jumped.add(pos + 5);
							steps.add(pos + 9);
							comboCount(player, pos+9, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos+4 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos+4 > 0 && pos+4 < 33) tmp = squares[pos-1 + 4];
				else tmp = new Man(" ");
				if(tmp.isWhite() && !jumped.contains(pos+4)) {
					int nextLine = (int)(pos-1 + 7)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2)) {
						if(squares[pos-1 + 7].isEmpty()) {
							jumped.add(pos + 4);
							steps.add(pos + 7);
							comboCount(player, pos+7, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos-3 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos-3 > 0 && pos-3 < 33) tmp = squares[pos-1 - 3];
				else tmp = new Man(" ");
				if(tmp.isWhite() && !jumped.contains(pos-3)) {
					int nextLine = (int)(pos-1 - 7)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2) && pos-7 > 0) {
						if(squares[pos-1 - 7].isEmpty()) {
							jumped.add(pos - 3);
							steps.add(pos - 7);
							comboCount(player, pos-7, jumped, steps, ways, enemies);
						}
					}
				}
				
				tmpN = (int)(pos-4 - 1)/4;
				if((tmpN == line-1 || tmpN == line+1) && pos-4 > 0 && pos-4 < 33) tmp = squares[pos-1 - 4];
				else tmp = new Man(" ");
				if(tmp.isWhite() && !jumped.contains(pos-4)) {
					int nextLine = (int)(pos-1 - 9)/4;
					if(nextLine < 8 && nextLine >= 0 && (nextLine == line + 2 || nextLine == line - 2) && pos-9 > 0) {
						if(squares[pos-1 - 9].isEmpty()) {
							jumped.add(pos - 4);
							steps.add(pos - 9);
							comboCount(player, pos-9, jumped, steps, ways, enemies);
						}
					}
				}
				if(!steps.isEmpty()) {
					ways.add(steps.toArray(new Integer[0])); steps.remove(steps.size()-1);
					enemies.add(jumped.toArray(new Integer[0])); jumped.remove(jumped.size()-1);
				} else	return;
			}
		}
	}
	
	/**
	 * Helping function to calculate the higher amount of taken opposing Mans by a King
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 * @param position The position where to calculate possible ways from
	 * @param jumped The list of jumped pieces.
	 * @param steps The list of steps made.
	 * @param ways The list of possible ways.
	 * @param enemies The list of enemies taken by each possible way.
	 */
	private void comboCountK(int player, int position, List<Integer> jumped, List<Integer> steps, List<Integer[]> ways, List<Integer[]> enemies) {
		int line = (int)((position-1)/4);
		int tmpN = 0, tmpLine = 0, pos = position;
		Piece to;
		if(player == 1) { //white
			if((line+1)%2 == 0) { //even
				tmpN = (int)((position+4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+4 < 33) to = squares[pos+4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
					tmpLine = tmpN;
					if(to.isBlack()) {
						tmpN = (int)(pos+9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+9 < 33) to = squares[pos+9 - 1];
						else to = new Man(" ");
						tmpN--;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) {
									to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;
									steps.add(pos+4); comboCountK(player, pos+4, jumped, steps, ways, enemies);
								}
								else { 
									to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;
									steps.add(pos+5); comboCountK(player, pos+5, jumped, steps, ways, enemies);
								}
							} 
						} else break;
					}
					if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
					else { to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;}
				} 
				
				tmpN = (int)((position+3)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+3 < 33) to = squares[pos+3 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
					tmpLine = tmpN;
					if(to.isBlack()) {
						tmpN = (int)(pos+7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+7 < 33) to = squares[pos+7 - 1];
						else to = new Man(" ");
						tmpN--;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) {
									to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;
									steps.add(pos+3); comboCountK(player, pos+3, jumped, steps, ways, enemies);
								}
								else { 
									to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;
									steps.add(pos+4); comboCountK(player, pos+4, jumped, steps, ways, enemies);
								}
							} 
						} else break;
					}
					if((tmpN + 1)%2 == 0) { to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;}
					else { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;}
				}
				
				tmpN = (int)((position-4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
					if(to.isBlack()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-7 > 0) to = squares[pos-7 - 1];
						else to = new Man(" ");
						tmpN++;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-4)-1)/4;
									pos = pos-4;
									steps.add(pos-4); comboCountK(player, pos-4, jumped, steps, ways, enemies);
								}
								else { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-3)-1)/4;
									pos = pos-3;
									steps.add(pos-3); comboCountK(player, pos-3, jumped, steps, ways, enemies);
								}
							}
						}
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-4)-1)/4;
						pos = pos-4;
					}
					else { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-3)-1)/4;
						pos = pos-3;
					}
				}
				
				tmpN = (int)((position-5)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
					if(to.isBlack()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-9 > 0) to = squares[pos-9 - 1];
						else to = new Man(" ");
						tmpN++;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-5)-1)/4;
									pos = pos-5;
									steps.add(pos-5); comboCountK(player, pos-5, jumped, steps, ways, enemies);
								}
								else { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-4)-1)/4;
									pos = pos-4;
									steps.add(pos-4); comboCountK(player, pos-4, jumped, steps, ways, enemies);
								}
							}
						}
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-5)-1)/4;
						pos = pos-5;
					}
					else { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-4)-1)/4;
						pos = pos-4;
					}
				}
				if(!steps.isEmpty()) {
					ways.add(steps.toArray(new Integer[0])); steps.remove(steps.size()-1);
					enemies.add(jumped.toArray(new Integer[0])); jumped.remove(jumped.size()-1);
				} else	return;
				
			} else { //odd
				tmpN = (int)((position+4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+4 < 33) to = squares[pos+4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
					tmpLine = tmpN;
					if(to.isBlack()) {
						tmpN = (int)(pos+7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+7 < 33) to = squares[pos+7 - 1];
						else to = new Man(" ");
						tmpN--;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) {
									to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;
									steps.add(pos+3); comboCountK(player, pos+3, jumped, steps, ways, enemies);
								}
								else { 
									to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;
									steps.add(pos+4); comboCountK(player, pos+4, jumped, steps, ways, enemies);
								}
							} 
						} else break;
					}
					if((tmpN + 1)%2 == 0) { to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;}
					else { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;}
				} 
				
				tmpN = (int)((position+5)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+5 < 33) to = squares[pos+5 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
					tmpLine = tmpN;
					if(to.isBlack()) {
						tmpN = (int)(pos+9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+9 < 33) to = squares[pos+9 - 1];
						else to = new Man(" ");
						tmpN--;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) {
									to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;
									steps.add(pos+4); comboCountK(player, pos+4, jumped, steps, ways, enemies);
								}
								else { 
									to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;
									steps.add(pos+5); comboCountK(player, pos+5, jumped, steps, ways, enemies);
								}
							} 
						} else break;
					}
					if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
					else { to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;}
				}
				
				tmpN = (int)((position-4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
					if(to.isBlack()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-9 > 0) to = squares[pos-9 - 1];
						else to = new Man(" ");
						tmpN++;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-5)-1)/5;
									pos = pos-5;
									steps.add(pos-5); comboCountK(player, pos-5, jumped, steps, ways, enemies);
								}
								else { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-4)-1)/4;
									pos = pos-4;
									steps.add(pos-4); comboCountK(player, pos-4, jumped, steps, ways, enemies);
								}
							}
						}
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-5)-1)/4;
						pos = pos-5;
					}
					else { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-4)-1)/4;
						pos = pos-4;
					}
				}
				
				tmpN = (int)((position-3)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
					if(to.isBlack()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-7 > 0) to = squares[pos-7 - 1];
						else to = new Man(" ");
						tmpN++;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-4)-1)/4;
									pos = pos-4;
									steps.add(pos-4); comboCountK(player, pos-4, jumped, steps, ways, enemies);
								}
								else { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-3)-1)/4;
									pos = pos-3;
									steps.add(pos-3); comboCountK(player, pos-3, jumped, steps, ways, enemies);
								}
							}
						}
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-4)-1)/4;
						pos = pos-4;
					}
					else { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-3)-1)/4;
						pos = pos-3;
					}
				}
				if(!steps.isEmpty()) {
					ways.add(steps.toArray(new Integer[0])); steps.remove(steps.size()-1);
					enemies.add(jumped.toArray(new Integer[0])); jumped.remove(jumped.size()-1);
				} else	return;
			}
		} else { //black
			if((line+1)%2 == 0) { //even
				tmpN = (int)((position+4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+4 < 33) to = squares[pos+4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
					tmpLine = tmpN;
					if(to.isWhite()) {
						tmpN = (int)(pos+9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+9 < 33) to = squares[pos+9 - 1];
						else to = new Man(" ");
						tmpN--;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) {
									to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;
									steps.add(pos+4); comboCountK(player, pos+4, jumped, steps, ways, enemies);
								}
								else { 
									to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;
									steps.add(pos+5); comboCountK(player, pos+5, jumped, steps, ways, enemies);
								}
							} 
						} else break;
					}
					if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
					else { to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;}
				} 
				
				tmpN = (int)((position+3)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+3 < 33) to = squares[pos+3 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
					tmpLine = tmpN;
					if(to.isWhite()) {
						tmpN = (int)(pos+7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+7 < 33) to = squares[pos+7 - 1];
						else to = new Man(" ");
						tmpN--;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) {
									to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;
									steps.add(pos+3); comboCountK(player, pos+3, jumped, steps, ways, enemies);
								}
								else { 
									to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;
									steps.add(pos+4); comboCountK(player, pos+4, jumped, steps, ways, enemies);
								}
							} 
						} else break;
					}
					if((tmpN + 1)%2 == 0) { to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;}
					else { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;}
				}
				
				tmpN = (int)((position-4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
					if(to.isWhite()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-7 > 0) to = squares[pos-7 - 1];
						else to = new Man(" ");
						tmpN++;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-4)-1)/4;
									pos = pos-4;
									steps.add(pos-4); comboCountK(player, pos-4, jumped, steps, ways, enemies);
								}
								else { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-3)-1)/4;
									pos = pos-3;
									steps.add(pos-3); comboCountK(player, pos-3, jumped, steps, ways, enemies);
								}
							}
						}
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-4)-1)/4;
						pos = pos-4;
					}
					else { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-3)-1)/4;
						pos = pos-3;
					}
				}
				
				tmpN = (int)((position-5)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
					if(to.isWhite()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-9 > 0) to = squares[pos-9 - 1];
						else to = new Man(" ");
						tmpN++;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-5)-1)/4;
									pos = pos-5;
									steps.add(pos-5); comboCountK(player, pos-5, jumped, steps, ways, enemies);
								}
								else { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-4)-1)/4;
									pos = pos-4;
									steps.add(pos-4); comboCountK(player, pos-4, jumped, steps, ways, enemies);
								}
							}
						}
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-5)-1)/4;
						pos = pos-5;
					}
					else { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-4)-1)/4;
						pos = pos-4;
					}
				}
				if(!steps.isEmpty()) {
					ways.add(steps.toArray(new Integer[0])); steps.remove(steps.size()-1);
					enemies.add(jumped.toArray(new Integer[0])); jumped.remove(jumped.size()-1);
				} else	return;
				
			} else { //odd
				tmpN = (int)((position+4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+4 < 33) to = squares[pos+4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
					tmpLine = tmpN;
					if(to.isWhite()) {
						tmpN = (int)(pos+7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+7 < 33) to = squares[pos+7 - 1];
						else to = new Man(" ");
						tmpN--;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) {
									to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;
									steps.add(pos+3); comboCountK(player, pos+3, jumped, steps, ways, enemies);
								}
								else { 
									to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;
									steps.add(pos+4); comboCountK(player, pos+4, jumped, steps, ways, enemies);
								}
							} 
						} else break;
					}
					if((tmpN + 1)%2 == 0) { to = squares[pos+3 - 1]; tmpN = (int)((pos+3)-1)/4; pos = pos+3;}
					else { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos + 4;}
				} 
				
				tmpN = (int)((position+5)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+5 < 33) to = squares[pos+5 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
					tmpLine = tmpN;
					if(to.isWhite()) {
						tmpN = (int)(pos+9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+9 < 33) to = squares[pos+9 - 1];
						else to = new Man(" ");
						tmpN--;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) {
									to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;
									steps.add(pos+4); comboCountK(player, pos+4, jumped, steps, ways, enemies);
								}
								else { 
									to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;
									steps.add(pos+5); comboCountK(player, pos+5, jumped, steps, ways, enemies);
								}
							} 
						} else break;
					}
					if((tmpN + 1)%2 == 0) { to = squares[pos+4 - 1]; tmpN = (int)((pos+4)-1)/4; pos = pos+4;}
					else { to = squares[pos+5 - 1]; tmpN = (int)((pos+5)-1)/4; pos = pos + 5;}
				}
				
				tmpN = (int)((position-4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
					if(to.isWhite()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-9 > 0) to = squares[pos-9 - 1];
						else to = new Man(" ");
						tmpN++;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-5)-1)/5;
									pos = pos-5;
									steps.add(pos-5); comboCountK(player, pos-5, jumped, steps, ways, enemies);
								}
								else { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-4)-1)/4;
									pos = pos-4;
									steps.add(pos-4); comboCountK(player, pos-4, jumped, steps, ways, enemies);
								}
							}
						}
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-5)-1)/4;
						pos = pos-5;
					}
					else { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-4)-1)/4;
						pos = pos-4;
					}
				}
				
				tmpN = (int)((position-3)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
					if(to.isWhite()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-7 > 0) to = squares[pos-7 - 1];
						else to = new Man(" ");
						tmpN++;
						if(to.isEmpty()) {
							jumped.add(pos);
							while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
								tmpLine = tmpN;
								if((tmpN + 1)%2 == 0) { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-4)-1)/4;
									pos = pos-4;
									steps.add(pos-4); comboCountK(player, pos-4, jumped, steps, ways, enemies);
								}
								else { 
									if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
									else to = new Man(" ");
									tmpLine = tmpN;
									tmpN = (int)((pos-3)-1)/4;
									pos = pos-3;
									steps.add(pos-3); comboCountK(player, pos-3, jumped, steps, ways, enemies);
								}
							}
						}
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-4)-1)/4;
						pos = pos-4;
					}
					else { 
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
						else to = new Man(" ");
						tmpLine = tmpN;
						tmpN = (int)((pos-3)-1)/4;
						pos = pos-3;
					}
				}
				if(!steps.isEmpty()) {
					ways.add(steps.toArray(new Integer[0])); steps.remove(steps.size()-1);
					enemies.add(jumped.toArray(new Integer[0])); jumped.remove(jumped.size()-1);
				} else	return;
			}
		}
	}
	
	/**
	 * Helping function to check if there are any jumpable adjacent enemies around a Man
	 * @param position The position to check around
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 * @return boolean
	 */
	private boolean checkAdjacentEnemies(int position, int player) {
		Piece to, to2;
		int line = (int)(position-1)/4;
		int tmpN = 0;
		if(player == 1) { //white
			if((line + 1)%2 == 0) { //even
				tmpN = (int)((position+4)-1)/4;
				if((tmpN == line + 1 || tmpN == line - 1) && tmpN < 8 && tmpN >= 0) {
					tmpN = (int)(position+9 - 1)/4;
					to = squares[position+4 - 1];
					if(position+9 > 0 && position+9 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position+9 - 1];
					else to2 = new Man(" ");
					if(to.isBlack() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position+3)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0) {
					to = squares[position+3 - 1];
					tmpN = (int)(position+7 - 1)/4;
					if(position+7 > 0 && position+7 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position+7 - 1];
					else to2 = new Man(" ");
					if(to.isBlack() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position-4)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0 && position-4 > 0) {
					to = squares[position-4 - 1];
					tmpN = (int)(position-7 - 1)/4;
					if(position-7 > 0 && position-7 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position-7 - 1];
					else to2 = new Man(" ");
					if(to.isBlack() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position-5)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0 && position-5 > 0) {
					to = squares[position-5 - 1];
					tmpN = (int)(position-9 - 1)/4;
					if(position-9 > 0 && position-9 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position-9 - 1];
					else to2 = new Man(" ");
					if(to.isBlack() && to2.isEmpty()) return true;
				}
			} else { //odd
				tmpN = (int)((position+4)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0) {
					to = squares[position+4 - 1];
					tmpN = (int)(position+7 - 1)/4;
					if(position+7 > 0 && position+7 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position+7 - 1];
					else to2 = new Man(" ");
					if(to.isBlack() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position+5)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0) {
					to = squares[position+5 - 1];
					tmpN = (int)(position+9 - 1)/4;
					if(position+9 > 0 && position+9 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position+9 - 1];
					else to2 = new Man(" ");
					if(to.isBlack() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position-4)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0 && position-4 > 0) {
					to = squares[position-4 - 1];
					tmpN = (int)(position-9 - 1)/4;
					if(position-9 > 0 && position-9 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position-9 - 1];
					else to2 = new Man(" ");
					if(to.isBlack() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position-3)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0 && position-3 > 0) {
					to = squares[position-3 - 1];
					tmpN = (int)(position-7 - 1)/4;
					if(position-7 > 0 && position-7 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position-7 - 1];
					else to2 = new Man(" ");
					if(to.isBlack() && to2.isEmpty()) return true;
				}
			}
		} else { //black
			if((line + 1)%2 == 0) { //even
				tmpN = (int)((position+4)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0) {
					to = squares[position+4 - 1];
					tmpN = (int)(position+9 - 1)/4;
					if(position+9 > 0 && position+9 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position+9 - 1];
					else to2 = new Man(" ");
					if(to.isWhite() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position+3)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0) {
					to = squares[position+3 - 1];
					tmpN = (int)(position+7 - 1)/4;
					if(position+7 > 0 && position+7 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position+7 - 1];
					else to2 = new Man(" ");
					if(to.isWhite() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position-4)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0 && position-4 > 0) {
					to = squares[position-4 - 1];
					tmpN = (int)(position-7 - 1)/4;
					if(position-7 > 0 && position-7 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position-7 - 1];
					else to2 = new Man(" ");
					if(to.isWhite() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position-5)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0 && position-5 > 0) {
					to = squares[position-5 - 1];
					tmpN = (int)(position-9 - 1)/4;
					if(position-9 > 0 && position-9 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position-9 - 1];
					else to2 = new Man(" ");
					if(to.isWhite() && to2.isEmpty()) return true;
				}
			} else { //odd
				tmpN = (int)((position+4)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0) {
					to = squares[position+4 - 1];
					tmpN = (int)(position+7 - 1)/4;
					if(position+7 > 0 && position+7 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position+7 - 1];
					else to2 = new Man(" ");
					if(to.isWhite() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position+5)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0) {
					to = squares[position+5 - 1];
					tmpN = (int)(position+9 - 1)/4;
					if(position+9 > 0 && position+9 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position+9 - 1];
					else to2 = new Man(" ");
					if(to.isWhite() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position-4)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0 && position-4 > 0) {
					to = squares[position-4 - 1];
					tmpN = (int)(position-9 - 1)/4;
					if(position-9 > 0 && position-9 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position-9 - 1];
					else to2 = new Man(" ");
					if(to.isWhite() && to2.isEmpty()) return true;
				}
				
				tmpN = (int)((position-3)-1)/4;
				if((tmpN == line-1 || tmpN == line+1) && tmpN < 8 && tmpN >= 0 && position-3 > 0) {
					to = squares[position-3 - 1];
					tmpN = (int)(position-7 - 1)/4;
					if(position-7 > 0 && position-7 < 33 && (tmpN == line+2 || tmpN == line-2)) to2 = squares[position-7 - 1];
					else to2 = new Man(" ");
					if(to.isWhite() && to2.isEmpty()) return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Helping function to check adjacent enemies around a King
	 * @param position The position to check around
	 * @param player The current player, 1 - Whites, 2 - Blacks.
	 * @return boolean
	 */
	private boolean checkAdjacentEnemiesK(int position, int player) {
		Piece to;
		int line = (int)(position-1)/4;
		int tmpN = 0, pos = 0, tmpLine = 0;
		if(player == 1) { //white
			if((line + 1)%2 == 0) { //even
				tmpN = (int)((position+4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+4 < 33) to = squares[pos+4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
					tmpLine = tmpN;
					if(to.isBlack()) {
						tmpN = (int)(pos+9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+9 < 33) to = squares[pos+9 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { pos = pos+5; tmpN = (int)((pos+4)-1)/4; to = squares[pos+4 - 1];}
					else { pos = pos + 4; tmpN = (int)((pos+5)-1)/4; to = squares[pos+5 - 1]; }
				}
				
				tmpN = (int)((position+3)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+3 < 33) to = squares[pos+3 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
					tmpLine = tmpN;
					if(to.isBlack()) {
						tmpN = (int)(pos+7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+7 < 33) to = squares[pos+7 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { pos = pos+4; tmpN = (int)((pos+3)-1)/4; to = squares[pos+3 - 1];}
					else { pos = pos + 3; tmpN = (int)((pos+4)-1)/4; to = squares[pos+4 - 1];}
				}
				
				tmpN = (int)((position-4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
					if(to.isBlack()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-7 > 0) to = squares[pos-7 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						tmpLine = tmpN;
						pos = pos-3;
						tmpN = (int)((pos-4)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
					}
					else { 
						tmpLine = tmpN;
						pos = pos-4;
						tmpN = (int)((pos-3)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
						else to = new Man(" ");
					}
				}
				
				tmpN = (int)((position-5)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
					if(to.isBlack()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-9 > 0) to = squares[pos-9 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						tmpLine = tmpN;
						pos = pos-4;
						tmpN = (int)((pos-5)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
						else to = new Man(" ");
					}
					else { 
						tmpLine = tmpN;
						pos = pos-5;
						tmpN = (int)((pos-4)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
					}
				}
			} else { //odd
				tmpN = (int)((position+4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+4 < 33) to = squares[pos+4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
					tmpLine = tmpN;
					if(to.isBlack()) {
						tmpN = (int)(pos+7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+7 < 33) to = squares[pos+7 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { pos = pos + 3; tmpN = (int)((pos+4)-1)/4; to = squares[pos+4 - 1];}
					else { pos = pos+4; tmpN = (int)((pos+3)-1)/4; to = squares[pos+3 - 1];}
				}
				
				tmpN = (int)((position+5)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+5 < 33) to = squares[pos+5 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isWhite()) {
					tmpLine = tmpN;
					if(to.isBlack()) {
						tmpN = (int)(pos+9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+9 < 33) to = squares[pos+9 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { pos = pos + 5; tmpN = (int)((pos+4)-1)/4; to = squares[pos+4 - 1];}
					else { pos = pos+4; tmpN = (int)((pos+5)-1)/4; to = squares[pos+5 - 1];}
				}
				
				tmpN = (int)((position-4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
					if(to.isBlack()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-9 > 0) to = squares[pos-9 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						tmpLine = tmpN;
						pos = pos-4;
						tmpN = (int)((pos-5)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
						else to = new Man(" ");
					}
					else { 
						tmpLine = tmpN;
						pos = pos-5;
						tmpN = (int)((pos-4)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
					}
				}
				
				tmpN = (int)((position-3)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isWhite()) {
					if(to.isBlack()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-7 > 0) to = squares[pos-7 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						tmpLine = tmpN;
						pos = pos-3;
						tmpN = (int)((pos-4)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
					}
					else { 
						tmpLine = tmpN;
						pos = pos-4;
						tmpN = (int)((pos-3)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
						else to = new Man(" ");
					}
				}

			}
		} else { //black
			if((line + 1)%2 == 0) { //even
				tmpN = (int)((position+4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+4 < 33) to = squares[pos+4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
					tmpLine = tmpN;
					if(to.isWhite()) {
						tmpN = (int)(pos+9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+9 < 33) to = squares[pos+9 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { pos = pos + 5; tmpN = (int)((pos+4)-1)/4; to = squares[pos+4 - 1];}
					else { pos = pos+4; tmpN = (int)((pos+5)-1)/4; to = squares[pos+5 - 1];}
				}
				
				tmpN = (int)((position+3)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+3 < 33) to = squares[pos+3 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
					tmpLine = tmpN;
					if(to.isWhite()) {
						tmpN = (int)(pos+7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+7 < 33) to = squares[pos+7 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { pos = pos + 4; tmpN = (int)((pos+3)-1)/4; to = squares[pos+3 - 1];}
					else { pos = pos+3; tmpN = (int)((pos+4)-1)/4; to = squares[pos+4 - 1];}
				}
				
				tmpN = (int)((position-4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
					if(to.isWhite()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-7 > 0) to = squares[pos-7 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						tmpLine = tmpN;
						pos = pos-3;
						tmpN = (int)((pos-4)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
					}
					else { 
						tmpLine = tmpN;
						pos = pos-4;
						tmpN = (int)((pos-3)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
						else to = new Man(" ");
					}
				}
				
				tmpN = (int)((position-5)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
					if(to.isWhite()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-9 > 0) to = squares[pos-9 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						tmpLine = tmpN;
						pos = pos-4;
						tmpN = (int)((pos-5)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
						else to = new Man(" ");
					}
					else { 
						tmpLine = tmpN;
						pos = pos-5;
						tmpN = (int)((pos-4)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
					}
				}
			} else { //odd
				tmpN = (int)((position+4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+4 < 33) to = squares[pos+4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
					tmpLine = tmpN;
					if(to.isWhite()) {
						tmpN = (int)(pos+7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+7 < 33) to = squares[pos+7 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { pos = pos + 3; tmpN = (int)((pos+4)-1)/4; to = squares[pos+4 - 1];}
					else { pos = pos+4; tmpN = (int)((pos+3)-1)/4; to = squares[pos+3 - 1];}
				}
				
				tmpN = (int)((position+5)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos+5 < 33) to = squares[pos+5 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && tmpN < 8 && !to.isBlack()) {
					tmpLine = tmpN;
					if(to.isWhite()) {
						tmpN = (int)(pos+9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos+9 < 33) to = squares[pos+9 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { pos = pos + 5; tmpN = (int)((pos+4)-1)/4; to = squares[pos+4 - 1];}
					else { pos = pos+4; tmpN = (int)((pos+5)-1)/4; to = squares[pos+5 - 1];}
				}
				
				tmpN = (int)((position-4)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
					if(to.isWhite()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-9 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-9 > 0) to = squares[pos-9 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						tmpLine = tmpN;
						pos = pos-4;
						tmpN = (int)((pos-5)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-5 > 0) to = squares[pos-5 - 1];
						else to = new Man(" ");
					}
					else { 
						tmpLine = tmpN;
						pos = pos-5;
						tmpN = (int)((pos-4)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
					}
				}
				
				tmpN = (int)((position-3)-1)/4;
				tmpLine = line;
				pos = position;
				if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
				else to = new Man(" ");
				while((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos > 0 && !to.isBlack()) {
					if(to.isWhite()) {
						tmpLine = tmpN;
						tmpN = (int)(pos-7 - 1)/4;
						if((tmpN == tmpLine+2 || tmpN == tmpLine-2) && pos-7 > 0) to = squares[pos-7 - 1];
						else to = new Man(" ");
						if(to.isEmpty()) return true;
						else break;
					}
					if((tmpN + 1)%2 == 0) { 
						tmpLine = tmpN;
						pos = pos-3;
						tmpN = (int)((pos-4)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-4 > 0) to = squares[pos-4 - 1];
						else to = new Man(" ");
					}
					else { 
						tmpLine = tmpN;
						pos = pos-4;
						tmpN = (int)((pos-3)-1)/4;
						if((tmpN == tmpLine+1 || tmpN == tmpLine-1) && pos-3 > 0) to = squares[pos-3 - 1];
						else to = new Man(" ");
						
					}
				}

			}
		}
		return false;
	}
	
	/**
	 * Function to display the board notation as a String
	 * @return String
	 */
	public String displayNotation() {
		String ret = "";
		for(int i=0; i<dimension; i++) {
			if((i+1)%2 != 0) {
				for(int j=4*i; j<4*(i+1); j++) {
					if(j>8) ret = ret + "\u2591\u2591" + (j+1);
					else ret = ret + "\u2591\u2591" + (j+1) + " ";
				}
				ret = ret + "\n";
			}
			else {
				for(int j=4*i; j<4*(i+1); j++) {
					if(j>8) ret = ret + (j+1) + "\u2591\u2591";
					else ret = ret + (j+1) + " \u2591\u2591";
				}
				ret = ret + "\n";
			}
		}
		return ret;
	}
	
}
