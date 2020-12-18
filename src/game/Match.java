package game;

import game.core.*;

import java.util.ArrayList;

public class Match implements IUpdatable, IPlayerListener, IBoardListener
{
    private Vector2Int boardSize;

    private Board leftBoard;
    private Board rightBoard;

    private Player leftPlayer;
    private Player rightPlayer;

    private static ArrayList<IMatchListener> listeners = new ArrayList<IMatchListener>();

    public Match(Vector2Int boardSize)
    {
        this.boardSize = boardSize;

        leftBoard = new Board(boardSize, true);
        rightBoard = new Board(boardSize, false);

        leftPlayer = new Human("eule", leftBoard);
        leftPlayer.addListener(this);

        invokePlayerAdded(leftPlayer, true);

        rightPlayer = new Computer("com", rightBoard);
        rightPlayer.addListener(this);

        invokePlayerAdded(rightPlayer, false);

        invokePlacingPlayerChanged(leftPlayer, ShipType.SUPER_PATROL);

        Game.addUpdatable(this);
    }

    @Override
    public void update(long elapsedMillis)
    {

    }

    @Override
    public void onShipPlaced(Player player, Vector2Int position, ShipType shipType)
    {
        System.out.println("Player " + player.name + " placed a ship of type " + shipType);

        var board = player == leftPlayer ? leftBoard : rightBoard;

        board.placeShip(position, shipType);

        invokePlacingPlayerChanged(player == leftPlayer ? rightPlayer : leftPlayer, ShipType.SUPER_PATROL);
    }

    @Override
    public void onGuess(Player player, Vector2Int position)
    {

    }

    private void invokeGuessingPlayerChanged(Player player)
    {
        for (var listener : listeners)
        {
            listener.onGuessingPlayerChanged(player);
        }
    }

    private void invokePlacingPlayerChanged(Player player, ShipType shipType)
    {
        for (var listener : listeners)
        {
            listener.onPlacingPlayerChanged(player, shipType);
        }
    }

    private void invokePlayerAdded(Player player, boolean isLeftPlayer)
    {
        for (var listener : listeners)
        {
            listener.onPlayerAdded(player, isLeftPlayer);
        }
    }

    public static void addListener(IMatchListener listener)
    {
        listeners.add(listener);
    }

    public static void removeListener(IMatchListener listener)
    {
        listeners.remove(listener);
    }

    @Override
    public void onFieldChanged(Board board, Vector2Int pos, FieldState state)
    {

    }
}
