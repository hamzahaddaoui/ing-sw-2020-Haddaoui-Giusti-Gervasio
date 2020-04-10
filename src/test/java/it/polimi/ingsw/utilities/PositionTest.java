package it.polimi.ingsw.utilities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    Position position;

    @Test
    void testNeighbourPositionsCentered() {
        Set<Position> returnValue = new HashSet<>();
        position= new Position(3,3);
        returnValue.add(new Position(2,2));
        returnValue.add(new Position(2,3));
        returnValue.add(new Position(2,4));
        returnValue.add(new Position(3,2));
        returnValue.add(new Position(3,4));
        returnValue.add(new Position(4,2));
        returnValue.add(new Position(4,3));
        returnValue.add(new Position(4,4));
        assertEquals(returnValue, position.neighbourPositions());
    }

    @Test
    void testNeighbourPositionsUpLeftCorner() {
        Set<Position> returnValue = new HashSet<>();
        position= new Position(0,0);
        returnValue.add(new Position(1,1));
        returnValue.add(new Position(0,1));
        returnValue.add(new Position(1,0));
        assertEquals(returnValue, position.neighbourPositions());
    }

    @Test
    void testNeighbourPositionsDownLeftCorner() {
        Set<Position> returnValue = new HashSet<>();
        position= new Position(4,0);
        returnValue.add(new Position(3,1));
        returnValue.add(new Position(4,1));
        returnValue.add(new Position(3,0));
        assertEquals(returnValue, position.neighbourPositions());
    }

    @Test
    void testNeighbourPositionsUpRightCorner() {
        Set<Position> returnValue = new HashSet<>();
        position= new Position(0,4);
        returnValue.add(new Position(0,3));
        returnValue.add(new Position(1,3));
        returnValue.add(new Position(1,4));
        assertEquals(returnValue, position.neighbourPositions());
    }

    @Test
    void testNeighbourPositionsDownRightCorner() {
        Set<Position> returnValue = new HashSet<>();
        position= new Position(4,4);
        returnValue.add(new Position(3,3));
        returnValue.add(new Position(3,4));
        returnValue.add(new Position(4,3));
        assertEquals(returnValue, position.neighbourPositions());
    }


    @Test
    void testCheckMutualPosition() {
        position= new Position(3,3);
        assertEquals(CardinalDirection.NORTH, position.checkMutualPosition(new Position(2,3)));
        assertEquals(CardinalDirection.SOUTH, position.checkMutualPosition(new Position(4,3)));
        assertEquals(CardinalDirection.WEST, position.checkMutualPosition(new Position(3,2)));
        assertEquals(CardinalDirection.EAST, position.checkMutualPosition(new Position(3,4)));
        assertEquals(CardinalDirection.NORTHWEST, position.checkMutualPosition(new Position(2,2)));
        assertEquals(CardinalDirection.NORTHEAST, position.checkMutualPosition(new Position(2,4)));
        assertEquals(CardinalDirection.SOUTHWEST, position.checkMutualPosition(new Position(4,2)));
        assertEquals(CardinalDirection.SOUTHEAST, position.checkMutualPosition(new Position(4,4)));
    }

    @Test
    void testMutualPositionNotNeighbouring() {
        position= new Position(3,3);
        assertEquals(null, position.checkMutualPosition(new Position(1,1)));
    }
}