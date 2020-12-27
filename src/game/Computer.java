package game;

import game.core.Vector2Int;

import java.util.Random;

public class Computer extends Player
{
    /**
     * 
     * @param name The name of the player
     * @param match The match context
     */
    public Computer(String name, Match match)
    {
        super(name, match);
    }

    /**
     * Gets invoked when the placing player has changed
     * @param player The new placing player
     * @param shipType The new ship type to be placed
     */
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

    /**
     * Gets invoked when the guessing player has changed
     * @param player The new placing player
     */
    @Override
    public void onGuessingPlayerChanged(Player player)
    {
        super.onGuessingPlayerChanged(player);
        
    	if (isGuessing)
    	{
    		var rand = new Random();
            var cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
            
            while (!match.canGuess(this, cellPos))
            {
                cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
            }
            
            invokeFieldGuessed(cellPos);
    	}
    }
}
