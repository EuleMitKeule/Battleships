package game;

import game.core.Vector2Int;

import java.util.ArrayList;

public abstract class Player implements IMatchListener
{
	protected String name;
	protected Match match;

	protected boolean isPlacing;
	protected boolean isGuessing;

	protected ShipType curShipType;

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

		match.addListener(this);
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
	 * Invokes the ShipPlaced event
	 * @param cellPos The cell position the ship was placed at 
	 * @param shipType The ship type that was placed
	 */
	protected void invokeShipPlaced(Vector2Int cellPos, ShipType shipType)
	{
		for (var listener : listeners)
		{
			listener.onShipPlaced(this, cellPos, shipType);
		}
	}

	/**
	 * Invokes the FieldGuessed event
	 * @param cellPos The cell position that was guessed
	 */
	protected void invokeFieldGuessed(Vector2Int cellPos)
	{
		for (var listener : listeners)
		{
			listener.onFieldGuessed(this, cellPos);
		}
	}

	/**
	 * Gets invoked when the placing player has changed
	 * @param player The new placing player
	 * @param shipType The new ship type to be placed 
	 */
	@Override
	public void onPlacingPlayerChanged(Player player, ShipType shipType)
	{
		isPlacing = player == this;
		isGuessing = false;

		if (isPlacing) curShipType = shipType;
	}

	/**
	 * Gets invoked when the guessing player has changed
	 * @param player The new guessing player
	 */
	@Override
	public void onGuessingPlayerChanged(Player player, boolean hasHit)
	{
		isGuessing = player == this;
		isPlacing = false;
	}
}
