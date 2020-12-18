package game;

public interface IBoardListener
{
	public void onFieldChanged(Vector2 pos, FieldState state);
}
