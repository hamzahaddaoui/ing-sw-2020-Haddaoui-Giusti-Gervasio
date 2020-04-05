package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BillboardTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTowerHeight() {
        Billboard billboard = new Billboard();
        billboard.incrementTowerHeight(new Position(2,3));
        assertEquals(1, billboard.getTowerHeight(new Position (2,3)),"ok");

    }

    @Test
    void testGetTowerHeight() {
    }

    @Test
    void incrementTowerHeight() {
    }

    @Test
    void getDome() {
    }

    @Test
    void testGetDome() {
    }

    @Test
    void setDome() {
    }

    @Test
    void getPlayer() {
    }

    @Test
    void testGetPlayer() {
    }

    @Test
    void setPlayer() {
    }

    @Test
    void resetPlayer() {
    }
}