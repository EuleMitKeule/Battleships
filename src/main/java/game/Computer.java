package game;

import game.core.Vector2Int;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

        ownBoard = new Board(GameConstants.boardSize, GameConstants.rightOffset, GameConstants.tileSize, false, true);

        setState(aiStartState);
    }

    public void Start()
    {
        assignRandomBoard();
    }

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

    /**
     * Gets invoked when the guessing player has changed
     * @param player The new placing player
     */
    @Override
    public void onUpdate(Player player, Vector2Int cellPos, boolean isHit, boolean isSunk)
    {
        super.onUpdate(player, cellPos, isHit, isSunk);

        if (player != this && isHit || player == this && !isHit) ownBoard.guessField(cellPos);

        if (isGuessing && isHit) setState(aiHasGuessedCorrectlyState);

        if (isGuessing)
        {
            // try {
            //     TimeUnit.SECONDS.sleep(2);
                
            // } catch (Exception e) {
            //     //TODO: handle exception
            // }
            System.out.println("computer guessed jetzt im " + state.getClass().getName());
            state.onUpdate(player, cellPos, isHit, isSunk);
        } 
    }
    
    public Vector2Int getRandomGuessPos()
    {
        var rand = new Random();
        var cellPos = new Vector2Int(rand.nextInt(GameConstants.boardSize.x), rand.nextInt(GameConstants.boardSize.y));
        
        while (!ownBoard.canGuess(cellPos))
        {
            System.out.println("wrong pos");
            cellPos = new Vector2Int(rand.nextInt(GameConstants.boardSize.x), rand.nextInt(GameConstants.boardSize.y));
        }

        return cellPos;
    }

    void assignRandomBoard()
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
