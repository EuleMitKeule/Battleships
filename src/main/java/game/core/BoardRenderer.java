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
     * @param board The board that will be rendered
     * @param offset The pixel offset of the board
     * @param tileSize The pixel size of a tile
     */
    public BoardRenderer(Board board, Vector2Int offset, int tileSize)
    {
        this.offset = offset;
        this.tileSize = tileSize;
        this.board = board;

        Game.addRenderable(this);
    }

    /**
     * Event subscription cleanup
     */
    public void dispose()
    {
        Game.removeRenderable(this);
    }

    /**
     * Draws a sprite at a grid position
     * @param graphics The graphics context
     * @param sprite The sprite to draw
     * @param position The cell position to draw at
     */
    private void drawField(Graphics graphics, BufferedImage sprite, Vector2Int position)
    {        
        var posX = (int)(position.x * tileSize * GameConstants.scale  + offset.x);
        var posY = (int)(position.y * tileSize * GameConstants.scale + offset.y);

        graphics.drawImage(sprite, posX, posY, (int)(sprite.getWidth() * GameConstants.scale), (int)(sprite.getHeight() * GameConstants.scale), null);
    }

	/**
	 * Invoked every render frame
	 * @param graphics The graphics context
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
