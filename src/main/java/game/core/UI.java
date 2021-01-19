package game.core;

import game.GameConstants;
import game.IMatchListener;
import game.LocalMatch;
import game.Match;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI
{    
    private JLabel shipCount;
    private Game game;
    private Match match;

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
        startServerButton.addActionListener(e -> onStartLocalButton(e));
        
        var joinServerButton = new JButton("Join Server");
        joinServerButton.setBounds(650, 500, 300, 50);
        joinServerButton.addActionListener(e -> onStartLocalButton(e));
        
        game.add(joinServerButton);
        game.add(startServerButton);
        game.add(startLocalButton);

    }

    public void loadGame(String leftPlayerName, String rightPlayerName)
    {
        unload();
        
        var labelFont = new JLabel().getFont();

        var leftPlayerNameLabel = new JLabel(leftPlayerName);
        leftPlayerNameLabel.setBounds(64, 16, 128, 32);
        leftPlayerNameLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));

        var rightPlayerNameLabel = new JLabel(rightPlayerName);
        rightPlayerNameLabel.setBounds(768, 16, 128, 32);
        rightPlayerNameLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        shipCount = new JLabel("");
        shipCount.setBounds(1376, 16, 128, 32);
        shipCount.setFont(new Font(labelFont.getName(), Font.BOLD, 32));

        game.add(leftPlayerNameLabel);
        game.add(rightPlayerNameLabel);
        game.add(shipCount);
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
