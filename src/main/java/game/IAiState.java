package game;

import game.core.Vector2Int;

public interface IAiState
{
    void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk);

    void enterState();

    void exitState();
}
