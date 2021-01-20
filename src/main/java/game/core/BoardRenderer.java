package game.core;

import game.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BoardRenderer implements IRenderable
{
    private Vector2Int offset;
    private int tileSize;
    private Board board;

    /**
     * @param offset The pixel offset of the board
     * @param tileSize The pixel size of a tile
     */
    public BoardRenderer(Board board, Vector2Int offset, int tileSize)
    {
        System.out.println("Hier bin ich im constructor");
        this.offset = offset;
        this.tileSize = tileSize;
        this.board = board;

        Game.addRenderable(this);
    }

    public void dispose()
    {
        Game.removeRenderable(this);
    }

    private void drawField(Graphics graphics, BufferedImage sprite, Vector2Int position)
    {
        var posX = position.x * tileSize + offset.x;
        var posY = position.y * tileSize + offset.y;

        graphics.drawImage(sprite, posX, posY, sprite.getWidth() / 2, sprite.getHeight() / 2, null);
    }

	/**
	 * Gets invoked every render frame
	 * @param renderer The renderer context
	 */

	@Override
	public void render(Graphics graphics)
	{
		for (int x = 0; x < board.getSize().x; x++)
		{
			for (int y = 0; y < board.getSize().y; y++)
			{
				var ship = board.getShip(x, y);
				var position = new Vector2Int(x, y);

				var sprite = Resources.SPRITE_NULL;
                
                if (board.isGuessed(position))
                {
                    if (ShipType.isShip(ship)) sprite = Resources.SHIP_GUESSED_DUMMY;
                    else sprite = Resources.WATER_GUESSED_DUMMY;
                }
                else
                {
                    switch (ship)
                    {
                        case WATER -> sprite = Resources.WATER_DUMMY;
                        case PATROL -> sprite = Resources.PATROL;
                        case SUPER_PATROL_FRONT -> sprite = Resources.SUPER_PATROL_FRONT;
                        case SUPER_PATROL_BACK -> sprite = Resources.SUPER_PATROL_BACK;
                        case DESTROYER_FRONT -> sprite = Resources.DESTROYER_FRONT;
                        case DESTROYER_MID -> sprite = Resources.DESTROYER_MID;
                        case DESTROYER_BACK -> sprite = Resources.DESTROYER_BACK;
                        case BATTLESHIP_FRONT -> sprite = Resources.BATTLESHIP_FRONT;
                        case BATTLESHIP_FRONT_MID -> sprite = Resources.BATTLESHIP_FRONT_MID;
                        case BATTLESHIP_BACK_MID -> sprite = Resources.BATTLESHIP_BACK_MID;
                        case BATTLESHIP_BACK -> sprite = Resources.BATTLESHIP_BACK;
                        case CARRIER_FRONT -> sprite = Resources.CARRIER_FRONT;
                        case CARRIER_FRONT_MID -> sprite = Resources.CARRIER_FRONT_MID;
                        case CARRIER_MID -> sprite = Resources.CARRIER_MID;
                        case CARRIER_BACK_MID -> sprite = Resources.CARRIER_BACK_MID;
                        case CARRIER_BACK -> sprite = Resources.CARRIER_BACK;
                        default -> sprite = Resources.SPRITE_NULL;
                    }
                }

                drawField(graphics, sprite, position);
			}
		}
	}
}
