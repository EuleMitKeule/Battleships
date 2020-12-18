package game;

import game.core.Vector2Int;

public interface IPlayerListener
{
    void onShipPlaced(Player player, Vector2Int position);

    void onGuess(Player player, Vector2Int position);
}
