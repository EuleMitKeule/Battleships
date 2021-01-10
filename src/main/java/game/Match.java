package game;

import game.core.*;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Match implements IUpdatable, IPlayerListener
{
    protected Vector2Int boardSize;

    protected Board leftBoard;
    protected Board rightBoard;
    
    protected Player leftPlayer;
    protected Player rightPlayer;
    
    protected int leftShipCount;
    protected int rightShipCount;
    
    protected ArrayList<IMatchListener> listeners = new ArrayList<IMatchListener>();

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

        Game.addUpdatable(this);
    }
    
    /**
     * Gets invoked when a player placed a ship
     */
    // @Override
    // public void onShipPlaced(Player player, Vector2Int position, ShipType shipType)
    // {
    	// var isLeftPlayer = player == leftPlayer;
        // var board = isLeftPlayer ? leftBoard : rightBoard;

        // board.placeShip(position, shipType);

        // if (leftShipQueue.size() == 0 && rightShipQueue.size() == 0)
        // {
        // 	invokeUpdate(leftPlayer, false);
        // }
    // }

    @Override
    public void onClientBoard(Player player, Board board)
    {
        if (player == leftPlayer)
        {
            leftBoard = board;
        }
        else if (player == rightPlayer)
        {
            rightBoard = board;
        }
    }

    @Override
    public void onMove(Player player, Vector2Int cellPos)
    {
    	var isLeftPlayer = player == leftPlayer;
    	var board = isLeftPlayer ? rightBoard : leftBoard;

    	if (canGuess(player, cellPos))
        {
            var isSunk = board.isSinking(cellPos);
            var isHit = board.isHit(cellPos);

            var nextPlayer = isHit ? player : (isLeftPlayer ? rightPlayer : leftPlayer);
            
            System.out.println(nextPlayer.name + " is now guessing");

            if (isHit) 
                invokeUpdate(nextPlayer, cellPos, isHit, isSunk);
            else 
                invokeUpdate(nextPlayer, cellPos, isHit, isSunk);
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
    protected void invokeUpdate(Player player, Vector2Int position, boolean isHit, boolean isSunk)
    {
        for (var listener : listeners)
        {
            listener.onUpdate(player, position, isHit, isSunk);
        }
    }

    /**
     * Invokes the PlayerAdded event
     * @param player The added player
     * @param isLeftPlayer Whether the player is the left player
     */
    protected void invokePlayerAdded(Player player, boolean isLeftPlayer)
    {
        for (var listener : listeners)
        {
            listener.onPlayerAdded(player, isLeftPlayer);
        }
    }

    protected void invokeGameSetup(Player player)
    {
        for (var listener : listeners)
        {
            listener.onGameSetup(player);
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
}
