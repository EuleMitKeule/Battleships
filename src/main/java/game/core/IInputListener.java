package game.core;

public interface IInputListener
{
    /**
     * Invoked when a key is pressed down
     * @param keyCode The keyCode of the pressed key
     */
    default void onKeyDown(int keyCode) { }

    /**
     * Invoked when a key is let go
     * @param keyCode The keyCode of the pressed key
     */
    default void onKeyUp(int keyCode) { }

    /**
     * Invoked when a mouse button is pressed down
     * @param mousePos The current mouse position
     */
    default void onMouseDown(Vector2 mousePos) { }

    /**
     * Invoked when a mouse button is let go
     * @param mousePos The current mouse position
     */
    default void onMouseUp(Vector2 mousePos) { }
}
