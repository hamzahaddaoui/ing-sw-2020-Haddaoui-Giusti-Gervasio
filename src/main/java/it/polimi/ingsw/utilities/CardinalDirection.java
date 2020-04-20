package it.polimi.ingsw.utilities;

import java.util.HashMap;
import java.util.Map;

public enum CardinalDirection {
    NORTH (180.0, -1, 0),
    WEST (-90.0, 0, -1),
    EAST(90.0, 0, 1),
    SOUTH(0.0, 1, 0),
    NORTHEAST(135.0, -1, 1),
    SOUTHEAST(45.0, 1, 1),
    NORTHWEST(-135.0, -1, -1),
    SOUTHWEST(-45.0, 1, -1);

    double angle;
    private static final Map<Double, CardinalDirection> map = new HashMap<Double, CardinalDirection>();
    int xOffset;
    int yOffset;

    CardinalDirection(double angle, int xOffset, int yOffset){
        this.angle = angle;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    static {
        for (CardinalDirection cardinalDirection : CardinalDirection.values()) {
            map.put(cardinalDirection.angle, cardinalDirection);
        }
    }

    public static CardinalDirection valueOf(double angle) {
        return (CardinalDirection) map.get(angle);
    }
}
