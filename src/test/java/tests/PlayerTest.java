package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import game.*;
import game.core.Vector2Int;

public class PlayerTest implements IPlayerListener 
{

    Player player;
    String playerName = "Erwin";

    @BeforeEach
    public void setUp()
    {
        player = new Human(playerName, new LocalMatch());
    }

    @Test
    public void shouldHaveSameName()
    {
        assertEquals(playerName, player.name);
    }
}
