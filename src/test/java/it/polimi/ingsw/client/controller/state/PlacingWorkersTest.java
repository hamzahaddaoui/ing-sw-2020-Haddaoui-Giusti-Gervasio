package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlacingWorkersTest {

    View view = new View();
    Controller controller;
    ControlState state;
    Set<Position> placingAvailableCells ;
    String input;
    Map<Position, Cell> billBoardStatus;


    void setCells() {

        for (int x = 0; x<5; x++)
            for (int y=0; y<5; y++)
                placingAvailableCells.add(new Position(x,y));
    }

    void setBillBoardStatus(){
        for(int i=0;i<5; i++){
            for(int j=0; i<5; j++){
                billBoardStatus.put(new Position(i,j), new Cell());
            }
        }
    }

    @BeforeEach
    void setUp(){
        controller = new Controller();
        DataBase.setControlState(new PlacingWorkers());
        state = DataBase.getControlState();
        billBoardStatus = new HashMap<>();
        placingAvailableCells = new HashSet<>();
        DataBase.setPlacingAvailableCells(new HashSet<>());
        DataBase.setMatchPlayers(new HashMap<>());
    }

    @Test
    void computeInput() {
        input="  ";
        System.out.println(state);

        assertThrows(IllegalArgumentException.class, () -> state.computeInput(input) );

        setCells();
        DataBase.setPlacingAvailableCells(placingAvailableCells);
        input = "78";
        state.computeInput(input);

        assertFalse(DataBase.isMessageReady());
        assertTrue(DataBase.isActiveInput());

        DataBase.getPlacingAvailableCells().remove(new Position(1,1));
        input = "22";
        state.computeInput(input);

        assertFalse(DataBase.isMessageReady());
        assertTrue(DataBase.isActiveInput());

        input = "33";
        state.computeInput(input);

        assertFalse(DataBase.isMessageReady());
        assertTrue(DataBase.isActiveInput());

        input = "13";
        state.computeInput(input);

        assertTrue(DataBase.isMessageReady());
        assertTrue(DataBase.isActiveInput());
        assertTrue(DataBase.getPlayerState() == PlayerState.IDLE);
    }

    @Test
    void updateData() {
       /* MessageEvent message = new MessageEvent();

        message.setInfo(null);
        DataBase.setMatchState(MatchState.PLACING_WORKERS);
        setCells();
        message.setAvailablePlacingCells(placingAvailableCells);
        setBillBoardStatus();
        message.setBillboardStatus(billBoardStatus);
        state.updateData(message);

        assertSame(DataBase.getPlayerState(), MatchState.PLACING_WORKERS);
        assertNotNull(DataBase.getBillboardStatus());
        assertNotNull(DataBase.getPlacingAvailableCells());

        DataBase.setPlayerState(PlayerState.ACTIVE);
        DataBase.setMatchState(null);
        state.updateData(message);

        assertTrue(DataBase.isActiveInput());*/
    }

    @Test
    void computeView() {
    }

    @Test
    void error() {
    }
}