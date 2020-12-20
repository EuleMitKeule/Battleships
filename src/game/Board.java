package game;

import game.core.*;

import java.util.ArrayList;

public class Board implements IRenderable
{
	private FieldState[][] fields;
	private final Vector2Int size;
	private boolean owned;

	private ArrayList<IBoardListener> listeners = new ArrayList<IBoardListener>();

	public Board(int sizeX, int sizeY, boolean owned)
	{
		this(new Vector2Int(sizeX, sizeY), owned);
	}

	public Board(Vector2Int size, boolean owned)
	{
		Game.addRenderable(this);

		this.size = size;
		this.owned = owned;

		fields = new FieldState[size.x][size.y];

		setFields(FieldState.WATER);
	}
	
	public void setField(Vector2Int pos, FieldState state)
	{
		fields[pos.x][pos.y] = state;
		InvokeFieldChanged(pos, state);
	}

	public void setField(int x, int y, FieldState state)
	{
		fields[x][y] = state;
		InvokeFieldChanged(new Vector2Int(x, y), state);
	}

	public void setFields(FieldState state)
	{
		for(int x = 0; x < size.x; x++)
		{
			for (int y = 0; y < size.y; y++)
			{
				setField(x, y, state);
			}
		}
	}

	public FieldState getField(Vector2Int pos)
	{
		try
		{
			return fields[pos.x][pos.y];
		}
		catch (IndexOutOfBoundsException ex)
		{
			return null;
		}
	}

	public FieldState getField(int x, int y)
	{
		try
		{
			return fields[x][y];
		}
		catch (IndexOutOfBoundsException ex)
		{
			return null;
		}
	}

	public FieldState[][] getFields()
	{
		return fields;
	}

	public Vector2Int getSize()
	{
		return size;
	}

	public boolean canPlace(Vector2Int position, ShipType shipType)
	{
		switch (shipType)
		{
			case PATROL:
				return getField(position) == FieldState.WATER;
			case SUPER_PATROL:
				return getField(position) == FieldState.WATER &&
						getField(position.add(Vector2Int.right())) == FieldState.WATER;
			case DESTROYER:
				return getField(position) == FieldState.WATER &&
						getField(position.add(Vector2Int.right())) == FieldState.WATER &&
						getField(position.add(Vector2Int.right().times(2))) == FieldState.WATER;
			case BATTLESHIP:
				return getField(position) == FieldState.WATER &&
						getField(position.add(Vector2Int.right())) == FieldState.WATER &&
						getField(position.add(Vector2Int.right().times(2))) == FieldState.WATER &&
						getField(position.add(Vector2Int.right().times(3))) == FieldState.WATER;
			case CARRIER:
				return getField(position) == FieldState.WATER &&
						getField(position.add(Vector2Int.right())) == FieldState.WATER &&
						getField(position.add(Vector2Int.right().times(2))) == FieldState.WATER &&
						getField(position.add(Vector2Int.right().times(3))) == FieldState.WATER &&
						getField(position.add(Vector2Int.right().times(4))) == FieldState.WATER;
			default:
				return false;
		}
	}

	public void placeShip(Vector2Int position, ShipType shipType)
	{
		if (canPlace(position, shipType))
		{
			switch (shipType)
			{
				case PATROL:
					setField(position, FieldState.SHIP);
					break;
				case SUPER_PATROL:
					setField(position, FieldState.SHIP);
					setField(position.add(Vector2Int.right()), FieldState.SHIP);
					break;
				case DESTROYER:
					setField(position, FieldState.SHIP);
					setField(position.add(Vector2Int.right()), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(2)), FieldState.SHIP);
					break;
				case BATTLESHIP:
					setField(position, FieldState.SHIP);
					setField(position.add(Vector2Int.right()), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(2)), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(3)), FieldState.SHIP);
					break;
				case CARRIER:
					setField(position, FieldState.SHIP);
					setField(position.add(Vector2Int.right()), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(2)), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(3)), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(4)), FieldState.SHIP);
					break;
			}
		}
	}

	public void addListener(IBoardListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeListener(IBoardListener listener)
	{
		listeners.remove(listener);
	}

	public void InvokeFieldChanged(Vector2Int pos, FieldState state)
	{
		for(var listener:listeners)
		{
			listener.onFieldChanged(this, pos, state);
		}
	}

	@Override
	public void render(BoardRenderer renderer)
	{
		for (int x = 0; x < size.x; x++)
		{
			for (int y = 0; y < size.y; y++)
			{
				var field = getField(x, y);
				var position = new Vector2Int(x, y);

				var sprite = Resources.SPRITE_NULL;

				switch (field)
				{
					case WATER -> sprite = Resources.WATER_DUMMY;
					case SHIP -> sprite = Resources.SHIP_DUMMY;
				}

				if (owned)
				{
					renderer.drawLeftField(sprite, position);
				}
				else
				{
					renderer.drawRightField(sprite, position);
				}
			}
		}
	}
}
