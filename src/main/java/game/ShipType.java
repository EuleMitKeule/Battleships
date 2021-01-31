package game;

public enum ShipType
{
    WATER,
    PATROL,
    SUPER_PATROL_FRONT,
    SUPER_PATROL_BACK,
    DESTROYER_FRONT,
    DESTROYER_MID,
    DESTROYER_BACK,
    BATTLESHIP_FRONT,
    BATTLESHIP_FRONT_MID,
    BATTLESHIP_BACK_MID,
    BATTLESHIP_BACK,
    CARRIER_FRONT,
    CARRIER_FRONT_MID,
    CARRIER_MID,
    CARRIER_BACK_MID,
    CARRIER_BACK,

    //only used in our client, not for network purposes

    SUPER_PATROL,
    DESTROYER,
    BATTLESHIP,
    CARRIER;

    /**
     * @param shipType The ship type to check
     * @return Whether the specified ship type is not water
     */
    public static boolean isShip(ShipType shipType)
    {
        return 
            shipType == PATROL ||
            shipType == SUPER_PATROL_FRONT ||
            shipType == SUPER_PATROL_BACK ||
            shipType == DESTROYER_FRONT ||
            shipType == DESTROYER_MID ||
            shipType == DESTROYER_BACK ||
            shipType == BATTLESHIP_FRONT ||
            shipType == BATTLESHIP_FRONT_MID ||
            shipType == BATTLESHIP_BACK_MID ||
            shipType == BATTLESHIP_BACK ||
            shipType == CARRIER_FRONT ||
            shipType == CARRIER_FRONT_MID ||
            shipType == CARRIER_MID ||
            shipType == CARRIER_BACK_MID ||
            shipType == CARRIER_BACK;
    }
}
