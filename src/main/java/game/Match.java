package game;

import game.core.*;

import java.util.ArrayList;

public abstract class Match implements IPlayerListener
{
    protected Vector2Int boardSize;

    protected Board leftBoard;
    protected Board rightBoard;
    
    protected Player leftPlayer;
    protected Player rightPlayer;
    
    protected int leftShipCount;
    protected int rightShipCount;

    protected int leftScore;
    protected int rightScore;

    protected Player curPlayer;

    protected MatchTimer matchTimer;
    protected RoundTimer roundTimer;
    
    protected ArrayList<IMatchListener> listeners = new ArrayList<IMatchListener>();

    /**
     * 
     * @param game The game context
     * @param leftOffset The pixel offset of the left board
     * @param rightOffset The pixel offset of the right board
     * @param tileSize The pixel size of tiles
     * @param boardSize The cell dimensions of the board
     */
    public Match(Game game, Vector2Int leftOffset, Vector2Int rightOffset, int tileSize, Vector2Int boardSize)
    {
        this.boardSize = boardSize;

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
    	var isLeftPlayer = player == leftPlayer;
    	var board = isLeftPlayer ? rightBoard : leftBoard;

        var isSunk = board.isSinking(cellPos);
        var isHit = board.isHit(cellPos);
        
        board.guessField(cellPos);

        if (isHit)
        {
            if (isLeftPlayer) leftScore += 1;
            else rightScore += 1;
        }

        if (isSunk)
        {
            if (isLeftPlayer) leftScore += 1;
            else rightScore += 1;
            
            if (isLeftPlayer) leftShipCount -= 1;
            else rightShipCount -= 1;
            
            System.out.println("Left ship count now at: " + leftShipCount);
            System.out.println("Right ship count now at: " + rightShipCount);
        }

        var nextPlayer = isHit ? player : (isLeftPlayer ? rightPlayer : leftPlayer);

        curPlayer = nextPlayer;

        roundTimer.restart();
        
        if (leftShipCount == 0) 
        {
            invokeUpdate(player, null, cellPos, isHit, isSunk);
            invokeGameOver(Result.WIN_RIGHT);
        }
        else if (rightShipCount == 0) 
        {
            invokeUpdate(player, null, cellPos, isHit, isSunk);
            invokeGameOver(Result.WIN_LEFT);
        }
        else
        {
            invokeUpdate(player, nextPlayer, cellPos, isHit, isSunk);
        }
    }
    
    public void onRoundTimerStopped()
    {
        var isLeftPlayer = curPlayer == leftPlayer;
        
        invokeLateMove();
        invokeUpdate(curPlayer, isLeftPlayer ? rightPlayer : leftPlayer, null, false, false);
    }
    
    public void onMatchTimerStopped()
    {   
        var result = 
        leftScore > rightScore ? Result.WIN_LEFT : 
        rightScore > leftScore ? Result.WIN_RIGHT : 
        Result.TIE;

        invokeGameOver(result);
    }

    /**
     * Invokes the GuessingPlayerChanged event
     * @param player The player that guesses next
     */
    protected void invokeUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onUpdate(lastPlayer, nextPlayer, position, isHit, isSunk);
        }
    }

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

    protected void invokeGameSetup(Player player)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onGameSetup(player);
        }
    }

    protected void invokeLateMove()
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) return;
            listener.onLateMove();
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

    public void dispose()
    {
        matchTimer.stop();
        roundTimer.stop();

        leftPlayer.removeListener(this);
        rightPlayer.removeListener(this);
    }
}
