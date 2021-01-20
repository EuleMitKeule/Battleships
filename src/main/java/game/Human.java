package game;

import game.core.IInputListener;
import game.core.Input;

import game.core.*;

public class Human extends Player implements IInputListener {
    private boolean isPlacing = true;

    /**
     * 
     * @param name  The name for the player
     * @param match The match context
     */
    public Human(String name, Match match) 
    {
        super(name, match);

        this.ownBoard = new Board(GameConstants.boardSize, GameConstants.leftOffset, GameConstants.tileSize);

        curShipType = shipQueue.pop();

        new BoardRenderer(ownBoard, GameConstants.leftOffset, GameConstants.tileSize);
        new BoardRenderer(enemyBoard, GameConstants.rightOffset, GameConstants.tileSize);

        Input.addListener(this);
    }

    @Override
    public void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int cellPos, boolean isHit, boolean isSunk) 
    {
        super.onUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk);

        if (cellPos != null)
        {
            if (lastPlayer != this) ownBoard.guessField(cellPos);
            else
            {
                enemyBoard.setShip(cellPos, isHit ? ShipType.PATROL : ShipType.WATER );
                enemyBoard.guessField(cellPos);
            }
        }
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
            if (ownBoard.inBounds(mousePos))
            {
                var cellPos = ownBoard.worldToCell(mousePos);

                if (cellPos != null)
                {
                    if (ownBoard.canPlace(cellPos, curShipType))
                    {
                        ownBoard.placeShip(cellPos, curShipType);

                        if (shipQueue.size() == 0)
                        {
                            isPlacing = false;
                            invokeClientBoard(this, ownBoard);
                        }
                        else
                        {
                            curShipType = shipQueue.pop();
                        }
                    }
                }
            }
        }
        else if (isGuessing)
        {
            if (enemyBoard.inBounds(mousePos))
            {
                var cellPos = enemyBoard.worldToCell(mousePos);
                if (cellPos != null && enemyBoard.canGuess(cellPos)) invokeMove(cellPos);
            }
        }
    }
}
