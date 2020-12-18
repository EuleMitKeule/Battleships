package game;

import game.core.Vector2Int;

import java.util.ArrayList;

public abstract class Player implements IMatchListener
{
	protected String name;
	protected Board board;
	protected boolean isPlacing;
	protected boolean isGuessing;

	private ArrayList<IPlayerListener> listeners = new ArrayList<IPlayerListener>();

	public Player(String name, Board board)
	{
		this.name = name;
		this.board = board;

		Match.addListener(this);
	}

	public void addListener(IPlayerListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(IPlayerListener listener)
	{
		listeners.remove(listener);
	}

	protected void invokeShipPlaced(Vector2Int position)
	{
		for (var listener : listeners)
		{
			listener.onShipPlaced(this, position);
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
	public void onPlacingPlayerChanged(Player player)
	{
		isPlacing = player == this;
		isGuessing = false;

		if (isPlacing)
		{
			System.out.println("Player " + name + " is now placing a ship");
		}
	}

	@Override
	public void onGuessingPlayerChanged(Player player)
	{
		isGuessing = player == this;
		isPlacing = false;
	}
}
