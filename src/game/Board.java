package game;
import java.util.ArrayList;

public class Board 
{
	FieldState [][] fields;
	final Vector2 size;
	ArrayList<IBoardListener> listeners = new ArrayList<IBoardListener>(); 
	 
	
	public Board(int sizeX, int sizeY)
	{
		size = new Vector2(sizeX, sizeY);
		fields = new FieldState[sizeX][sizeY];
		for(int x = 0; x < size.x; x++) 
		{
			for(int y = 0; y < size.y; y++) 
			{
				fields[x][y] = FieldState.WATER;
			}
		}
		
	}
	
	public void setField(Vector2 pos, FieldState state)
	{
		fields[pos.x][pos.y] = state;
		InvokeFieldChanged(pos, state);
	}
	
	public void setField(int x, int y, FieldState state)
	{
		fields[x][y] = state;
		InvokeFieldChanged(new Vector2(x, y), state);
	}
	
	public FieldState getField(Vector2 pos)
	{
		return fields[pos.x][pos.y];
	}
	
	public FieldState getField(int x, int y)
	{
		return fields[x][y];
	}

	public FieldState[][] getFields()
	{
		return fields;
	}
	
	public void resetBoard() 
	{
		fields = new FieldState[size.x][size.y];
	}
	
	public void InvokeFieldChanged(Vector2 pos, FieldState state)
	{
		for(var listener:listeners)
		{
			listener.onFieldChanged(pos, state);
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
	
	public static void main(String args[])  
    {  
       var board = new Board(10,10);
       System.out.println(board.getField(1, 1));
    } 
}
