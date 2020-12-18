package game;

import game.core.*;

public class Match implements IUpdatable
{
    private Vector2Int boardSize;

    private Board leftBoard;
    private Board rightBoard;

    public Match(Vector2Int boardSize)
    {
        this.boardSize = boardSize;

        leftBoard = new Board(boardSize, true);
        rightBoard = new Board(boardSize, false);

        leftBoard.setField(5, 4, FieldState.SHIP);
        leftBoard.setField(4, 4, FieldState.SHIP);
        leftBoard.setField(3, 4, FieldState.SHIP);
        leftBoard.setField(2, 7, FieldState.SHIP);

        Game.addUpdatable(this);
    }

    @Override
    public void update(long elapsedMillis)
    {

    }
}
