package game.core;

import game.GameConstants;
import game.LocalMatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI
{    
    private Game game;
    
    private JLabel leftShipCountLabel;
    private JLabel rightShipCountLabel;
    private JLabel leftScoreLabel;
    private JLabel rightScoreLabel;

    /**
     * @param game The game window context
     * @param match The match context
     */
    public UI (Game game)
    {
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

    public void loadEnd()
    {
        unload();

        var restartGameButton = new JButton("Restart Game");
        restartGameButton.setBounds(650, 350, 300, 50);

        var exitGameButton = new JButton("Exit");
        exitGameButton.setBounds(650, 350, 300, 50);

        game.add(restartGameButton);
        game.add(exitGameButton);
    }

    public void loadGame(String leftPlayerName, String rightPlayerName)
    {
        unload();
        
        var labelFont = new JLabel().getFont();

        var leftPlayerNameLabel = new JLabel(leftPlayerName);
        leftPlayerNameLabel.setBounds(64, 16, 128, 32);
        leftPlayerNameLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        leftScoreLabel = new JLabel("0");
        leftScoreLabel.setBounds(256, 16, 128, 32);
        leftScoreLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        leftShipCountLabel = new JLabel("28");
        leftShipCountLabel.setBounds(512, 16, 128, 32);
        leftShipCountLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        var rightPlayerNameLabel = new JLabel(rightPlayerName);
        rightPlayerNameLabel.setBounds(768, 16, 128, 32);
        rightPlayerNameLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));

        rightScoreLabel = new JLabel("0");
        rightScoreLabel.setBounds(900, 16, 128, 32);
        rightScoreLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        rightShipCountLabel = new JLabel("28");
        rightShipCountLabel.setBounds(1376, 16, 128, 32);
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
}
