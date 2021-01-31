package tests;

import java.util.Vector;

import game.Board;
import game.GameConstants;
import game.ShipType;
import game.core.Vector2Int;

public class BoardBuilder 
{

    private Board board;

    public BoardBuilder() {
        board = new Board(GameConstants.boardSize, GameConstants.leftOffset, GameConstants.tileSize);
    }
    
    public BoardBuilder onSide(Side side)
    {
        board = new Board(GameConstants.boardSize, side == Side.LEFT ? GameConstants.leftOffset : GameConstants.rightOffset, GameConstants.tileSize);
        return this;
    } 

    public BoardBuilder withShipAt(Vector2Int position)
    {
        board.placeShip(position, ShipType.PATROL);
        return this;
    }

    public BoardBuilder withDefaultShip()
    {
        board.placeShip(Vector2Int.zero(), ShipType.PATROL);
        return this;
    }

    public BoardBuilder withShips()
    {
        for (int x = 0; x < board.getSize().x; x++)
        {
            for (int y = 0; y < board.getSize().y; y++)
            {
                board.placeShip(new Vector2Int(x, y), ShipType.PATROL);
            }
        }
        return this;
    }

    public Board build()
    {
        return board;
    }
}
