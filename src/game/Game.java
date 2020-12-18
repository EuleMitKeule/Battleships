package game;

public class Game implements IBoardListener
{

	public Game()
	{
		var board = new Board(10,10);
		board.addListener(this);
		board.setField(1, 1, FieldState.SHIP);
	}
	@Override
	public void onFieldChanged(Vector2 pos, FieldState state)
	{
		System.out.println(pos.x + "," + pos.y + "," + state);
	}
	
	public static void main(String args[])  
    {
		
    } 
}
