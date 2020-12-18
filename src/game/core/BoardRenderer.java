package game.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BoardRenderer
{
    private final Graphics graphics;
    private final Vector2Int leftOffset;
    private final Vector2Int rightOffset;
    private final int tileSize;

    public BoardRenderer(Graphics graphics, Vector2Int leftOffset, Vector2Int rightOffset, int tileSize)
    {
        this.graphics = graphics;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.tileSize = tileSize;
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
}
