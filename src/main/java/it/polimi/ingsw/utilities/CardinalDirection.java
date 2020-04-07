package it.polimi.ingsw.utilities;

import java.util.HashMap;
import java.util.Map;

public enum CardinalDirection {
    NORTH (180.0),
    WEST (-90.0),
    EAST(90.0),
    SOUTH(0.0),
    NORTHEAST(135.0),
    SOUTHEAST(45.0),
    NORTHWEST(-135.0),
    SOUTHWEST(-45.0);

    double angle;
    private static Map<Double, CardinalDirection> map = new HashMap<>();

    CardinalDirection(double angle){
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
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
