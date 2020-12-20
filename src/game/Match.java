package game;

import game.core.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Match implements IUpdatable, IPlayerListener, IBoardListener
{
    private Vector2Int boardSize;

    private Board leftBoard;
    private Board rightBoard;

    private Player leftPlayer;
    private Player rightPlayer;
    
    private final LinkedList<ShipType> leftShipQueue = new LinkedList<ShipType>();
    private final LinkedList<ShipType> rightShipQueue = new LinkedList<ShipType>();


    private static ArrayList<IMatchListener> listeners = new ArrayList<IMatchListener>();

    public Match(Vector2Int boardSize)
    {
        this.boardSize = boardSize;

        leftBoard = new Board(boardSize, true);
        rightBoard = new Board(boardSize, false);

        leftPlayer = new Human("eule", leftBoard);
        leftPlayer.addListener(this);

        invokePlayerAdded(leftPlayer, true);

        rightPlayer = new Computer("com", rightBoard);
        rightPlayer.addListener(this);

        invokePlayerAdded(rightPlayer, false);

        Game.addUpdatable(this);
        
        for(int i = 0; i < 2; i++)
        {
        	leftShipQueue.add(ShipType.CARRIER);
        	rightShipQueue.add(ShipType.CARRIER);
        }
        for(int i = 0; i < 3; i++)
        {
        	leftShipQueue.add(ShipType.BATTLESHIP);
        	rightShipQueue.add(ShipType.BATTLESHIP);
        }
        for(int i = 0; i < 5; i++)
        {
        	leftShipQueue.add(ShipType.DESTROYER);
        	rightShipQueue.add(ShipType.DESTROYER);
        }
        for(int i = 0; i < 8; i++)
        {
        	leftShipQueue.add(ShipType.SUPER_PATROL);
        	rightShipQueue.add(ShipType.SUPER_PATROL);
        }
        for(int i = 0; i < 10; i++)
        {
        	leftShipQueue.add(ShipType.PATROL);
        	rightShipQueue.add(ShipType.PATROL);
        }
        
        invokePlacingPlayerChanged(leftPlayer, leftShipQueue.pop());
    }

    @Override
    public void update(long elapsedMillis)
    {

    }
    
    /**
     * Gets invoked when a player placed a ship.
     */
    @Override
    public void onShipPlaced(Player player, Vector2Int position, ShipType shipType)
    {
    	var isLeftPlayer = player == leftPlayer;
        System.out.println("Player " + player.name + " placed a ship of type " + shipType);
        var board = isLeftPlayer ? leftBoard : rightBoard;
        board.placeShip(position, shipType);
        invokePlacingPlayerChanged(isLeftPlayer ? rightPlayer : leftPlayer, isLeftPlayer ? rightShipQueue.pop() : leftShipQueue.pop());
    }

    @Override
    public void onGuess(Player player, Vector2Int position)
    {
    	
    }

    private void invokeGuessingPlayerChanged(Player player)
    {
        for (var listener : listeners)
        {
            listener.onGuessingPlayerChanged(player);
        }
    }

    private void invokePlacingPlayerChanged(Player player, ShipType shipType)
    {
        for (var listener : listeners)
        {
            listener.onPlacingPlayerChanged(player, shipType);
        }
    }

    private void invokePlayerAdded(Player player, boolean isLeftPlayer)
    {
        for (var listener : listeners)
        {
            listener.onPlayerAdded(player, isLeftPlayer);
        }
    }

    public static void addListener(IMatchListener listener)
    {
        listeners.add(listener);
    }

    public static void removeListener(IMatchListener listener)
    {
        listeners.remove(listener);
    }

    @Override
    public void onFieldChanged(Board board, Vector2Int pos, FieldState state)
    {

    }
}
