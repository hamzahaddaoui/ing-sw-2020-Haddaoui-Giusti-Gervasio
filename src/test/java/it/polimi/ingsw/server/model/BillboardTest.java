package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class BillboardTest {
    Billboard billboard = new Billboard();
    Position position;
    Random random = new Random();
    @BeforeEach
    void setUp() {
        position = new Position(Math.abs(random.nextInt()%5),Math.abs(random.nextInt()%5));
    }

    @Test
    void testIncrementTowerHeight() {
        assertEquals(0, billboard.getTowerHeight(position));



        billboard.incrementTowerHeight(position);
        assertEquals(1, billboard.getTowerHeight(position));
        billboard.incrementTowerHeight(position);
        assertEquals(2, billboard.getTowerHeight(position));
        billboard.incrementTowerHeight(position);
        assertEquals(3, billboard.getTowerHeight(position));
        assertEquals(false, billboard.getDome(position));
        billboard.incrementTowerHeight(position);
        assertEquals(3, billboard.getTowerHeight(position));
        assertEquals(true, billboard.getDome(position));
    }

    @Test
    void setDome() {
        assertEquals(false, billboard.getDome(position));
        billboard.setDome(position);
        assertEquals(true, billboard.getDome(position));
        assertEquals(true, billboard.getDome().get(position));
    }

    @Test
    void setPlayer() {
        assertEquals(-1, billboard.getPlayer(position));
        billboard.setPlayer(position, 10);
        assertEquals(10, billboard.getPlayer(position));
        assertEquals(10, billboard.getPlayer().get(position));
    }

    @Test
    void resetPlayer() {
        billboard.setPlayer(position, 10);
        billboard.resetPlayer(position);
        assertEquals(-1, billboard.getPlayer(position));
        assertEquals(-1, billboard.getPlayer().get(position));
    }
}