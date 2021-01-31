package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.engine.discovery.predicates.IsTestClassWithTests;

import game.*;
import game.core.Vector2Int;

public class MatchTest implements IMatchListener
{
    Player rightPlayer;
    Player leftPlayer;

    Player startingPlayer;

    Match match;

    int leftShipCount;
    int rightShipCount;

    int leftScore;
    int rightScore;

    Result result;

    Player lastPlayer, nextPlayer;

    Vector2Int position;

    boolean isHit, isSunk, isLate;
    
    @BeforeEach
    void setUp()
    {
        match = new LocalMatch(this, true);
    }

    @Test
    public void shouldAssignBoardToCorrectSide() 
    {
        var testBoard = A.Board().build();
        match.onClientBoard(leftPlayer, testBoard);
        
        System.out.println(match.leftBoard);
        assertEquals(testBoard, match.leftBoard);
    }

    @Test
    public void shouldBeginMatchWithLeftPlayer()
    {
        var testBoard = A.Board().build();
        match.onClientBoard(leftPlayer, testBoard);
        match.onClientBoard(rightPlayer, testBoard);

        assertEquals(startingPlayer, leftPlayer);
    }

    @Test
    public void shouldAddScoreAndSubtractShipCount()
    {
        var leftBoard = A.Board().build();
        var rightBoard = A.Board().onSide(Side.RIGHT).withDefaultShip().build();
        
        match.onClientBoard(leftPlayer, leftBoard);
        match.onClientBoard(rightPlayer, rightBoard);
    
        match.onMove(leftPlayer, Vector2Int.zero());

        assertEquals(27, rightShipCount);
        assertEquals(2, leftScore);

        assertEquals(28, leftShipCount);
        assertEquals(0, rightScore);

    }

    @Test
    public void shouldEndGameWhenShipsAreDestroyed()
    {
        var leftBoard = A.Board().build();
        var rightBoard = A.Board().onSide(Side.RIGHT).withShips().build();
        
        match.onClientBoard(leftPlayer, leftBoard);
        match.onClientBoard(rightPlayer, rightBoard);

        for (int i = 0; i < 28; i++)
        {
            var x = i % GameConstants.boardSize.x;
            var y = Math.round(i / GameConstants.boardSize.x);

            match.onMove(leftPlayer, new Vector2Int(x, y));
        }

        assertEquals(Result.WIN_LEFT, result);
    }

    @Test
    public void shouldNotChangeTurnsOnHit()
    {
        var leftBoard = A.Board().withDefaultShip().build();
        var rightBoard = A.Board().onSide(Side.RIGHT).withDefaultShip().build();

        match.onClientBoard(leftPlayer, leftBoard);
        match.onClientBoard(rightPlayer, rightBoard);

        match.onMove(leftPlayer, Vector2Int.zero());

        assertEquals(leftPlayer, lastPlayer);
        assertEquals(leftPlayer, nextPlayer);
        assertEquals(0, position.x);
        assertEquals(0, position.y);
        assertTrue(isHit);
        assertTrue(isSunk);
        assertFalse(isLate);
    }

    @Test
    public void shouldChangeTurnsOnMiss()
    {
        var leftBoard = A.Board().withDefaultShip().build();
        var rightBoard = A.Board().onSide(Side.RIGHT).withDefaultShip().build();

        match.onClientBoard(leftPlayer, leftBoard);
        match.onClientBoard(rightPlayer, rightBoard);

        match.onMove(leftPlayer, Vector2Int.right());

        assertEquals(leftPlayer, lastPlayer);
        assertEquals(rightPlayer, nextPlayer);
        assertEquals(1, position.x);
        assertEquals(0, position.y);
        assertFalse(isHit);
        assertFalse(isSunk);
        assertFalse(isLate);
    }

    @Test
    public void shouldEndTurnWhenRoundTimerRunsOut()
    {
        var leftBoard = A.Board().withDefaultShip().build();
        var rightBoard = A.Board().onSide(Side.RIGHT).withDefaultShip().build();

        match.onClientBoard(leftPlayer, leftBoard);
        match.onClientBoard(rightPlayer, rightBoard);

        match.onRoundTimerStopped();

        assertEquals(leftPlayer, lastPlayer);
        assertEquals(rightPlayer, nextPlayer);
        assertNull(position);
        assertFalse(isHit);
        assertFalse(isSunk);
        assertTrue(isLate);
    }
    
    @Test
    public void shoulTieWhenMatchTimerRunsOut()
    {
        var leftBoard = A.Board().withDefaultShip().build();
        var rightBoard = A.Board().onSide(Side.RIGHT).withDefaultShip().build();

        match.onClientBoard(leftPlayer, leftBoard);
        match.onClientBoard(rightPlayer, rightBoard);

        match.onMatchTimerStopped();

        assertEquals(Result.TIE, result);
    }

    @Test
    public void shouldWinByScoreWhenMatchTimerRunsOut()
    {
        var leftBoard = A.Board().withDefaultShip().build();
        var rightBoard = A.Board().onSide(Side.RIGHT).withDefaultShip().build();

        match.onClientBoard(leftPlayer, leftBoard);
        match.onClientBoard(rightPlayer, rightBoard);

        match.onMove(leftPlayer, Vector2Int.right());

        match.onMove(rightPlayer, Vector2Int.zero());

        match.onMatchTimerStopped();

        assertEquals(Result.WIN_RIGHT, result);
    }

    @Override
    public void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk, boolean isLate)
    {
        System.out.println(lastPlayer);
        this.lastPlayer = lastPlayer;
        this.nextPlayer = nextPlayer;
        this.position = position;
        this.isHit = isHit;
        this.isSunk = isSunk;
        this.isLate = isLate;
        
    }

    @Override
    public void onGameOver(Result result)
    {
        this.result = result;
    }

    @Override
    public void onShipCountChanged(int leftShipCount, int rightShipCount)
    {
        this.leftShipCount = leftShipCount;
        this.rightShipCount = rightShipCount;
    }

    @Override
    public void onScoreChanged(int leftScore, int rightScore)
    {
        this.leftScore = leftScore;
        this.rightScore = rightScore;
    }

    @Override
    public void onGameSetup(Player player)
    {
        this.startingPlayer = player;
    }

    @Override
    public void onPlayerAdded(Player player, boolean isLeftPlayer)
    {
        if (isLeftPlayer)
        {
            leftPlayer = player;
        }
        else rightPlayer = player;
        System.out.println("player added");
    }
}
