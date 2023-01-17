
import board.*;
import java.util.Scanner;

/**
 * Main testing code for Draughts
 */

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int select = 0;
		int move = 0;
		String[] moves;
		
		CheckBoard board = new CheckBoard();
		System.out.println("Board Notation");
		System.out.println(board.displayNotation());
		
		System.out.println("Rules: \n" + "The goal is to capture all the enemy pieces\n" + 
							"To move a piece simply select the square of the piece you want to move\n" +
							"Piece positions are given in the above board notation, try to remember it\n" +
							"Every piece can move diagonnaly forward but can capture backrawds as well\n" +
							"Once a piece reaches the other side of the board is crowned and becomes a King" +
							"\nKings move diagonally and can choose where to stop\n" +
							"By customs you have to take pieces if you can, but in this version\n" +
							"You just have to take the maximum amount of pieces by a selected piece if it can take\n" +
							"For more rules google it\npress enter to continue");
		scan.nextLine();
		int currentPlayer = 1;
		
		String winner = "";
		
		//select
		while(winner.isEmpty()) {
			try {
				winner = board.whoWins();
				
				System.out.println(board.toString());
				System.out.println("Player " + currentPlayer + " Choose 0 to exit");
				
				System.out.println("Select Man: ");
				select = scan.nextInt();
				scan.nextLine();
				if(select == 0) break;
				if(!board.validMan(currentPlayer, select)) {
					System.out.println("Invalid");
					continue;
				}
				
				System.out.println("Select where to move: ");
				moves = board.possibleMoves(select, currentPlayer);
				if(moves.length == 0) {
					System.out.println("No moves available");
					continue;
				}
				for(int i=0; i<moves.length; i++) System.out.print(moves[i] + " ");
				System.out.print("\n");
								
				move = scan.nextInt();
				scan.nextLine();
				
				if(!board.validMove(currentPlayer, move, moves)) {
					System.out.println("Invalid move");
					continue;
				}
				
				board.move(select, move);
				if(++currentPlayer > 2) currentPlayer = 1;
				continue;
			} catch(Exception e) {
				System.out.println("Error during movement");
				scan.nextLine();
				e.printStackTrace();
				continue;
			}
		}
		
		if(winner.isEmpty()) winner = "Draw";
		System.out.printf("The winner is: %s\n", winner);
		System.out.println(board.toString());
		scan.close();
	}

}
