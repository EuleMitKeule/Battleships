package game;

public class Board {
	int [][] board; 
	
	public Board() {
		board = new int [10][10];
	}

	public int[][] getBoard() {
		return board;
	}
	
	public void resetBoard() {
		board = new int[5][5];
	}
	
	public static void main(String args[])  
    {  
       
    } 
}
