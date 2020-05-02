package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RunningStatusTest {

    View view = new View();
    Controller controller = new Controller();
    ControlState state;
    Set<Position> worker1 = new HashSet<>();
    Set<Position> worker2 = new HashSet<>();
    Map<Position,Set<Position>> testMap = new HashMap<>();
    Map<Position,Boolean> availableMap = new HashMap<>();
    Position posWorker1 = new Position(1,1);
    Position posWorker2 = new Position(3,4);
    Player player = View.getPlayer();
    GameBoard gameBoard = View.getGameBoard();

    void settingAvailableCells() {

        worker1.add(new Position(0,0));
        worker1.add(new Position(0,1));
        worker1.add(new Position(1,0));
        worker1.add(new Position(2,1));
        worker1.add(new Position(2,2));
        worker1.add(new Position(1,2));
        worker1.add(new Position(0,2));
        worker1.add(new Position(2,0));
        worker2.add(new Position(4,4));
        worker2.add(new Position(4,3));

        testMap.put(posWorker1,worker1);
        testMap.put(posWorker2,worker2);
    }

    void settingAvailableMap() {
        availableMap.put(posWorker1,true);
        availableMap.put(posWorker2,true);
    }

    @BeforeEach
    void setUp() {
        controller.setState(new RunningStatus());
        state = controller.getControlState();

        settingAvailableCells();
        gameBoard.setWorkersAvailableCells(testMap);
    }

    @AfterEach
    void tearDown() {testMap = null;}

    @Test
    void processingMessage_String() {
        assertThrows(IllegalArgumentException.class,() -> state.processingMessage("Vasio"));
    }


    @Test
    void simulateTurn() {
        gameBoard.setColoredPosition(posWorker1);
        gameBoard.setStartingPosition(null);
        settingAvailableMap();
        player.setSpecialFunctionAvailable(availableMap);
        MessageEvent msg = Controller.getMessage();
        ArrayList<Position> availableTest;

        state.processingMessage(InsertCharacter.D);
        assertEquals(posWorker2,gameBoard.getColoredPosition());
        state.processingMessage(InsertCharacter.A);
        assertEquals(posWorker1,gameBoard.getColoredPosition());
        state.processingMessage(InsertCharacter.A);
        assertEquals(posWorker2,gameBoard.getColoredPosition());
        state.processingMessage(InsertCharacter.ENTER);
        state.processingMessage(InsertCharacter.F);
        assertEquals(posWorker2,gameBoard.getStartingPosition());
        assertEquals(posWorker2,msg.getStartPosition());
        assertTrue(msg.getSpecialFunction());
        availableTest = new ArrayList<Position>(gameBoard.getWorkersAvailableCells().get(gameBoard.getStartingPosition()));
        gameBoard.setColoredPosition(availableTest.get(0));
        assertEquals(new Position(4,4),gameBoard.getColoredPosition());

        try { state.processingMessage(InsertCharacter.W);
        } catch (IllegalArgumentException ex) {
            ex.getMessage();
            assertEquals("you are trying to exit from your neighboring cells. Nice try! :)",ex.getMessage());
        }

        assertEquals(new Position(4,4),gameBoard.getColoredPosition());
        state.processingMessage(InsertCharacter.A);
        assertEquals(new Position(4,3),gameBoard.getColoredPosition());
        state.processingMessage(InsertCharacter.W);
        assertFalse(state.processingMessage(InsertCharacter.ENTER));
        state.processingMessage(InsertCharacter.S);
        state.processingMessage(InsertCharacter.D);
        assertTrue(state.processingMessage(InsertCharacter.ENTER));
        assertEquals(new Position(4,4),msg.getEndPosition());
    }

    @Test
    public void nextStateTest() {
        state.nextState(controller);
        assertEquals(WaitingStatus.class,controller.getControlState().getClass());
    }
}