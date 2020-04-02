package it.polimi.ingsw.utilities;

import java.util.HashMap;
import java.util.Map;

public enum CardinalDirection {
    NORTH (90.0),
    WEST (180.0),
    EAST(0.0),
    SOUTH(-90.0),
    NORTHEAST(45.0),
    SOUTHEAST(-45.0),
    NORTHWEST(135.0),
    SOUTHWEST(-135.0);

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
