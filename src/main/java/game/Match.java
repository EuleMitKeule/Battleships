package game;

import game.core.*;

import java.util.ArrayList;

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

        var isSunk = board.isSinking(cellPos);
        var isHit = board.isHit(cellPos);
        
        var nextPlayer = isHit ? player : (isLeftPlayer ? rightPlayer : leftPlayer);

        if (isHit) 
            invokeUpdate(player, nextPlayer, cellPos, isHit, isSunk);
        else 
            invokeUpdate(player, nextPlayer, cellPos, isHit, isSunk);
    }
    
    /**
     * Invokes the GuessingPlayerChanged event
     * @param player The player that guesses next
     */
    protected void invokeUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk)
    {
        for (var listener : listeners)
        {
            listener.onUpdate(lastPlayer, nextPlayer, position, isHit, isSunk);
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
