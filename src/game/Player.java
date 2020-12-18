package game;

import game.core.Vector2Int;

import java.util.ArrayList;

public abstract class Player implements IMatchListener
{
	protected String name;
	protected Board board;

	protected boolean isPlacing;
	protected boolean isGuessing;

	protected ShipType curShipType;

	private ArrayList<IPlayerListener> listeners = new ArrayList<IPlayerListener>();

	public Player(String name, Board board)
	{
		this.name = name;
		this.board = board;

		Match.addListener(this);
	}

	public String getName()
	{
		return this.name;
	}

	public void addListener(IPlayerListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(IPlayerListener listener)
	{
		listeners.remove(listener);
	}

	protected void invokeShipPlaced(Vector2Int position, ShipType shipType)
	{
		for (var listener : listeners)
		{
			listener.onShipPlaced(this, position, shipType);
		}
	}

	protected void invokeGuess(Vector2Int position)
	{
		for (var listener : listeners)
		{
			listener.onGuess(this, position);
		}
	}

	@Override
	public void onPlacingPlayerChanged(Player player, ShipType shipType)
	{
		isPlacing = player == this;
		isGuessing = false;

		if (isPlacing)
		{
			curShipType = shipType;
		}
	}

	@Override
	public void onGuessingPlayerChanged(Player player)
	{
		isGuessing = player == this;
		isPlacing = false;
	}
}
