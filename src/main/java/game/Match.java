package game;

import game.core.*;

import java.util.ArrayList;

public abstract class Match implements IPlayerListener
{
    public Board leftBoard;
    protected Board rightBoard;
    
    public Player leftPlayer;
    public Player rightPlayer;
    
    protected int leftShipCount;
    protected int rightShipCount;

    protected int leftScore;
    protected int rightScore;

    protected Player curPlayer;

    protected MatchTimer matchTimer;
    protected RoundTimer roundTimer;
    
    protected ArrayList<IMatchListener> listeners = new ArrayList<IMatchListener>();

    public Match()
    {
        matchTimer = new MatchTimer(this);
        roundTimer = new RoundTimer(this);

        leftShipCount = Resources.getShipQueue().size();
        rightShipCount = Resources.getShipQueue().size();
    }

    @Override
    public void onClientBoard(Player player, Board board)
    {
        if (player == leftPlayer)
        {
            leftBoard = board;
        }
        else if (player == rightPlayer)
        {
            rightBoard = board;
        }

        if (leftBoard != null && rightBoard != null)
        {
            curPlayer = leftPlayer;

            matchTimer.start();
            roundTimer.start();
            
            invokeGameSetup(leftPlayer);
        }
    }
   
    @Override
    public void onMove(Player player, Vector2Int cellPos)
    {
        System.out.println("the player is " + player);
        if (player == null) return;
        if (cellPos == null) return;

    	var isLeftPlayer = player == leftPlayer;
    	var board = isLeftPlayer ? rightBoard : leftBoard;

        var isSunk = board.isSinking(cellPos);
        var isHit = board.isHit(cellPos);
        
        board.guessField(cellPos);

        if (isHit)
        {
            if (isLeftPlayer) leftScore += 1;
            else rightScore += 1;

            invokeScoreChanged(leftScore, rightScore);
        }

        if (isSunk)
        {
            if (isLeftPlayer) leftScore += 1;
            else rightScore += 1;
            
            if (isLeftPlayer) rightShipCount -= 1;
            else leftShipCount -= 1;
            
            invokeShipCountChanged(leftShipCount, rightShipCount);
            invokeScoreChanged(leftScore, rightScore);

            System.out.println("Left ship count now at: " + leftShipCount);
            System.out.println("Right ship count now at: " + rightShipCount);
        }

        var nextPlayer = isHit ? player : (isLeftPlayer ? rightPlayer : leftPlayer);

        curPlayer = nextPlayer;

        roundTimer.restart();
        if (leftShipCount == 0) 
        {
            System.out.println("a1");
            invokeUpdate(player, null, cellPos, isHit, isSunk, false);
            invokeGameOver(Result.WIN_RIGHT);
        }
        else if (rightShipCount == 0) 
        {
            System.out.println("a2");
            invokeUpdate(player, null, cellPos, isHit, isSunk, false);
            invokeGameOver(Result.WIN_LEFT);
        }
        else
        {
            System.out.println("a3");
            invokeUpdate(player, nextPlayer, cellPos, isHit, isSunk, false);
        }
    }
    
    /**
     * Invoked when the round timer has stopped
     */
    public void onRoundTimerStopped()
    {
        var isLeftPlayer = curPlayer == leftPlayer;
        
        invokeUpdate(curPlayer, isLeftPlayer ? rightPlayer : leftPlayer, null, false, false, true);
    }
    
    /**
     * Invoked when the match timer has stopped
     */
    public void onMatchTimerStopped()
    {   
        var result = 
        leftScore > rightScore ? Result.WIN_LEFT : 
        rightScore > leftScore ? Result.WIN_RIGHT : 
        Result.TIE;

        invokeGameOver(result);
    }

    /** 
     * Invokes the Update event
     * @param lastPlayer The player that guessed before
     * @param nextPlayer The player that will be guessing next
     * @param position The position that was guessed at
     * @param isHit Whether the guess hit a ship
     * @param isSunk Whether the guess sunk a ship
     * @param isLate Whether the move was made due to the round timer running out
     */
    protected void invokeUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk, boolean isLate)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onUpdate(lastPlayer, nextPlayer, position, isHit, isSunk, isLate);
        }
    }

    /** 
     * Invokes the GameOver event
     * @param result The Result of the game
     */
    protected void invokeGameOver(Result result)
    {
        System.out.println("The game has ended in a "+ result.toString() + "!");

        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onGameOver(result);
        }

        dispose();
    }

    /**
     * Invokes the PlayerAdded event
     * @param player The added player
     * @param isLeftPlayer Whether the player is the left player
     */
    protected void invokePlayerAdded(Player player, boolean isLeftPlayer)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onPlayerAdded(player, isLeftPlayer);
        }
    }

    /** 
     * Invokes the GameSetup event
     * @param player The player whose game gets set up
     */
    protected void invokeGameSetup(Player player)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onGameSetup(player);
        }
    }

    /** 
     * Invokes the ShipCountChanged event
     * @param leftShipCount The number of ships the left player has left
     * @param rightShipCount The number of ships the right player has left
     */
    protected void invokeShipCountChanged(int leftShipCount, int rightShipCount)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onShipCountChanged(leftShipCount, rightShipCount);
        }
    }

    /** 
     * Invokes the ScoreChanged event
     * @param leftScore The score of the left player
     * @param rightScore The score of the right player
     */
    protected void invokeScoreChanged(int leftScore, int rightScore)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onScoreChanged(leftScore, rightScore);
        }
    }

    /**
     * Adds an IMatchListener to the match observer list
     * @param listener The listener to add
     */
    public void addListener(IMatchListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes an IMatchListener from the match observer list
     * @param listener The listener to remove
     */
    public void removeListener(IMatchListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Event subscription cleanup and timer disposing
     */
    public void dispose()
    {
        matchTimer.stop();
        roundTimer.stop();

        leftPlayer.removeListener(this);
        rightPlayer.removeListener(this);
    }
}
