package game;

import game.core.Vector2Int;

import java.util.Random;

public class Computer extends Player
{
    public Vector2Int lastGuessPos;
    public Vector2Int lastStartGuessPos;
    public Direction curDirection;

    public IAiState aiStartState;
    public IAiState aiHasGuessedCorrectyState;
    public IAiState aiHasGuessedIncorrectlyState;

    
    private IAiState _state;
    /**
     * @param name The name of the player
     * @param match The match context
     */
    public Computer(String name, Match match)
    {
        super(name, match);

        aiStartState = new AiStartState(this);
        aiHasGuessedCorrectyState = new AiHasGuessedCorrectlyState(this);
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
        
        if (this._state != null) 
        {
            state.exitState();
        }

        this._state = state;
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

        if (player == this) ownBoard.guessField(cellPos);

        if (isGuessing && isHit) setState(aiHasGuessedCorrectyState);

        if (isGuessing)
        {
            System.out.println("computer guessed jetzt");
            _state.onUpdate(player, cellPos, isHit, isSunk);
        } 
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

    void assignRandomBoard()
    {
        do
        {
            curShipType = shipQueue.pop();
            
            var rand = new Random();
            var cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
    
            while (!ownBoard.canPlace(cellPos, curShipType))
            {
                cellPos = new Vector2Int(rand.nextInt(match.getBoardSize().x), rand.nextInt(match.getBoardSize().y));
            }
    
            ownBoard.placeShip(cellPos, curShipType);

        }
        while (shipQueue.size() != 0);

        invokeClientBoard(this, ownBoard);
    }
}
