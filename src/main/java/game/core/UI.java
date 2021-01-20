package game.core;

import game.GameConstants;
import game.LocalMatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import game.*;

public class UI implements IMatchListener
{    
    public static UI instance;
    
    private Game game;

    private JLabel leftShipCountLabel;
    private JLabel rightShipCountLabel;
    private JLabel leftScoreLabel;
    private JLabel rightScoreLabel;
    private JLabel winnerLabel;

    /**
     * @param game The game window context
     * @param match The match context
     */
    public UI (Game game)
    {
        instance = this;

        this.game = game;

        loadMenu();
    }

    public void unload()
    {
        for (var c: game.getComponents())
        {
            game.remove(c);
        }
    }

    public void loadMenu()
    {
        unload();

        var startLocalButton = new JButton("Start Local Game");
        startLocalButton.setBounds(650, 350, 300, 50);
        startLocalButton.addActionListener(e -> onStartLocalButton(e));
        
        var startServerButton = new JButton("Start Server");
        startServerButton.setBounds(650, 425, 300, 50);
        startServerButton.addActionListener(e -> onStartServerButton(e));
        
        var joinServerButton = new JButton("Join Server");
        joinServerButton.setBounds(650, 500, 300, 50);
        joinServerButton.addActionListener(e -> onJoinServerButton(e));
        
        game.add(joinServerButton);
        game.add(startServerButton);
        game.add(startLocalButton);

    }

    public void loadEnd(Result result, String winner)
    {
        unload();

        var labelFont = new JLabel().getFont();

        if(result != Result.TIE)
        {
            winnerLabel = new JLabel(winner + " won the game", JLabel.CENTER);
        }
        else {
            winnerLabel = new JLabel("Game has ended in a tie");
        }
        winnerLabel.setBounds(500, 100, 600, 200);
        winnerLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 40));
        
        var restartGameButton = new JButton("Restart Game");
        restartGameButton.setBounds(650, 385, 300, 50);
        restartGameButton.addActionListener(e -> onRestartGameButton(e));

        var exitGameButton = new JButton("Exit");
        exitGameButton.setBounds(650, 525, 300, 50);
        exitGameButton.addActionListener(e -> onExitGameButton(e));

        game.add(restartGameButton);
        game.add(exitGameButton);
        game.add(winnerLabel);
    }

    public void loadGame(String leftPlayerName, String rightPlayerName)
    {
        unload();
        
        var labelFont = new JLabel().getFont();

        var leftPlayerNameLabel = new JLabel(leftPlayerName);
        leftPlayerNameLabel.setBounds(32, 16, 256, 32);
        leftPlayerNameLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        leftScoreLabel = new JLabel("Points 0");
        leftScoreLabel.setBounds(296, 16, 128, 32);
        leftScoreLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        leftShipCountLabel = new JLabel("Ships 28", JLabel.RIGHT);
        leftShipCountLabel.setBounds(572, 16, 196, 32);
        leftShipCountLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        var rightPlayerNameLabel = new JLabel(rightPlayerName, JLabel.RIGHT);
        rightPlayerNameLabel.setBounds(1312, 16, 256, 32);
        rightPlayerNameLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));

        rightScoreLabel = new JLabel("0 Points", JLabel.RIGHT);
        rightScoreLabel.setBounds(1142, 16, 128, 32);
        rightScoreLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        rightShipCountLabel = new JLabel("28 Ships");
        rightShipCountLabel.setBounds(832, 16, 196, 32);
        rightShipCountLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));

        game.add(leftPlayerNameLabel);
        game.add(rightPlayerNameLabel);
        game.add(leftScoreLabel);
        game.add(rightScoreLabel);
        game.add(leftShipCountLabel);
        game.add(rightShipCountLabel);
    }

    private void onStartLocalButton(ActionEvent e)
    {
        loadGame("leftPlayerName", "rightPlayerName");
        new LocalMatch(game, GameConstants.leftOffset, GameConstants.rightOffset, GameConstants.tileSize, GameConstants.boardSize);
    }

    private void onStartServerButton(ActionEvent e)
    {
        loadGame("leftPlayerName", "rightPlayerName");
        
    }

    private void onJoinServerButton(ActionEvent e)
    {
        loadGame("leftPlayerName", "rightPlayerName");
    }

    private void onRestartGameButton(ActionEvent e)
    {
        loadGame("leftPlayerName", "rightPlayerName");
        new LocalMatch(game, GameConstants.leftOffset, GameConstants.rightOffset, GameConstants.tileSize, GameConstants.boardSize);
    }

    private void onExitGameButton(ActionEvent e)
    {
        loadMenu();
    }

    public void onShipCountChanged(int leftShipCount, int rightShipCount)
    {
        if (leftShipCountLabel != null) leftShipCountLabel.setText("Ships " + leftShipCount);
        if (rightShipCountLabel != null) rightShipCountLabel.setText(rightShipCount + " Ships");
    }
    
    public void onScoreChanged(int leftScore, int rightScore)
    {
        if (leftScoreLabel != null) leftScoreLabel.setText("Points " + leftScore);
        if (rightScoreLabel != null) rightScoreLabel.setText(rightScore + " Points");
    }
}
