package game.core;

import game.LocalMatch;
import game.networking.ClientConnection;
import game.networking.ServerConnection;

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

    private JConsole console;
    private int logs;

    /**
     * @param game The game window context
     */
    public UI (Game game)
    {
        instance = this;

        this.game = game;

        loadMenu();
    }

    /**
     * Unloads all loaded swing components
     */
    public void unload()
    {
        for (var c: game.getComponents())
        {
            game.remove(c);
        }
    }

    /**
     * Loads and populates the menu swing components
     */
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

        var joinServerAsComputerButton = new JButton("Join Server as Computer");
        joinServerAsComputerButton.setBounds(650, 575, 300, 50);
        joinServerAsComputerButton.addActionListener(e -> onJoinServerAsComputerButton(e));
        
        game.add(joinServerButton);
        game.add(startServerButton);
        game.add(startLocalButton);
        game.add(joinServerAsComputerButton);

    }

    /**
     * Loads and populates the server swing components
     */
    public void loadServer()
    {
        unload();

        console = new JConsole(160,10000);
        console.setCursorVisible(true);
        console.setCursorBlink(true);
        console.setBounds(0, 0, 1600, 900);

        game.add(console);
        // console.captureStdOut();
        console.setAutoscrolls(true);
    }


    /**
     * Logs a message to the server console
     * @param message The message to log
     */
    public void log(String message)
    {
        System.out.println(message);

        if (console == null) return;

        logs += 1;

        if (logs % 5 == 0) console.clearScreen();

        console.writeln(message);
    }

    /**
     * Loads and populates the end screen swing components
     * @param result The result type of the match
     * @param winner The name of the winning player
     * @param isLocal Whether the match was a locally played match
     * @param isRegularWin Whether the match was won by score or by a player disconnecting
     */
    public void loadEnd(Result result, String winner, boolean isLocal, boolean isRegularWin)
    {
        unload();

        var labelFont = new JLabel().getFont();

        winnerLabel = new JLabel("", JLabel.CENTER);

        if (result != Result.TIE)
        {
            
            winnerLabel.setText(winner + " won the game" + (isRegularWin ? "!" : " because of a disconnect!"));
        }
        else 
        {
            winnerLabel.setText("The game has ended in a tie!");
        }
        winnerLabel.setBounds(0, 100, 1600, 200);
        winnerLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 40));
        var restartGameButton = new JButton("Restart Game");
        restartGameButton.setBounds(650, 385, 300, 50);
        restartGameButton.addActionListener(e -> onRestartGameButton(e));

        var exitGameButton = new JButton("Exit");
        exitGameButton.setBounds(650, 525, 300, 50);
        exitGameButton.addActionListener(e -> onExitGameButton(e));
        
        if (isLocal) game.add(restartGameButton);
        game.add(exitGameButton);
        game.add(winnerLabel);
    }

    /**
     * Loads and populates the ingame swing components
     */
    public void loadGame(String leftPlayerName, String rightPlayerName)
    {
        unload();
        
        var labelFont = new JLabel().getFont();

        var leftPlayerNameLabel = new JLabel(leftPlayerName);
        leftPlayerNameLabel.setBounds(32, 16, 256, 32);
        leftPlayerNameLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        leftScoreLabel = new JLabel("Points 0");
        leftScoreLabel.setBounds(296, 16, 256, 32);
        leftScoreLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        leftShipCountLabel = new JLabel("Ships 28", JLabel.RIGHT);
        leftShipCountLabel.setBounds(572, 16, 196, 32);
        leftShipCountLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        var rightPlayerNameLabel = new JLabel(rightPlayerName, JLabel.RIGHT);
        rightPlayerNameLabel.setBounds(1312, 16, 256, 32);
        rightPlayerNameLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 32));

        rightScoreLabel = new JLabel("0 Points", JLabel.RIGHT);
        rightScoreLabel.setBounds(1014, 16, 256, 32);
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

    /**
     * Invoked when the start local game button is clicked
     * @param e The ActionEvent context
     */
    private void onStartLocalButton(ActionEvent e)
    {
        new LocalMatch(this, false);
    }

    /**
     * Invoked when the start server button is clicked
     * @param e The ActionEvent context
     */
    private void onStartServerButton(ActionEvent e)
    {
        loadServer(); 
        new ServerConnection();  
    }

    /**
     * Invoked when the join server game button is clicked
     * @param e The ActionEvent context
     */
    private void onJoinServerButton(ActionEvent e)
    {
        unload();
        new ClientConnection(false);
    }

    /**
     * Invoked when the restart local game button is clicked
     * @param e The ActionEvent context
     */
    private void onRestartGameButton(ActionEvent e)
    {
        new LocalMatch(this, false);
    }

    /**
     * Invoked when the exit button is clicked
     * @param e The ActionEvent context
     */
    private void onExitGameButton(ActionEvent e)
    {
        loadMenu();
    }

    /**
     * Invoked when the join server game as computer button is clicked
     * @param e The ActionEvent context
     */
    private void onJoinServerAsComputerButton(ActionEvent e)
    {
        unload();
        new ClientConnection(true);
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
