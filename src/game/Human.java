package game;

import game.core.BoardRenderer;
import game.core.IInputListener;
import game.core.Input;
import game.core.Vector2;

public class Human extends Player implements IInputListener
{
    public Human(String name, Board board)
    {
        super(name, board);

        Input.addListener(this);
    }

    @Override
    public void onKeyDown(int keyCode)
    {

    }

    @Override
    public void onKeyUp(int keyCode)
    {

    }

    @Override
    public void onMouseDown(Vector2 mousePos)
    {
        if (isPlacing)
        {
            if (BoardRenderer.inLeftBounds(mousePos))
            {
                var cellPos = BoardRenderer.worldToCell(mousePos);
                if (cellPos != null)
                {
                    if (board.getField(cellPos) != FieldState.SHIP)
                    {
                        invokeShipPlaced(cellPos);
                    }
                }
            }
        }
        else if (isGuessing)
        {
            if (BoardRenderer.inRightBounds(mousePos))
            {
                var cellPos = BoardRenderer.worldToCell(mousePos);
                if (cellPos != null) invokeGuess(cellPos);
            }
        }
    }

    @Override
    public void onMouseUp(Vector2 mousePos)
    {

    }
}
