package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Cell;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
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
        assertFalse(billboard.getDome(position));

        billboard.incrementTowerHeight(position);
        assertEquals(3, billboard.getTowerHeight(position));
        assertTrue(billboard.getDome(position));
    }

    @Test
    void setDome() {
        assertFalse(billboard.getDome(position));
        billboard.setDome(position);
        assertTrue(billboard.getDome(position));
    }

    @Test
    void setPlayer() {
        assertEquals(0, billboard.getPlayer(position));
        billboard.setPlayer(position, 10);
        assertEquals(10, billboard.getPlayer(position));
    }

    @Test
    void resetPlayer() {
        billboard.setPlayer(position, 10);
        billboard.resetPlayer(position);
        assertEquals(0, billboard.getPlayer(position));
    }

    @Test
    void generalTest(){
        int x, y;
        for(x=0; x<Billboard.ROWS; x++){
            for(y=0; y<Billboard.COLOUMNS; y++){
                Position position = new Position(x,y);
                billboard.incrementTowerHeight(position);
                billboard.incrementTowerHeight(position);
                billboard.incrementTowerHeight(position);
                billboard.setPlayer(position, x+y);
            }
        }

        Map<Position, Cell> cells = billboard.getCells();


        for(x=0; x<Billboard.ROWS; x++){
            for(y=0; y<Billboard.COLOUMNS; y++){
                Position position = new Position(x,y);
                assertEquals(cells.get(position).getTowerHeight(), 3);
                assertFalse(cells.get(position).isDome());
                assertEquals(cells.get(position).getPlayerID(), x+y);
            }
        }


        for(x=0; x<Billboard.ROWS; x++){
            for(y=0; y<Billboard.COLOUMNS; y++){
                Position position = new Position(x,y);
                billboard.resetPlayer(position);
                billboard.incrementTowerHeight(position);

            }
        }

        cells = billboard.getCells();


        for(x=0; x<Billboard.ROWS; x++){
            for(y=0; y<Billboard.COLOUMNS; y++){
                Position position = new Position(x,y);
                assertEquals(cells.get(position).getTowerHeight(), 3);
                assertTrue(cells.get(position).isDome());
                assertEquals(0, cells.get(position).getPlayerID());
            }
        }
    }
}