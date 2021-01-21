package game.networking;

public enum MessageId 
{
    CLIENT_HANDSHAKE,
    SERVER_HANDSHAKE,
    NAME_EXISTS,
    ENEMY_NAME,
    CLIENT_BOARD,
    GAME_SETUP,
    MOVE,
    UPDATE,
    GAME_OVER,
    EXIT
}
