package game.core;

public interface IInputListener
{
    default void onKeyDown(int keyCode) { }

    default void onKeyUp(int keyCode) { }

    default void onMouseDown(Vector2 mousePos) { }

    default void onMouseUp(Vector2 mousePos) { }
}
