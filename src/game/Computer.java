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
    public void onPlacingPlayerChanged(Player player)
    {
        super.onPlacingPlayerChanged(player);

        if (player == this)
        {
            var rand = new Random();
            var placePos = new Vector2Int(rand.nextInt(board.getSize().x), rand.nextInt(board.getSize().y));

            while (board.getField(placePos.x, placePos.y) != FieldState.WATER)
            {
                placePos = new Vector2Int(rand.nextInt(board.getSize().x), rand.nextInt(board.getSize().y));
            }

            invokeShipPlaced(placePos);
        }
    }

    @Override
    public void onGuessingPlayerChanged(Player player)
    {

    }
}
