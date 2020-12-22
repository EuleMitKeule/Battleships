package game;

import game.core.*;

import java.util.ArrayList;

public class Board implements IRenderable
{
	private ShipType[][] ships;
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
		ships = new ShipType[size.x][size.y];

		setFields(FieldState.WATER);
	}
	
	public void setField(Vector2Int pos, FieldState state)
	{
		
		fields[pos.x][pos.y] = state;
		invokeFieldChanged(pos, state);
	}

	public void setField(int x, int y, FieldState state)
	{
		fields[x][y] = state;
		invokeFieldChanged(new Vector2Int(x, y), state);
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
	
	public void setShip(Vector2Int pos, ShipType shipType)
	{
		
		ships[pos.x][pos.y] = shipType;
	}

	public void setShip(int x, int y, ShipType shipType)
	{
		ships[x][y] = shipType;
	}

	public void setShips(ShipType shipType)
	{
		for(int x = 0; x < size.x; x++)
		{
			for (int y = 0; y < size.y; y++)
			{
				setShip(x, y, shipType);
			}
		}
	}

	public ShipType getShip(Vector2Int pos)
	{
		try
		{
			return ships[pos.x][pos.y];
		}
		catch (IndexOutOfBoundsException ex)
		{
			return null;
		}
	}

	public ShipType getShip(int x, int y)
	{
		try
		{
			return ships[x][y];
		}
		catch (IndexOutOfBoundsException ex)
		{
			return null;
		}
	}

	public ShipType[][] getShips()
	{
		return ships;
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
					setShip(position, ShipType.PATROL);
					break;
				case SUPER_PATROL:
					setField(position, FieldState.SHIP);
					setField(position.add(Vector2Int.right()), FieldState.SHIP);
					setShip(position, ShipType.SUPER_PATROL_FRONT);
					setShip(position.add(Vector2Int.right()), ShipType.SUPER_PATROL_BACK);
					break;
				case DESTROYER:
					setField(position, FieldState.SHIP);
					setField(position.add(Vector2Int.right()), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(2)), FieldState.SHIP);
					setShip(position, ShipType.DESTROYER_FRONT);
					setShip(position.add(Vector2Int.right()), ShipType.DESTROYER_MID);
					setShip(position.add(Vector2Int.right().times(2)), ShipType.DESTROYER_BACK);
					break;
				case BATTLESHIP:
					setField(position, FieldState.SHIP);
					setField(position.add(Vector2Int.right()), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(2)), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(3)), FieldState.SHIP);
					setShip(position, ShipType.BATTLESHIP_FRONT);
					setShip(position.add(Vector2Int.right()), ShipType.BATTLESHIP_FRONT_MID);
					setShip(position.add(Vector2Int.right().times(2)), ShipType.BATTLESHIP_BACK_MID);
					setShip(position.add(Vector2Int.right().times(3)), ShipType.BATTLESHIP_FRONT);
					break;
				case CARRIER:
					setField(position, FieldState.SHIP);
					setField(position.add(Vector2Int.right()), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(2)), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(3)), FieldState.SHIP);
					setField(position.add(Vector2Int.right().times(4)), FieldState.SHIP);
					setShip(position, ShipType.CARRIER_FRONT);
					setShip(position.add(Vector2Int.right()), ShipType.CARRIER_FRONT_MID);
					setShip(position.add(Vector2Int.right().times(2)), ShipType.CARRIER_MID);
					setShip(position.add(Vector2Int.right().times(3)), ShipType.CARRIER_BACK_MID);
					setShip(position.add(Vector2Int.right().times(4)), ShipType.CARRIER_BACK);
					break;
			default:
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

	private void invokeFieldChanged(Vector2Int pos, FieldState state)
	{
		for(var listener:listeners)
		{
			listener.onFieldChanged(this, pos, state);
		}
	}
	
	private void invokeShipDestroyed()
	{
		for(var listener:listeners)
		{
			listener.onShipDestroyed(this);
		}
	}
	
	public boolean canGuess(Vector2Int pos)
	{
		return getField(pos) == FieldState.WATER || getField(pos) == FieldState.SHIP;
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
					case SHIP -> sprite = owned ? Resources.SHIP_DUMMY : Resources.WATER_DUMMY;
					case WATER_GUESSED -> sprite = Resources.WATER_GUESSED_DUMMY;
					case SHIP_GUESSED -> sprite = Resources.SHIP_GUESSED_DUMMY;
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

	public boolean guessField(Vector2Int pos) 
	{
		switch(getField(pos)) 
    	{
	    	case WATER:
	    		setField(pos, FieldState.WATER_GUESSED);
	    		return false;
	    	case SHIP:
	    		setField(pos, FieldState.SHIP_GUESSED);
	    		if(isLastShipField(pos)) invokeShipDestroyed();

	    		setShip(pos, ShipType.SHIP_DESTROYED);
	    		
	    		return true;
	    	default:
	    		return false;
    	}
		
	}
	
	
	
	private boolean isLastShipField(Vector2Int pos)
	{
		var startField = getShip(pos);
		
		if(startField == ShipType.PATROL) return true;
		
		if(startField == ShipType.SUPER_PATROL_FRONT) {
			if(getShip(pos.add(Vector2Int.right())) != ShipType.SUPER_PATROL_BACK) return true;
		}
		
		if(startField == ShipType.SUPER_PATROL_BACK) {
			if(getShip(pos.add(Vector2Int.left())) != ShipType.SUPER_PATROL_FRONT) return true;
		}
		
		if(startField == ShipType.DESTROYER_FRONT) {
			if(getShip(pos.add(Vector2Int.right())) != ShipType.DESTROYER_MID &&
					getShip(pos.add(Vector2Int.right().times(2))) != ShipType.DESTROYER_BACK) return true;
		}
		
		if(startField == ShipType.DESTROYER_MID) {
			if(getShip(pos.add(Vector2Int.right())) != ShipType.DESTROYER_BACK &&
					getShip(pos.add(Vector2Int.left())) != ShipType.DESTROYER_FRONT) return true;
		}
		
		if(startField == ShipType.DESTROYER_BACK) {
			if(getShip(pos.add(Vector2Int.left())) != ShipType.DESTROYER_MID &&
					getShip(pos.add(Vector2Int.left().times(2))) != ShipType.DESTROYER_FRONT) return true;
		}
		
		if(startField == ShipType.BATTLESHIP_FRONT) {
			if(getShip(pos.add(Vector2Int.right())) != ShipType.BATTLESHIP_FRONT_MID &&
					getShip(pos.add(Vector2Int.right().times(2))) != ShipType.BATTLESHIP_BACK_MID &&
					getShip(pos.add(Vector2Int.right().times(3))) != ShipType.BATTLESHIP_BACK) return true;
		}
		
		if(startField == ShipType.BATTLESHIP_FRONT_MID) {
			if(getShip(pos.add(Vector2Int.left())) != ShipType.BATTLESHIP_FRONT &&
					getShip(pos.add(Vector2Int.right())) != ShipType.BATTLESHIP_BACK_MID &&
					getShip(pos.add(Vector2Int.left().times(2))) != ShipType.BATTLESHIP_BACK) return true;
		}
		
		if(startField == ShipType.BATTLESHIP_BACK_MID) {
			if(getShip(pos.add(Vector2Int.left())) != ShipType.BATTLESHIP_FRONT_MID &&
					getShip(pos.add(Vector2Int.left().times(2))) != ShipType.BATTLESHIP_FRONT &&
					getShip(pos.add(Vector2Int.right())) != ShipType.BATTLESHIP_BACK) return true;
		}
		
		if(startField == ShipType.BATTLESHIP_BACK) {
			if(getShip(pos.add(Vector2Int.left())) != ShipType.BATTLESHIP_BACK_MID &&
					getShip(pos.add(Vector2Int.left().times(2))) != ShipType.BATTLESHIP_FRONT_MID &&
					getShip(pos.add(Vector2Int.left().times(3))) != ShipType.BATTLESHIP_FRONT) return true;
		}
		
		if(startField == ShipType.CARRIER_FRONT) {
			if(getShip(pos.add(Vector2Int.right())) != ShipType.CARRIER_FRONT_MID &&
					getShip(pos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_MID &&
					getShip(pos.add(Vector2Int.right().times(3))) != ShipType.CARRIER_BACK_MID &&
					getShip(pos.add(Vector2Int.right().times(4))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_FRONT) {
			if(getShip(pos.add(Vector2Int.right())) != ShipType.CARRIER_FRONT_MID &&
					getShip(pos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_MID &&
					getShip(pos.add(Vector2Int.right().times(3))) != ShipType.CARRIER_BACK_MID &&
					getShip(pos.add(Vector2Int.right().times(4))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_FRONT) {
			if(getShip(pos.add(Vector2Int.right())) != ShipType.CARRIER_FRONT_MID &&
					getShip(pos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_MID &&
					getShip(pos.add(Vector2Int.right().times(3))) != ShipType.CARRIER_BACK_MID &&
					getShip(pos.add(Vector2Int.right().times(4))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_FRONT_MID) {
			if(getShip(pos.add(Vector2Int.left())) != ShipType.CARRIER_FRONT &&
					getShip(pos.add(Vector2Int.right())) != ShipType.CARRIER_MID &&
					getShip(pos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_BACK_MID &&
					getShip(pos.add(Vector2Int.right().times(3))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_MID) {
			if(getShip(pos.add(Vector2Int.left().times(2))) != ShipType.CARRIER_FRONT &&
					getShip(pos.add(Vector2Int.left())) != ShipType.CARRIER_FRONT_MID &&
					getShip(pos.add(Vector2Int.right())) != ShipType.CARRIER_BACK_MID &&
					getShip(pos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_BACK_MID) {
			if(getShip(pos.add(Vector2Int.left().times(3))) != ShipType.CARRIER_FRONT &&
					getShip(pos.add(Vector2Int.left().times(2))) != ShipType.CARRIER_FRONT_MID &&
					getShip(pos.add(Vector2Int.left())) != ShipType.CARRIER_MID &&
					getShip(pos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_BACK) {
			if(getShip(pos.add(Vector2Int.left().times(4))) != ShipType.CARRIER_FRONT &&
					getShip(pos.add(Vector2Int.left().times(3))) != ShipType.CARRIER_FRONT_MID &&
					getShip(pos.add(Vector2Int.left().times(2))) != ShipType.CARRIER_MID &&
					getShip(pos.add(Vector2Int.left())) != ShipType.CARRIER_BACK_MID) return true;
		}
		return false;
	}
}
