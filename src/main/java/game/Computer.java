package game;

import game.core.Vector2Int;

import java.util.Random;

import javax.lang.model.util.ElementScanner14;
import javax.swing.text.DefaultStyledDocument.ElementSpec;

public class Computer extends Player
{
    private Vector2Int lastGuessPos;

    /**
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
    public void onGuessingPlayerChanged(Player player, boolean hasHit)
    {
        super.onGuessingPlayerChanged(player, hasHit);

    	if (isGuessing)
    	{
            // try
            // {
            //     Thread.sleep(1000);
            // }
            // catch (Exception ex) { }

            if (lastGuessPos == null)
            {
                var guessPos = getRandomGuessPos();
                lastGuessPos = guessPos;
                invokeFieldGuessed(guessPos);
            }
            else
            {
                if (hasHit)
                {
                    var rightPos = lastGuessPos.add(Vector2Int.right());
                    var leftPos = lastGuessPos.add(Vector2Int.left());

                    if (match.inBounds(rightPos) && match.canGuess(this, rightPos))
                    {
                        lastGuessPos = rightPos;
                        invokeFieldGuessed(rightPos);
                    }
                    else if (match.inBounds(leftPos) && match.canGuess(this, leftPos))
                    {
                        lastGuessPos = leftPos;
                        invokeFieldGuessed(leftPos);
                    }
                    else
                    {
                        var guessPos = getRandomGuessPos();
                        lastGuessPos = guessPos;
                        invokeFieldGuessed(guessPos);
                    }
                }
                else
                {
                    var guessPos = getRandomGuessPos();
                    lastGuessPos = guessPos;
                    invokeFieldGuessed(guessPos);
                }
            }
    	}
    }
    
    private Vector2Int getRandomGuessPos()
    {
        var rand = new Random();
        var cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
        
        while (!match.canGuess(this, cellPos))
        {
            cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
        }

        return cellPos;
    }
}
