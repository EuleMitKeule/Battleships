package game;

import game.core.Vector2Int;

public interface IBoardListener
{
	public void onFieldChanged(Board board, Vector2Int pos, FieldState state);
}
