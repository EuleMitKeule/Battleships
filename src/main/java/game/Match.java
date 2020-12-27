package game;

import game.core.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class Match implements IUpdatable, IPlayerListener, IBoardListener
{
    private Vector2Int boardSize;

    private Board leftBoard;
    private Board rightBoard;

    private Player leftPlayer;
    private Player rightPlayer;
    
    private int leftShipCount;
    private int rightShipCount;
    
    private LinkedList<ShipType> leftShipQueue = new LinkedList<ShipType>();
    private LinkedList<ShipType> rightShipQueue = new LinkedList<ShipType>();

    private ArrayList<IMatchListener> listeners = new ArrayList<IMatchListener>();

    /**
     * 
     * @param game The game context
     * @param leftOffset The pixel offset of the left board
     * @param rightOffset The pixel offset of the right board
     * @param tileSize The pixel size of tiles
     * @param boardSize The cell dimensions of the board
     */
    public Match(Game game, Vector2Int leftOffset, Vector2Int rightOffset, int tileSize, Vector2Int boardSize)
    {
        this.boardSize = boardSize;

		new UI(game, this);

        leftBoard = new Board(boardSize, leftOffset, tileSize, true);
        leftBoard.addListener(this);
        rightBoard = new Board(boardSize, rightOffset, tileSize, false);
        rightBoard.addListener(this);
        
        leftPlayer = new Human("eule", this);
        leftPlayer.addListener(this);

        invokePlayerAdded(leftPlayer, true);

        rightPlayer = new Computer("com", this);
        rightPlayer.addListener(this);

        invokePlayerAdded(rightPlayer, false);

        Game.addUpdatable(this);
        
        leftShipQueue = Resources.getShipQueue();
        rightShipQueue = Resources.getShipQueue();
        
        rightShipCount = rightShipQueue.size();
        invokeShipCountChanged(rightShipCount, false);

        leftShipCount = leftShipQueue.size();
        invokeShipCountChanged(leftShipCount, true);

        invokePlacingPlayerChanged(leftPlayer, leftShipQueue.pop());
    }
    
    /**
     * Gets invoked when a player placed a ship
     */
    @Override
    public void onShipPlaced(Player player, Vector2Int position, ShipType shipType)
    {
    	var isLeftPlayer = player == leftPlayer;
        var board = isLeftPlayer ? leftBoard : rightBoard;

        board.placeShip(position, shipType);

        if (leftShipQueue.size() == 0 && rightShipQueue.size() == 0)
        {
        	invokeGuessingPlayerChanged(leftPlayer, false);
        }
        else
        {
            invokePlacingPlayerChanged(isLeftPlayer ? rightPlayer : leftPlayer, 
                                        isLeftPlayer ? rightShipQueue.pop() : leftShipQueue.pop());
        }
    }

    /**
     * Gets invoked when a player guessed a field
     * @param player The player to have guessed
     * @param cellPos The cell position that was guessed
     */
    @Override
    public void onFieldGuessed(Player player, Vector2Int cellPos)
    {
    	var isLeftPlayer = player == leftPlayer;
    	var board = isLeftPlayer ? rightBoard : leftBoard;

    	if (canGuess(player, cellPos))
        {
            var shipHit = board.guessField(cellPos);

            if (shipHit) 
                invokeGuessingPlayerChanged(isLeftPlayer ? leftPlayer : rightPlayer, true);
            else 
                invokeGuessingPlayerChanged(isLeftPlayer ? rightPlayer : leftPlayer, false);
        }
    }
    
    /**
     * 
     * @param player The player wanting to guess
     * @param cellPos The cell position to be guessed
     * @return Returns whether the field is guessable
     */
    public boolean canGuess(Player player, Vector2Int cellPos)
    {
    	var isLeftPlayer = player == leftPlayer;
        var board = isLeftPlayer ? rightBoard : leftBoard;
        return board.canGuess(cellPos);
    }
    
    /**
     * 
     * @param player The player wanting to place
     * @param cellPos The cell position to be placed at
     * @param shipType The ship type to be placed
     * @return Returns whether the ship can be placed at that field
     */
    public boolean canPlace(Player player, Vector2Int cellPos, ShipType shipType)
    {
    	var isLeftPlayer = player == leftPlayer;
        var board = isLeftPlayer ? leftBoard : rightBoard;
        return board.canPlace(cellPos, shipType);
    }

    /**
     * 
     * @param position The world position to check
     * @return Returns whether the given position is inside the bounds of the left board
     */
    public boolean inLeftBounds(Vector2 position)
    {
        return leftBoard.inBounds(position); 
    }

    /**
     * @param position The world position to check
     * @return Returns whether the given position is inside the bounds of the right board
     */
    public boolean inRightBounds(Vector2 position)
    {
        return rightBoard.inBounds(position);
    }

    /**
     * @param cellPos The cell position to check
     * @return Returns whether the given cell position is inside the bounds of any board
     */
    public boolean inBounds(Vector2Int cellPos)
    {
        return cellPos.x > 0 && cellPos.x < boardSize.x &&
                cellPos.y > 0 && cellPos.y < boardSize.y;
    }

    /**
     * 
     * @param position The world position to convert
     * @return Returns the converted cell position or null if the given position is not inside any board
     */
    public Vector2Int worldToCell(Vector2 position)
    {
        if (inLeftBounds(position)) return leftBoard.worldToCell(position);
        else if (inRightBounds(position)) return rightBoard.worldToCell(position);
        else return null;
    }

    /**
     * 
     * @return The size of the game boards
     */
    public Vector2Int getBoardSize()
    {
    	return boardSize;
    }
    
    /**
     * Invokes the GuessingPlayerChanged event
     * @param player The player that guesses next
     */
    private void invokeGuessingPlayerChanged(Player player, boolean hasHit)
    {
        for (var listener : listeners)
        {
            listener.onGuessingPlayerChanged(player, hasHit);
        }
    }

    /**
     * Invokes the PlacingPlayerChanged event
     * @param player The player that places next
     * @param shipType The ship to be placed
     */
    private void invokePlacingPlayerChanged(Player player, ShipType shipType)
    {
        for (var listener : listeners)
        {
            listener.onPlacingPlayerChanged(player, shipType);
        }
    }

    /**
     * Invokes the PlayerAdded event
     * @param player The added player
     * @param isLeftPlayer Whether the player is the left player
     */
    private void invokePlayerAdded(Player player, boolean isLeftPlayer)
    {
        for (var listener : listeners)
        {
            listener.onPlayerAdded(player, isLeftPlayer);
        }
    }
    
    /**
     * Invokes the ShipCountChanged event
     * @param shipCount The new ship count
     * @param isLeft Whether the ship count is for the left or right side
     */
    private void invokeShipCountChanged(int shipCount, boolean isLeft)
    {
    	for (var listener : listeners)
        {
            listener.onShipCountChanged(shipCount, isLeft);
        }
    }

    /**
     * Adds an IMatchListener to the match observer list
     * @param listener The listener to add
     */
    public void addListener(IMatchListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes an IMatchListener from the match observer list
     * @param listener The listener to remove
     */
    public void removeListener(IMatchListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Gets invoked when a ship is completely guessed
     */
	@Override
	public void onShipDestroyed(Board board) 
	{
        var isLeftBoard = board == leftBoard;

		if (isLeftBoard) leftShipCount -= 1;
        else rightShipCount -= 1;
        
		invokeShipCountChanged(isLeftBoard ? leftShipCount : rightShipCount, isLeftBoard);
    }
}
