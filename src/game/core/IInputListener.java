package game.core;

import java.security.Key;

public interface IInputListener
{
    void onKeyDown(int keyCode);

    void onKeyUp(int keyCode);

    void onMouseDown(Vector2 mousePos);

    void onMouseUp(Vector2 mousePos);
}
