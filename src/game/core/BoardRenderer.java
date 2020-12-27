package game.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BoardRenderer
{
    private Graphics graphics;
    private Vector2Int leftOffset;
    private Vector2Int rightOffset;
    private int tileSize;

    /**
     * @param graphics The graphics context
     * @param leftOffset The pixel offset of the left board
     * @param rightOffset The pixel offset of the right board
     * @param tileSize The pixel size of a tile
     */
    public BoardRenderer(Graphics graphics, Vector2Int leftOffset, Vector2Int rightOffset, int tileSize)
    {
        this.graphics = graphics;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.tileSize = tileSize;
    }

    /**
     * Draws one cell of the left board
     * @param image The sprite to draw
     * @param cellPos The cell position to draw at
     */
    public void drawLeftField(BufferedImage image, Vector2Int cellPos)
    {
        var posX = cellPos.x * tileSize + leftOffset.x;
        var posY = cellPos.y * tileSize + leftOffset.y;

        graphics.drawImage(image, posX, posY, image.getWidth(), image.getHeight(), null);
    }

    /**
     * Draws one cell of the right board
     * @param image The sprite to draw
     * @param position The cell position to draw at
     */
    public void drawRightField(BufferedImage image, Vector2Int position)
    {
        var posX = position.x * tileSize + rightOffset.x;
        var posY = position.y * tileSize + rightOffset.y;

        graphics.drawImage(image, posX, posY, image.getWidth(), image.getHeight(), null);
    }
}
