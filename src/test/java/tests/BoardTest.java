package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import game.*;
import game.core.Vector2Int;

public class BoardTest
{
    Board board;

    @BeforeEach
    void setUp()
    {
        board = new Board(GameConstants.boardSize, GameConstants.leftOffset, GameConstants.tileSize);
    }

    @Test
    public void shouldEqualSizeConstant() {
        var size = board.getSize();
        assertEquals(GameConstants.boardSize, size);
    }

    @Test
    public void shouldNotBeGuessed() {
        var position = new Vector2Int(0, 0);
        board.setShip(position, ShipType.PATROL);
        assertEquals(false, board.isGuessed(position));
    }

    @Test
    public void shouldBeFilledWithWater() {
        board.setShips(ShipType.WATER);
        for(int x = 0; x < GameConstants.boardSize.x; x++)
		{
			for (int y = 0; y < GameConstants.boardSize.y; y++)
			{
				assertEquals(ShipType.WATER, board.getShips()[x][y]);
			}
		}
    }

    @Test
    public void shouldSetAndGetShip() {
        var position = new Vector2Int(0, 0);
        board.setShip(position, ShipType.PATROL);
        assertEquals(ShipType.PATROL, board.getShip(position));
        assertEquals(ShipType.PATROL, board.getShip(0, 0));
    }

    @Test
    public void shouldCompareBoard() {
        for(int x = 0; x < GameConstants.boardSize.x; x++)
		{
			for (int y = 0; y < GameConstants.boardSize.y; y++)
			{
				assertEquals(ShipType.WATER, board.getShips()[x][y]);
			}
		}
    }

    @Test
    public void shouldCheckIfPlaceShipPossibleAndPlace() {
        var truePos = new Vector2Int(2, 9);
        var falsePos = new Vector2Int(-1, 2);
        assertTrue(board.canGuess(truePos));
        assertFalse(board.canGuess(falsePos));

        board.placeShip(truePos, ShipType.PATROL);
        assertEquals(ShipType.PATROL, board.getShip(truePos));
    }

    @Test
    public void shouldAppendShipToTheRight() {
        var position = new Vector2Int(1, 0);
        var rightPos = position.add(Vector2Int.right());
        var twoRightPos = position.add(Vector2Int.right().times(2));
        var leftPos = position.add(Vector2Int.left());

        board.placeShip(position, ShipType.DESTROYER);
        
        assertEquals(ShipType.DESTROYER_FRONT, board.getShip(position));
        assertEquals(ShipType.DESTROYER_MID, board.getShip(rightPos));
        assertEquals(ShipType.DESTROYER_BACK, board.getShip(twoRightPos));
        assertEquals(ShipType.WATER, board.getShip(leftPos));
    }

    @Test
    public void shouldCheckIfCanGuessAndIfInBounds() {
        var pos1 = new Vector2Int(5, 5);
        var pos2 = new Vector2Int(5, 15);
        var pos3 = new Vector2Int(-5, 5);
        assertTrue(board.canGuess(pos1));
        assertFalse(board.canGuess(pos2));
        assertFalse(board.canGuess(pos3));
    }
}
