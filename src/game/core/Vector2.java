package game.core;

public class Vector2
{
	public double x;
	public double y;

	/**
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Multiplies the vector by any double value
	 * @param value The value to multiply with
	 * @return Returns the multiplied vector
	 */
	public Vector2 times(double value)
	{
		return new Vector2(this.x * value, this.y * value);
	}

	/**
	 * @return Shorthand for writing new Vector2(0, 0);
	 */
	public static Vector2 zero()
	{
		return new Vector2(0, 0);
	}

	/**
	 * @return Shorthand for writing new Vector2(1, 1);
	 */
	public static Vector2 one()
	{
		return new Vector2(1, 1);
	}
}
