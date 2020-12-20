package game.core;

public class Vector2Int
{
	public int x;
	public int y;
	
	public Vector2Int(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2Int add(Vector2Int other)
	{
		return new Vector2Int(this.x + other.x, this.y + other.y);
	}
	
	public Vector2Int times(int value)
	{
		return new Vector2Int(this.x * value, this.y * value);
	}

	public static Vector2Int zero()
	{
		return new Vector2Int(0, 0);
	}

	public static Vector2Int one()
	{
		return new Vector2Int(1, 1);
	}

	public static Vector2Int right()
	{
		return new Vector2Int(1, 0);
	}

	public static Vector2Int up()
	{
		return new Vector2Int(0, 1);
	}
}
