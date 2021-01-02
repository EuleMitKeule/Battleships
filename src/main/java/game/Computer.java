package game;

import game.core.Vector2Int;

import java.util.Random;

public class Computer extends Player
{
    public Vector2Int lastGuessPos;

    public AiState aiStartState;
    
    private AiState _state;
    /**
     * @param name The name of the player
     * @param match The match context
     */
    public Computer(String name, Match match)
    {
        super(name, match);

        aiStartState = new AiStartState(this);

        setState(aiStartState);
    }

    public void setState(AiState state) 
    {
        if (state == null) return;
        
        if (this._state != null) 
        {
            state.exitState();
        }

        this._state = state;
        state.enterState();
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
            _state.onGuess();
        }

        //     if (hasHit)
        //     {
        //         var rightPos = lastGuessPos.add(Vector2Int.right());
        //         var leftPos = lastGuessPos.add(Vector2Int.left());

        //         if (match.inBounds(rightPos) && match.canGuess(this, rightPos))
        //         {
        //             lastGuessPos = rightPos;
        //             invokeFieldGuessed(rightPos);
        //         }
        //         else if (match.inBounds(leftPos) && match.canGuess(this, leftPos))
        //         {
        //             lastGuessPos = leftPos;
        //             invokeFieldGuessed(leftPos);
        //         }
        //         else
        //         {
        //             var guessPos = getRandomGuessPos();
        //             lastGuessPos = guessPos;
        //             invokeFieldGuessed(guessPos);
        //         }
        //     }
        //     else
        //     {
        //         var guessPos = getRandomGuessPos();
        //         lastGuessPos = guessPos;
        //         invokeFieldGuessed(guessPos);
        //     }
    	// }
    }
    
    public Vector2Int getRandomGuessPos()
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
