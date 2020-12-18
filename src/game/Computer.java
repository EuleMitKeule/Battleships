package game;

import game.core.Vector2Int;

import java.util.Random;

public class Computer extends Player
{
    public Computer(String name, Board board)
    {
        super(name, board);
    }

    @Override
    public void onPlacingPlayerChanged(Player player, ShipType shipType)
    {
        super.onPlacingPlayerChanged(player, shipType);

        if (player == this)
        {
            var rand = new Random();
            var cellPos = new Vector2Int(rand.nextInt(board.getSize().x), rand.nextInt(board.getSize().y));

            while (!board.canPlace(cellPos, curShipType))
            {
                cellPos = new Vector2Int(rand.nextInt(board.getSize().x), rand.nextInt(board.getSize().y));
            }

            invokeShipPlaced(cellPos, shipType);
        }
    }

    @Override
    public void onGuessingPlayerChanged(Player player)
    {

    }
}
