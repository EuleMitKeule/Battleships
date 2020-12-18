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

	public static Vector2Int zero()
	{
		return new Vector2Int(0, 0);
	}

	public static Vector2 one()
	{
		return new Vector2(1, 1);
	}
}
