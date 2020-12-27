package game;

import game.core.Vector2Int;

public interface IBoardListener
{
	default void onFieldChanged(Board board, Vector2Int pos, FieldState state) { }
	
	default void onShipDestroyed(Board board) { }
}
