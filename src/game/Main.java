package game;

import game.core.Game;

public class Main
{
	public static void main(String args[])  
    {
		var game = new Game("Battleships", 60, 60);
		game.StartMatch();
    }
}
