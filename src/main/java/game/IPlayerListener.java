package game;

import game.core.Vector2Int;

public interface IPlayerListener
{
    default void onShipPlaced(Player player, Vector2Int position, ShipType shipType) { }

    default void onFieldGuessed(Player player, Vector2Int position) { }
}
