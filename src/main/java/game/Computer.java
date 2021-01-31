package game;

import game.core.Vector2Int;
import java.util.Random;

public class Computer extends Player
{
    public Vector2Int lastGuessPos;
    public Vector2Int lastStartGuessPos;
    public Direction curDirection;

    public IAiState aiStartState;
    public IAiState aiHasGuessedCorrectlyState;
    public IAiState aiHasGuessedIncorrectlyState;

    public IAiState state;

    /**
     * @param name The name of the player
     * @param match The match context
     */
    public Computer(String name, Match match)
    {
        super(name, match);

        aiStartState = new AiStartState(this);
        aiHasGuessedCorrectlyState = new AiHasGuessedCorrectlyState(this);
        aiHasGuessedIncorrectlyState = new AiHasGuessedIncorrectlyState(this);

        ownBoard = new Board(GameConstants.boardSize, GameConstants.rightOffset, GameConstants.tileSize);

        setState(aiStartState);
    }

    /**
     * Initiates the AI logic
     */
    public void start()
    {
        assignRandomBoard();
    }

    /**
     * Sets the state of the AI
     * @param state The state to enter
     */
    public void setState(IAiState state) 
    {
        if (state == null) return;
        
        if (this.state != null) 
        {
            state.exitState();
        }

        this.state = state;
        state.enterState();
    }

    @Override
    public void onGameSetup(Player player)
    {
        super.onGameSetup(player);

        if (isGuessing) state.onUpdate(player, player, null, false, false);
    }

    @Override
    public void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        super.onUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk, isLate);

        if (cellPos != null)
        {
            if (lastPlayer != this) ownBoard.guessField(cellPos);
            else enemyBoard.guessField(cellPos);
        }
        
        if (isGuessing && isHit) setState(aiHasGuessedCorrectlyState);
        
        if (isGuessing)
        {
            state.onUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk);
        } 
    }
    
    /**
     * @return A randomly chosen cell position within the boards bounds
     */
    public Vector2Int getRandomGuessPos()
    {
        var rand = new Random();
        var cellPos = new Vector2Int(rand.nextInt(GameConstants.boardSize.x), rand.nextInt(GameConstants.boardSize.y));
        
        while (!enemyBoard.canGuess(cellPos))
        {
            cellPos = new Vector2Int(rand.nextInt(GameConstants.boardSize.x), rand.nextInt(GameConstants.boardSize.y));
        }

        return cellPos;
    }

    /**
     * Assigns a random board to the Computer player
     */
    private void assignRandomBoard()
    {
        do
        {
            curShipType = shipQueue.pop();
            
            var rand = new Random();
            var cellPos = new Vector2Int(rand.nextInt(GameConstants.boardSize.x), rand.nextInt(GameConstants.boardSize.y));
    
            while (!ownBoard.canPlace(cellPos, curShipType))
            {
                cellPos = new Vector2Int(rand.nextInt(GameConstants.boardSize.x), rand.nextInt(GameConstants.boardSize.y));
            }
    
            ownBoard.placeShip(cellPos, curShipType);

        }
        while (shipQueue.size() != 0);

        invokeClientBoard(this, ownBoard);
    }
}
