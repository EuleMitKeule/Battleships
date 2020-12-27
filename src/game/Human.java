package game;

import game.core.IInputListener;
import game.core.Input;
import game.core.Vector2;

public class Human extends Player implements IInputListener
{
    /**
     * 
     * @param name The name for the player
     * @param match The match context
     */
    public Human(String name, Match match)
    {
        super(name, match);

        Input.addListener(this);
    }

    /**
     * Gets invoked when the left mouse button is pressed down
     * @param mousePos The current mouse position
     */
    @Override
    public void onMouseDown(Vector2 mousePos)
    {
        if (isPlacing)
        {
            if (match.inLeftBounds(mousePos))
            {
                var cellPos = match.worldToCell(mousePos);
                if (cellPos != null)
                {
                    if (match.canPlace(this, cellPos, curShipType))
                    {
                        invokeShipPlaced(cellPos, curShipType);
                    }
                }
            }
        }
        else if (isGuessing)
        {
            if (match.inRightBounds(mousePos))
            {
                var cellPos = match.worldToCell(mousePos);
                if (cellPos != null) invokeFieldGuessed(cellPos);
            }
        }
    }
}
