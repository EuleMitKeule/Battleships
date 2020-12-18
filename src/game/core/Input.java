package game.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Input implements KeyListener, MouseListener
{
    private static ArrayList<IInputListener> listeners = new ArrayList<IInputListener>();

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        invokeKeyDown(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        invokeKeyUp(e.getKeyCode());
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            var mousePos = new Vector2(e.getPoint().x, e.getPoint().y);
            invokeMouseDown(mousePos);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            var mousePos = new Vector2(e.getPoint().x, e.getPoint().y);
            invokeMouseUp(mousePos);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    private void invokeKeyDown(int keyCode)
    {
        for (var listener : listeners)
        {
            listener.onKeyDown(keyCode);
        }
    }

    private void invokeKeyUp(int keyCode)
    {
        for (var listener : listeners)
        {
            listener.onKeyUp(keyCode);
        }
    }

    private void invokeMouseDown(Vector2 mousePos)
    {
        for (var listener : listeners)
        {
            listener.onMouseDown(mousePos);
        }
    }

    private void invokeMouseUp(Vector2 mousePos)
    {
        for (var listener : listeners)
        {
            listener.onMouseUp(mousePos);
        }
    }

    public static void addListener(IInputListener listener)
    {
        listeners.add(listener);
    }

    public static void removeListener(IInputListener listener)
    {
        listeners.remove(listener);
    }
}
