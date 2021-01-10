package game;

import game.core.Vector2Int;

import java.util.ArrayList;
import java.util.LinkedList;
public abstract class Player implements IMatchListener
{
	protected String name;
	protected Match match;

	protected Board ownBoard;
	protected Board enemyBoard;

	protected boolean isGuessing;

	protected ShipType curShipType;
    protected LinkedList<ShipType> shipQueue = new LinkedList<ShipType>();

	private ArrayList<IPlayerListener> listeners = new ArrayList<IPlayerListener>();

	/**
	 * 
	 * @param name The name of the player
	 * @param match The match context
	 */
	public Player(String name, Match match)
	{
		this.name = name;
		this.match = match;

		shipQueue = Resources.getShipQueue();

		enemyBoard = new Board(GameConstants.boardSize, GameConstants.rightOffset, GameConstants.tileSize, false, false);

		addListener(match);
		match.addListener(this);
	}

	@Override
	public void onGameSetup(Player player)
	{
		if (player == this) isGuessing = true;
	}

	/**
	 * Gets invoked when the guessing player has changed
	 * @param player The new guessing player
	 */
	@Override
	public void onUpdate(Player player, Vector2Int position, boolean isHit, boolean isSunk)
	{
		isGuessing = player == this;
	}

	/**
	 * 
	 * @return Returns the name of the player
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Invokes the ShipPlaced event
	 * @param cellPos The cell position the ship was placed at 
	 * @param shipType The ship type that was placed
	 */
	protected void invokeClientBoard(Player player, Board board)
	{
		for (var listener : listeners)
		{
			listener.onClientBoard(player, board);
		}
	}

	/**
	 * Invokes the FieldGuessed event
	 * @param cellPos The cell position that was guessed
	 */
	protected void invokeMove(Vector2Int cellPos)
	{
		for (var listener : listeners)
		{
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
}
