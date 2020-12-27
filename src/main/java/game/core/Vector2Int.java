package game.core;

public class Vector2Int
{
	public int x;
	public int y;
	
	/**
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Vector2Int(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds two vectors together
	 * @param other The vector to add to the current one
	 * @return Returns the sum of the two vectors
	 */
	public Vector2Int add(Vector2Int other)
	{
		return new Vector2Int(this.x + other.x, this.y + other.y);
	}
	
	/**
	 * Multiplies the vector by any integer value
	 * @param value The value to multiply with
	 * @return Returns the calculated vector
	 */
	public Vector2Int times(int value)
	{
		return new Vector2Int(this.x * value, this.y * value);
	}

	/**
	 * @return Shorthand for writing new Vector2Int(0, 0);
	 */
	public static Vector2Int zero()
	{
		return new Vector2Int(0, 0);
	}

	/**
	 * @return Shorthand for writing new Vector2Int(1, 1);
	 */
	public static Vector2Int one()
	{
		return new Vector2Int(1, 1);
	}

	/**
	 * @return Shorthand for writing new Vector2Int(1, 0);
	 */
	public static Vector2Int right()
	{
		return new Vector2Int(1, 0);
	}
	
	/**
	 * @return Shorthand for writing new Vector2Int(-1, 0);
	 */
	public static Vector2Int left()
	{
		return new Vector2Int(-1, 0);
	}

	/**
	 * @return Shorthand for writing new Vector2Int(0, 1);
	 */
	public static Vector2Int up()
	{
		return new Vector2Int(0, 1);
	}
	
	/**
	 * @return Shorthand for writing new Vector2Int(0, -1);
	 */
	public static Vector2Int down()
	{
		return new Vector2Int(0, -1);
	}
}
