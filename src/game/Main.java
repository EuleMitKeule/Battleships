package game;

public class Main {
	
	Board board;
	Player player1;
	Player player2;
	
	public Main(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
				
	}
	public static void main(String args[])  
    {
		System.out.println("Game gestartet");
		var game = new Game();
    } 
}
