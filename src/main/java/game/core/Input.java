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
            System.out.println("maus");
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

    /**
     * Invokes the KeyDown event
     * @param keyCode The keyCode of the pressed key
     */
    private void invokeKeyDown(int keyCode)
    {
        for (var listener : listeners)
        {
            listener.onKeyDown(keyCode);
        }
    }

    /**
     * Invokes the KeyUp event
     * @param keyCode The keyCode of the released key
     */
    private void invokeKeyUp(int keyCode)
    {
        for (var listener : listeners)
        {
            listener.onKeyUp(keyCode);
        }
    }

    /**
     * Invokes the MouseDown event
     * @param mousePos The current mouse position in world space
     */
    private void invokeMouseDown(Vector2 mousePos)
    {
        for (var listener : listeners)
        {
            listener.onMouseDown(mousePos);
        }
    }

    /**
     * Invokes the MouseUp event
     * @param mousePos The current mouse position in world space
     */
    private void invokeMouseUp(Vector2 mousePos)
    {
        for (var listener : listeners)
        {
            listener.onMouseUp(mousePos);
        }
    }

    /**
     * Adds an IInputListener to the list of observers
     * @param listener The listener to add
     */
    public static void addListener(IInputListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Remvoves an IInputListener from the list of observers
     * @param listener The listener to remove
     */
    public static void removeListener(IInputListener listener)
    {
        listeners.remove(listener);
    }
}
