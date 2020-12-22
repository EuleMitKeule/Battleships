package game;

import game.core.Vector2Int;

import java.util.Random;

public class Computer extends Player
{
    public Computer(String name, Match match)
    {
        super(name, match);
    }

    @Override
    public void onPlacingPlayerChanged(Player player, ShipType shipType)
    {
        super.onPlacingPlayerChanged(player, shipType);

        if (player == this)
        {
            var rand = new Random();
            var cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));

            while (!match.canPlace(this, cellPos, curShipType))
            {
                cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
            }

            invokeShipPlaced(cellPos, shipType);
        }
    }

    @Override
    public void onGuessingPlayerChanged(Player player)
    {
    	super.onGuessingPlayerChanged(player);
    	if(isGuessing)
    	{
    		System.out.println("computer red");
    		var rand = new Random();
            var cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
            
            while (!match.canGuess(this, cellPos))
            {
                cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
            }
            invokeGuess(cellPos);
    	}
    }
}
