package game;

import game.core.*;

import java.util.ArrayList;

public class Board implements IRenderable
{
	private ShipType[][] ships;
	private FieldState[][] fields;

	private final Vector2Int size;
	private final Vector2Int offset;
	private final int tileSize;
	private boolean owned;
	private boolean isRendered;

	/**
	 * @param size The cell dimensions of the board
	 * @param offset The pixel offset of the board
	 * @param tileSize The pixel size of one tile
	 * @param owned Whether the board is assigned to the player running the application
	 */
	public Board(Vector2Int size, Vector2Int offset, int tileSize, boolean owned, boolean isRendered)
	{
		Game.addRenderable(this);

		this.size = size;
		this.offset = offset;
		this.tileSize = tileSize;
		this.owned = owned;
		this.isRendered = isRendered;

		fields = new FieldState[size.x][size.y];
		ships = new ShipType[size.x][size.y];

		setFields(FieldState.WATER);
	}
	
	/**
	 * Sets a field on the board to a new field state
	 * @param cellPos The cell position to set
	 * @param state The field state to assign
	 */
	public void setField(Vector2Int cellPos, FieldState state)
	{
		fields[cellPos.x][cellPos.y] = state;
	}

	/**
	 * Sets a field on the board to a new field state
	 * @param x The x cell coordinate
	 * @param y The y cell coordinate
	 * @param state The field state to assign
	 */
	public void setField(int x, int y, FieldState state)
	{
		fields[x][y] = state;
	}

	/**
	 * Sets all fields on the board to a new field state
	 * @param state The field state to assign
	 */
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
	
	/**
	 * Sets a field on the board to a new ship state
	 * @param cellPos The cell position to set
	 * @param shipType The ship type to assign
	 */
	public void setShip(Vector2Int cellPos, ShipType shipType)
	{
		ships[cellPos.x][cellPos.y] = shipType;
	}

	/**
	 * Sets a field on the board to a new ship state
	 * @param x The x cell coordinate
	 * @param y The y cell coordinate
	 * @param shipType The ship type to assign
	 */
	public void setShip(int x, int y, ShipType shipType)
	{
		ships[x][y] = shipType;
	}

	/**
	 * Sets all fields on the board to a new ship state
	 * @param shipType The ship type to assign
	 */
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

	/**
	 * @param cellPos The cell position to get
	 * @return Returns the ship type of a field on the board or null if out of bounds
	 */
	public ShipType getShip(Vector2Int cellPos)
	{
		try
		{
			return ships[cellPos.x][cellPos.y];
		}
		catch (IndexOutOfBoundsException ex)
		{
			return null;
		}
	}

	/**
	 * @param x The x cell coordinate to get
	 * @param y The y cell coordinate to get
	 * @return Returns the ship type of a field on the board or null if out of bounds
	 */
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

	/**
	 * @return Returns the ship type 2D-array
	 */
	public ShipType[][] getShips()
	{
		return ships;
	}
	
	/**
	 * @param cellPos The cell position to get
	 * @return Returns the field state of a field on the board or null if out of bounds
	 */
	public FieldState getField(Vector2Int cellPos)
	{
		try
		{
			return fields[cellPos.x][cellPos.y];
		}
		catch (IndexOutOfBoundsException ex)
		{
			return null;
		}
	}
	
	/**
	 * @param x The x cell coordinate to get
	 * @param y The y cell coordinate to get
	 * @return Returns the field state of a field on the board or null if out of bounds
	 */
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

	/**
	 * @return Returns the field state 2D-array
	 */
	public FieldState[][] getFields()
	{
		return fields;
	}

	/**
	 * @return Returns the cell dimensions of the board
	 */
	public Vector2Int getSize()
	{
		return size;
	}

	/**
	 * @param cellPos The cell position to check
	 * @param shipType The ship type to check
	 * @return Returns whether a ship type can be placed at a field on the board
	 */
	public boolean canPlace(Vector2Int cellPos, ShipType shipType)
	{
		switch (shipType)
		{
			case PATROL:
				return getField(cellPos) == FieldState.WATER;
			case SUPER_PATROL:
				return getField(cellPos) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right())) == FieldState.WATER;
			case DESTROYER:
				return getField(cellPos) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right())) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right().times(2))) == FieldState.WATER;
			case BATTLESHIP:
				return getField(cellPos) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right())) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right().times(2))) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right().times(3))) == FieldState.WATER;
			case CARRIER:
				return getField(cellPos) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right())) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right().times(2))) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right().times(3))) == FieldState.WATER &&
						getField(cellPos.add(Vector2Int.right().times(4))) == FieldState.WATER;
			default:
				return false;
		}
	}

	/**
	 * Places a ship type on the board
	 * @param cellPos The cell position to place the ship at
	 * @param shipType The ship type to place
	 */
	public void placeShip(Vector2Int cellPos, ShipType shipType)
	{
		if (canPlace(cellPos, shipType))
		{
			switch (shipType)
			{
				case PATROL:
					setField(cellPos, FieldState.SHIP);
					setShip(cellPos, ShipType.PATROL);
					break;
				case SUPER_PATROL:
					setField(cellPos, FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right()), FieldState.SHIP);
					setShip(cellPos, ShipType.SUPER_PATROL_FRONT);
					setShip(cellPos.add(Vector2Int.right()), ShipType.SUPER_PATROL_BACK);
					break;
				case DESTROYER:
					setField(cellPos, FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right()), FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right().times(2)), FieldState.SHIP);
					setShip(cellPos, ShipType.DESTROYER_FRONT);
					setShip(cellPos.add(Vector2Int.right()), ShipType.DESTROYER_MID);
					setShip(cellPos.add(Vector2Int.right().times(2)), ShipType.DESTROYER_BACK);
					break;
				case BATTLESHIP:
					setField(cellPos, FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right()), FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right().times(2)), FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right().times(3)), FieldState.SHIP);
					setShip(cellPos, ShipType.BATTLESHIP_FRONT);
					setShip(cellPos.add(Vector2Int.right()), ShipType.BATTLESHIP_FRONT_MID);
					setShip(cellPos.add(Vector2Int.right().times(2)), ShipType.BATTLESHIP_BACK_MID);
					setShip(cellPos.add(Vector2Int.right().times(3)), ShipType.BATTLESHIP_BACK);
					break;
				case CARRIER:
					setField(cellPos, FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right()), FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right().times(2)), FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right().times(3)), FieldState.SHIP);
					setField(cellPos.add(Vector2Int.right().times(4)), FieldState.SHIP);
					setShip(cellPos, ShipType.CARRIER_FRONT);
					setShip(cellPos.add(Vector2Int.right()), ShipType.CARRIER_FRONT_MID);
					setShip(cellPos.add(Vector2Int.right().times(2)), ShipType.CARRIER_MID);
					setShip(cellPos.add(Vector2Int.right().times(3)), ShipType.CARRIER_BACK_MID);
					setShip(cellPos.add(Vector2Int.right().times(4)), ShipType.CARRIER_BACK);
					break;
			default:
				break;
			}
		}
	}
	
	/**
	 * @param cellPos The cell position to check
	 * @return Returns whether a field is guessable
	 */
	public boolean canGuess(Vector2Int cellPos)
	{
		return inBounds(cellPos) && (getField(cellPos) == FieldState.WATER || getField(cellPos) == FieldState.SHIP);
	}

	/**
	 * Guesses a field on the board
	 * @param cellPos The cell position to guess
	 * @return Returns whether a ship was hit
	 */
	public void guessField(Vector2Int cellPos) 
	{
		switch(getField(cellPos)) 
    	{
			case WATER:

				setField(cellPos, FieldState.WATER_GUESSED);
				
				break;
			case SHIP:

				setField(cellPos, FieldState.SHIP_GUESSED);
	    		setShip(cellPos, ShipType.SHIP_DESTROYED);
				
				break;
	    	default:
	    		break;
    	}
		
	}

	public boolean isHit(Vector2Int cellPos)
	{
		switch(getField(cellPos)) 
    	{
			case WATER:

				setField(cellPos, FieldState.WATER_GUESSED);
				
				return false;
				
			case SHIP:

				setField(cellPos, FieldState.SHIP_GUESSED);

	    		setShip(cellPos, ShipType.SHIP_DESTROYED);
	    		
				return true;
				
	    	default:
	    		return false;
    	}
		
	}
	
	/**
	 * @param cellPos The cell position to check
	 * @return Returns whether a field is the last remaining part of a ship
	 */
	public boolean isSinking(Vector2Int cellPos)
	{
		var startField = getShip(cellPos);
		
		if(startField == ShipType.PATROL) return true;
		
		if(startField == ShipType.SUPER_PATROL_FRONT) {
			if(getShip(cellPos.add(Vector2Int.right())) != ShipType.SUPER_PATROL_BACK) return true;
		}
		
		if(startField == ShipType.SUPER_PATROL_BACK) {
			if(getShip(cellPos.add(Vector2Int.left())) != ShipType.SUPER_PATROL_FRONT) return true;
		}
		
		if(startField == ShipType.DESTROYER_FRONT) {
			if(getShip(cellPos.add(Vector2Int.right())) != ShipType.DESTROYER_MID &&
					getShip(cellPos.add(Vector2Int.right().times(2))) != ShipType.DESTROYER_BACK) return true;
		}
		
		if(startField == ShipType.DESTROYER_MID) {
			if(getShip(cellPos.add(Vector2Int.right())) != ShipType.DESTROYER_BACK &&
					getShip(cellPos.add(Vector2Int.left())) != ShipType.DESTROYER_FRONT) return true;
		}
		
		if(startField == ShipType.DESTROYER_BACK) {
			if(getShip(cellPos.add(Vector2Int.left())) != ShipType.DESTROYER_MID &&
					getShip(cellPos.add(Vector2Int.left().times(2))) != ShipType.DESTROYER_FRONT) return true;
		}
		
		if(startField == ShipType.BATTLESHIP_FRONT) {
			if(getShip(cellPos.add(Vector2Int.right())) != ShipType.BATTLESHIP_FRONT_MID &&
					getShip(cellPos.add(Vector2Int.right().times(2))) != ShipType.BATTLESHIP_BACK_MID &&
					getShip(cellPos.add(Vector2Int.right().times(3))) != ShipType.BATTLESHIP_BACK) return true;
		}
		
		if(startField == ShipType.BATTLESHIP_FRONT_MID) {
			if(getShip(cellPos.add(Vector2Int.left())) != ShipType.BATTLESHIP_FRONT &&
					getShip(cellPos.add(Vector2Int.right())) != ShipType.BATTLESHIP_BACK_MID &&
					getShip(cellPos.add(Vector2Int.left().times(2))) != ShipType.BATTLESHIP_BACK) return true;
		}
		
		if(startField == ShipType.BATTLESHIP_BACK_MID) {
			if(getShip(cellPos.add(Vector2Int.left())) != ShipType.BATTLESHIP_FRONT_MID &&
					getShip(cellPos.add(Vector2Int.left().times(2))) != ShipType.BATTLESHIP_FRONT &&
					getShip(cellPos.add(Vector2Int.right())) != ShipType.BATTLESHIP_BACK) return true;
		}
		
		if(startField == ShipType.BATTLESHIP_BACK) {
			if(getShip(cellPos.add(Vector2Int.left())) != ShipType.BATTLESHIP_BACK_MID &&
					getShip(cellPos.add(Vector2Int.left().times(2))) != ShipType.BATTLESHIP_FRONT_MID &&
					getShip(cellPos.add(Vector2Int.left().times(3))) != ShipType.BATTLESHIP_FRONT) return true;
		}
		
		if(startField == ShipType.CARRIER_FRONT) {
			if(getShip(cellPos.add(Vector2Int.right())) != ShipType.CARRIER_FRONT_MID &&
					getShip(cellPos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_MID &&
					getShip(cellPos.add(Vector2Int.right().times(3))) != ShipType.CARRIER_BACK_MID &&
					getShip(cellPos.add(Vector2Int.right().times(4))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_FRONT_MID) {
			if(getShip(cellPos.add(Vector2Int.left())) != ShipType.CARRIER_FRONT &&
					getShip(cellPos.add(Vector2Int.right())) != ShipType.CARRIER_MID &&
					getShip(cellPos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_BACK_MID &&
					getShip(cellPos.add(Vector2Int.right().times(3))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_MID) {
			if(getShip(cellPos.add(Vector2Int.left())) != ShipType.CARRIER_FRONT &&
					getShip(cellPos.add(Vector2Int.left().times(2))) != ShipType.CARRIER_FRONT_MID &&
					getShip(cellPos.add(Vector2Int.right())) != ShipType.CARRIER_BACK_MID &&
					getShip(cellPos.add(Vector2Int.right().times(2))) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_BACK_MID) {
			if(getShip(cellPos.add(Vector2Int.left().times(3))) != ShipType.CARRIER_FRONT &&
					getShip(cellPos.add(Vector2Int.left().times(2))) != ShipType.CARRIER_FRONT_MID &&
					getShip(cellPos.add(Vector2Int.left())) != ShipType.CARRIER_MID &&
					getShip(cellPos.add(Vector2Int.right())) != ShipType.CARRIER_BACK) return true;
		}
		
		if(startField == ShipType.CARRIER_BACK) {
			if(getShip(cellPos.add(Vector2Int.left().times(4))) != ShipType.CARRIER_FRONT &&
					getShip(cellPos.add(Vector2Int.left().times(3))) != ShipType.CARRIER_FRONT_MID &&
					getShip(cellPos.add(Vector2Int.left().times(2))) != ShipType.CARRIER_MID &&
					getShip(cellPos.add(Vector2Int.left())) != ShipType.CARRIER_BACK_MID) return true;
		}
		return false;
	}

	/**
	 * Gets invoked every render frame
	 * @param renderer The renderer context
	 */
	@Override
	public void render(BoardRenderer renderer)
	{
		if (!isRendered) return;

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
					case SHIP ->
					{
						//if (owned)
						//{
						switch (getShip(position))
						{
							case PATROL -> sprite = Resources.PATROL;
							case SUPER_PATROL_FRONT -> sprite = Resources.SUPER_PATROL_FRONT;
							case SUPER_PATROL_BACK -> sprite = Resources.SUPER_PATROL_BACK;
							case DESTROYER_FRONT -> sprite = Resources.DESTROYER_FRONT;
							case DESTROYER_MID -> sprite = Resources.DESTROYER_MID;
							case DESTROYER_BACK -> sprite = Resources.DESTROYER_BACK;
							case BATTLESHIP_FRONT -> sprite = Resources.BATTLESHIP_FRONT;
							case BATTLESHIP_FRONT_MID -> sprite = Resources.BATTLESHIP_FRONT_MID;
							case BATTLESHIP_BACK_MID -> sprite = Resources.BATTLESHIP_BACK_MID;
							case BATTLESHIP_BACK -> sprite = Resources.BATTLESHIP_BACK;
							case CARRIER_FRONT -> sprite = Resources.CARRIER_FRONT;
							case CARRIER_FRONT_MID -> sprite = Resources.CARRIER_FRONT_MID;
							case CARRIER_MID -> sprite = Resources.CARRIER_MID;
							case CARRIER_BACK_MID -> sprite = Resources.CARRIER_BACK_MID;
							case CARRIER_BACK -> sprite = Resources.CARRIER_BACK;
							default -> sprite = Resources.SPRITE_NULL;
						}
						//}
					}
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

	/**
	 * @param worldPos The world position to check
	 * @return Returns whether a world position is inside the bounds of the board
	 */
    public boolean inBounds(Vector2 worldPos)
    {
        return (worldPos.x >  offset.x && worldPos.x < offset.x + size.x * tileSize) &&
                (worldPos.y > offset.y && worldPos.y < offset.y + size.y * tileSize);
	}
	
	/**
	 * @param worldPos The world position to check
	 * @return Returns whether a world position is inside the bounds of the board
	 */
    public boolean inBounds(Vector2Int cellPos)
    {
        return (cellPos.x >=  0 && cellPos.x < GameConstants.boardSize.x) &&
                (cellPos.y >= 0 && cellPos.y < GameConstants.boardSize.y);
    }

	/**
	 * @param worldPos The world position to convert
	 * @return Returns the converted world position in cell space
	 */
    public Vector2Int worldToCell(Vector2 worldPos)
    {
        if (inBounds(worldPos))
        {
            return new Vector2Int((int)Math.floor((worldPos.x - offset.x) / tileSize),
                                    (int)Math.floor((worldPos.y - offset.y) / tileSize));
        }
        else return null;
	}
}
