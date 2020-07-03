package it.polimi.ingsw.utilities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
* author: hamzahaddaoui
*
* Testing of the position utilities
*
 */

class PositionTest {
    Position position;

    @Test
    void testGeneral(){
        position = new Position (2,3,4);
        assertEquals(position.getX(), 2);
        assertEquals(position.getY(), 3);
        assertEquals(position.getZ(), 4);

        position.set(5,5);
        assertEquals(position.getX(), 5);
        assertEquals(position.getY(), 5);
        assertEquals(position.getZ(), 4);

        position.set(5,5,7);
        assertEquals(position.getX(), 5);
        assertEquals(position.getY(), 5);
        assertEquals(position.getZ(), 7);

        position.setX(3);
        assertEquals(position.getX(), 3);
        assertEquals(position.getY(), 5);
        assertEquals(position.getZ(), 7);


        position.setY(3);
        assertEquals(position.getX(), 3);
        assertEquals(position.getY(), 3);
        assertEquals(position.getZ(), 7);

        position.setZ(3);
        assertEquals(position.getX(), 3);
        assertEquals(position.getY(), 3);
        assertEquals(position.getZ(), 3);



    }


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

    @Test
    void testTranslateCardinalDirectionToPosition() {
        position= new Position(3,3);

        assertEquals(new Position(2,3), position.translateCardinalDirectionToPosition(CardinalDirection.NORTH));
        assertEquals(new Position(4,3), position.translateCardinalDirectionToPosition(CardinalDirection.SOUTH));
        assertEquals(new Position(3,2), position.translateCardinalDirectionToPosition(CardinalDirection.WEST));
        assertEquals(new Position(3,4), position.translateCardinalDirectionToPosition(CardinalDirection.EAST));
        assertEquals(new Position(2,2), position.translateCardinalDirectionToPosition(CardinalDirection.NORTHWEST));
        assertEquals(new Position(2,4), position.translateCardinalDirectionToPosition(CardinalDirection.NORTHEAST));
        assertEquals(new Position(4,2), position.translateCardinalDirectionToPosition(CardinalDirection.SOUTHWEST));
        assertEquals(new Position(4,4), position.translateCardinalDirectionToPosition(CardinalDirection.SOUTHEAST));
    }
}