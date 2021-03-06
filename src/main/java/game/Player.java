package game;

import game.core.Vector2Int;

import java.util.ArrayList;
import java.util.LinkedList;
public abstract class Player implements IMatchListener
{
	public String name;
	protected Match match;

	protected Board ownBoard;
	protected Board enemyBoard;

	protected boolean isGuessing;

	protected ShipType curShipType;
    protected LinkedList<ShipType> shipQueue = new LinkedList<ShipType>();

	private ArrayList<IPlayerListener> listeners = new ArrayList<IPlayerListener>();

	/**
	 * @param name The name of the player
	 * @param match The match context
	 */
	public Player(String name, Match match)
	{
		this.name = name;
		this.match = match;

		shipQueue = Resources.getShipQueue();

		enemyBoard = new Board(GameConstants.boardSize, GameConstants.rightOffset, GameConstants.tileSize);

		addListener(match);
		match.addListener(this);
	}

	@Override
	public void onGameSetup(Player player)
	{
		if (player == this) isGuessing = true;
	}

	@Override
	public void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk, boolean isLate)
	{
		if (nextPlayer == null) isGuessing = false;
		else isGuessing = nextPlayer == this;
	}

	@Override
	public void onGameOver(Result result)
	{
		dispose();
	}

	/**
	 * Invokes the ShipPlaced event
	 * @param player The player object
	 * @param board The board object
	 */
	protected void invokeClientBoard(Player player, Board board)
	{
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
			listener.onClientBoard(player, board);
		}
	}

	/**
	 * Invokes the FieldGuessed event
	 * @param cellPos The cell position that was guessed
	 */
	protected void invokeMove(Vector2Int cellPos)
	{
		for (int i = 0; i < listeners.size(); i++)
		{
			var listener = listeners.get(i);
			if (listener == null) return;
			listener.onMove(this, cellPos);
		}
	}

	/**
	 * Adds an IPlayerListener to the list of observers
	 * @param listener The listener to add
	 */
	public void addListener(IPlayerListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * Removes an IPlayerListener from the list of observers
	 * @param listener The listener to remove
	 */
	public void removeListener(IPlayerListener listener)
	{
		listeners.remove(listener);
	}

	/**
	 * Event subscription cleanup
	 */
	public void dispose()
	{
		match.removeListener(this);
	}
}
