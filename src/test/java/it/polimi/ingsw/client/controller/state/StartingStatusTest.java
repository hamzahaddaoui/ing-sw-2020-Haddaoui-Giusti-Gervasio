package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartingStatusTest {

    Controller controller = new Controller();
    ControlState state;

    @BeforeEach
    void setUp() {
        controller.setState(new StartingStatus());
        state = controller.getControlState();
    }

    @AfterEach
    void tearDown() {}

    @Test
    void processingMessage_Nickname() {
        assertTrue(state.processingMessage("Vasio"));
        assertEquals("Vasio", Controller.getMessage().getNickname());
    }

    @Test
    void processingMessage_NullMessage_ExpectingThrowsException() {
        Exception exception;
        String nullString = null;
        exception = assertThrows(NullPointerException.class,() -> state.processingMessage(nullString));
    }

    @Test
    void processingMessage_EmptyMessage_ExpectingThrowsException() {
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class,() -> state.processingMessage(""));
    }

    @Test
    void nexState_NullPlayerAndMatchState() {
        state.nextState(controller);
        assertEquals(WaitingStatus.class,controller.getControlState().getClass());
    }

    @Test
    void nexState_PlayerStateActive_MatchStateGettingPlayersNum() {
        controller.setPlayerAndMatchState(PlayerState.ACTIVE, MatchState.GETTING_PLAYERS_NUM);
        state.nextState(controller);
        assertEquals(SelectionNumberStatus.class,controller.getControlState().getClass());
    }
}