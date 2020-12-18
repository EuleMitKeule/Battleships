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

        rightPlayer = new Computer("com", rightBoard);
        rightPlayer.addListener(this);

        invokePlacingPlayerChanged(leftPlayer);

        Game.addUpdatable(this);
    }

    @Override
    public void update(long elapsedMillis)
    {

    }

    @Override
    public void onShipPlaced(Player player, Vector2Int position)
    {
        System.out.println("Player " + player.name + " just placed a ship");

        if (player == leftPlayer)
        {
            leftBoard.setField(position, FieldState.SHIP);
            invokePlacingPlayerChanged(rightPlayer);
        }
        else if (player == rightPlayer)
        {
            rightBoard.setField(position, FieldState.SHIP);
            invokePlacingPlayerChanged(leftPlayer);
        }
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

    private void invokePlacingPlayerChanged(Player player)
    {
        for (var listener : listeners)
        {
            listener.onPlacingPlayerChanged(player);
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
