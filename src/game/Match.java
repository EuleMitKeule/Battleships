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
    
    private int leftShipCount;
    private int rightShipCount;
    
    private final LinkedList<ShipType> leftShipQueue = new LinkedList<ShipType>();
    private final LinkedList<ShipType> rightShipQueue = new LinkedList<ShipType>();


    private static ArrayList<IMatchListener> listeners = new ArrayList<IMatchListener>();

    public Match(Vector2Int boardSize)
    {
        this.boardSize = boardSize;

        leftBoard = new Board(boardSize, true);
        leftBoard.addListener(this);
        rightBoard = new Board(boardSize, false);
        rightBoard.addListener(this);
        
        leftPlayer = new Human("eule", this);
        leftPlayer.addListener(this);

        invokePlayerAdded(leftPlayer, true);

        rightPlayer = new Computer("com", this);
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
        
        rightShipCount = rightShipQueue.size();
        leftShipCount = leftShipQueue.size();
        invokeShipCountChanged(rightShipCount, false);
        invokeShipCountChanged(leftShipCount, true);

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
        var board = isLeftPlayer ? leftBoard : rightBoard;
        board.placeShip(position, shipType);
        if(leftShipQueue.size() == 0 && rightShipQueue.size() == 0)
        {
        	invokeGuessingPlayerChanged(leftPlayer);
        }
        else invokePlacingPlayerChanged(isLeftPlayer ? rightPlayer : leftPlayer, isLeftPlayer ? rightShipQueue.pop() : leftShipQueue.pop());
        
    }

    @Override
    public void onGuess(Player player, Vector2Int pos)
    {
    	var isLeftPlayer = player == leftPlayer;
    	var board = isLeftPlayer ? rightBoard : leftBoard;

    	if (canGuess(player, pos))
        {
            var shipHit = board.guessField(pos);
            if(shipHit) invokeGuessingPlayerChanged(isLeftPlayer ? leftPlayer : rightPlayer);
            else invokeGuessingPlayerChanged(isLeftPlayer ? rightPlayer : leftPlayer);
        }
    }
    
    public boolean canGuess(Player player, Vector2Int pos)
    {
    	var isLeftPlayer = player == leftPlayer;
        var board = isLeftPlayer ? rightBoard : leftBoard;
        return board.canGuess(pos);
    }
    
    public boolean canPlace(Player player, Vector2Int pos, ShipType shipType)
    {
    	var isLeftPlayer = player == leftPlayer;
        var board = isLeftPlayer ? leftBoard : rightBoard;
        return board.canPlace(pos, shipType);
    }

    public Vector2Int getBoardSize()
    {
    	return boardSize;
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
    
    private void invokeShipCountChanged(int shipCount, boolean isLeft)
    {
    	for (var listener : listeners)
        {
            listener.onShipCountChanged(shipCount, isLeft);
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

	@Override
	public void onShipDestroyed(Board board) 
	{
		var isLeftBoard = board == leftBoard;
		if(isLeftBoard) leftShipCount -= 1;
		else rightShipCount -= 1;
		invokeShipCountChanged(isLeftBoard ? leftShipCount : rightShipCount, isLeftBoard);
	}
}
