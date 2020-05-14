/*package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class PlacingWorkersStatusTest {

    View view;
    Controller controller;
    ControlState state;
    GameBoard gameBoard ;
    Player player ;
    Set<Position> placingAvailableCells ;

    void setCells() {
        placingAvailableCells = new HashSet<>();

        for (int x = 0; x<5; x++)
            for (int y=0; y<5; y++)
                placingAvailableCells.add(new Position(x,y));
    }

    @BeforeEach
    void setUp(){
        view = new View();
        controller = new Controller();
        gameBoard = View.getGameBoard();
        player = View.getPlayer();
        controller.setState(new PlacingWorkersStatus());
        state = controller.getControlState();
        setCells();
        gameBoard.setPlacingAvailableCells(placingAvailableCells);
    }

    @Test
    void processingMessage() {
        String viewObject = "12";
        assertFalse(state.processingMessage(viewObject));
        assertFalse(state.processingMessage(viewObject));
        viewObject = "334";
        assertFalse(state.processingMessage(viewObject));
        viewObject = "";
        assertFalse(state.processingMessage(viewObject));
        viewObject = "03";
        assertFalse(state.processingMessage(viewObject));
        viewObject = "45";
        assertTrue(state.processingMessage(viewObject));
    }

}*/