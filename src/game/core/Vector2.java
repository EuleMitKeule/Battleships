package game.core;

public class Vector2
{
	public double x;
	public double y;

	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2 times(double value)
	{
		return new Vector2(this.x * value, this.y * value);
	}

	public static Vector2 zero()
	{
		return new Vector2(0, 0);
	}

	public static Vector2 one()
	{
		return new Vector2(1, 1);
	}
}
