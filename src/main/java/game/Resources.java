package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Resources 
{
    public static BufferedImage SPRITE_NULL;
    public static BufferedImage WATER_DUMMY;
    public static BufferedImage SHIP_DUMMY;
    public static BufferedImage WATER_GUESSED_DUMMY;
    public static BufferedImage SHIP_GUESSED_DUMMY;
    public static BufferedImage PATROL;
    public static BufferedImage SUPER_PATROL_FRONT;
    public static BufferedImage SUPER_PATROL_BACK;
    public static BufferedImage DESTROYER_FRONT;
    public static BufferedImage DESTROYER_MID;
    public static BufferedImage DESTROYER_BACK;
    public static BufferedImage BATTLESHIP_FRONT;
    public static BufferedImage BATTLESHIP_FRONT_MID;
    public static BufferedImage BATTLESHIP_BACK_MID;
    public static BufferedImage BATTLESHIP_BACK;
    public static BufferedImage CARRIER_FRONT;
    public static BufferedImage CARRIER_FRONT_MID;
    public static BufferedImage CARRIER_MID;
    public static BufferedImage CARRIER_BACK_MID;
    public static BufferedImage CARRIER_BACK;

    /**
     * Loads the sprite assets
     * @throws IOException
     */
    public static void Initialize() throws IOException 
    {
        SPRITE_NULL = ImageIO.read(new File("Resources\\null.png"));
        WATER_DUMMY = ImageIO.read(new File("Resources\\water.png"));
        SHIP_DUMMY = ImageIO.read(new File("Resources\\ship.png"));
        WATER_GUESSED_DUMMY = ImageIO.read(new File("Resources\\water_guessed.png"));
        SHIP_GUESSED_DUMMY = ImageIO.read(new File("Resources\\ship_guessed.png"));

        PATROL = ImageIO.read(new File("Resources\\patrol.png"));
        SUPER_PATROL_FRONT = ImageIO.read(new File("Resources\\super_patrol_front.png"));
        SUPER_PATROL_BACK = ImageIO.read(new File("Resources\\super_patrol_back.png"));
        DESTROYER_FRONT = ImageIO.read(new File("Resources\\destroyer_front.png"));
        DESTROYER_MID = ImageIO.read(new File("Resources\\destroyer_mid.png"));
        DESTROYER_BACK = ImageIO.read(new File("Resources\\destroyer_back.png"));
        BATTLESHIP_FRONT = ImageIO.read(new File("Resources\\battleship_front.png"));
        BATTLESHIP_FRONT_MID = ImageIO.read(new File("Resources\\battleship_front_mid.png"));
        BATTLESHIP_BACK_MID = ImageIO.read(new File("Resources\\battleship_back_mid.png"));
        BATTLESHIP_BACK = ImageIO.read(new File("Resources\\battleship_back.png"));
        CARRIER_FRONT = ImageIO.read(new File("Resources\\carrier_front.png"));
        CARRIER_FRONT_MID = ImageIO.read(new File("Resources\\carrier_front_mid.png"));
        CARRIER_MID = ImageIO.read(new File("Resources\\carrier_mid.png"));
        CARRIER_BACK_MID = ImageIO.read(new File("Resources\\carrier_back_mid.png"));
        CARRIER_BACK = ImageIO.read(new File("Resources\\carrier_back.png"));

    }

    /**
     * @return Returns a linked list containing the ship queue
     * that was defined in the shipQueue.txt file
     * or null if the file can not be loaded
     */
    public static LinkedList<ShipType> getShipQueue() 
    {
        try 
        {
            var scanner = new Scanner(new File("Resources\\shipQueue.txt"));
            var ships = new LinkedList<String>();
            var shipQueue = new LinkedList<ShipType>();
    
            while (scanner.hasNext())
            {
                ships.add(scanner.next());
            }
    
            scanner.close();
    
            for (var ship : ships)
            {
                shipQueue.add(ShipType.valueOf(ship));
            }

            return shipQueue;
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
}
