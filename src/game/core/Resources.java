package game.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Resources
{
    public static BufferedImage SPRITE_NULL;
    public static BufferedImage WATER_DUMMY;
    public static BufferedImage SHIP_DUMMY;
    public static BufferedImage WATER_GUESSED_DUMMY;
    public static BufferedImage SHIP_GUESSED_DUMMY;

    public static void Initialize() throws IOException
    {
        SPRITE_NULL = ImageIO.read(new File("Resources\\null.png"));
        WATER_DUMMY = ImageIO.read(new File("Resources\\water.png"));
        SHIP_DUMMY = ImageIO.read(new File("Resources\\ship.png"));
        WATER_GUESSED_DUMMY = ImageIO.read(new File("Resources\\water_guessed.png"));
        SHIP_GUESSED_DUMMY = ImageIO.read(new File("Resources\\ship_guessed.png"));
    }
}
