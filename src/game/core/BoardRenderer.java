package game.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BoardRenderer
{
    private final Graphics graphics;
    private static Vector2Int leftOffset;
    private static Vector2Int rightOffset;
    private static int tileSize;
    private static Vector2Int boardSize;

    public BoardRenderer(Graphics graphics, Vector2Int leftOffset, Vector2Int rightOffset, int tileSize, Vector2Int boardSize)
    {
        this.graphics = graphics;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.tileSize = tileSize;
        this.boardSize = boardSize;
    }

    public void drawLeftField(BufferedImage image, Vector2Int position)
    {
        var posX = position.x * tileSize + leftOffset.x;
        var posY = position.y * tileSize + leftOffset.y;

        graphics.drawImage(image, posX, posY, image.getWidth(), image.getHeight(), null);
    }

    public void drawRightField(BufferedImage image, Vector2Int position)
    {
        var posX = position.x * tileSize + rightOffset.x;
        var posY = position.y * tileSize + rightOffset.y;

        graphics.drawImage(image, posX, posY, image.getWidth(), image.getHeight(), null);
    }

    public static boolean inLeftBounds(Vector2 worldPos)
    {
        return (worldPos.x >  leftOffset.x && worldPos.x < leftOffset.x + boardSize.x * tileSize) &&
                (worldPos.y > leftOffset.y && worldPos.y < leftOffset.y + boardSize.y * tileSize);
    }

    public static boolean inRightBounds(Vector2 worldPos)
    {
        return (worldPos.x >  rightOffset.x && worldPos.x < rightOffset.x + boardSize.x * tileSize) &&
                (worldPos.y > rightOffset.y && worldPos.y < rightOffset.y + boardSize.y * tileSize);
    }

    public static Vector2Int worldToCell(Vector2 worldPos)
    {
        if (inLeftBounds(worldPos))
        {
            return new Vector2Int((int)Math.floor((worldPos.x - leftOffset.x) / tileSize),
                                    (int)Math.floor((worldPos.y - leftOffset.y) / tileSize));
        }
        else if (inRightBounds(worldPos))
        {
            return new Vector2Int((int)Math.floor((worldPos.x - 2 * leftOffset.x - boardSize.x * tileSize) / tileSize),
                                    (int)Math.floor((worldPos.y - leftOffset.y) / tileSize));
        }
        else return null;
    }
}
