package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SelectingSpecialCommandStatusTest {

    Controller controller = new Controller();
    View view = View.constructor();
    Player player = View.getPlayer();
    GameBoard gameBoard = View.getGameBoard();
    ControlState state;
    InsertCharacter viewObject;
    Set<String> testingCards = new HashSet<String>();

    @BeforeEach
    void setUp() {
        controller.setState(new SelectingSpecialCommandStatus());
        state = controller.getControlState();

        testingCards.add("APOLLO");
        testingCards.add("ARTHEMIS");

        gameBoard.setSelectedGodCards(testingCards);
        gameBoard.setColoredGodCard("APOLLO");
        player.setPlayersNum(2);
    }

    @AfterEach
    void tearDown() {gameBoard.setColoredGodCard("APOLLO");}

    @Test
    void processingMessage_String() {
        assertThrows(IllegalArgumentException.class,() -> state.processingMessage("Vasio"));
    }

    @Test
    void processingMessage_ACommand() {
        viewObject = InsertCharacter.A;
        state.processingMessage(viewObject);
        assertEquals("ARTHEMIS",gameBoard.getColoredGodCard());
    }

    @Test
    void processingMessage_DCommand() {
        viewObject = InsertCharacter.D;
        state.processingMessage(viewObject);
        assertEquals("ARTHEMIS",gameBoard.getColoredGodCard());
        state.processingMessage(viewObject);
        assertEquals("APOLLO",gameBoard.getColoredGodCard());
    }

    @Test
    void processingMessage_EnterCommand() {
        viewObject = InsertCharacter.ENTER;
        assertTrue(state.processingMessage(viewObject));
        assertEquals("APOLLO",Controller.getMessage().getGodCard());
    }

    @Test
    void nexState_MatchPlacingWorkers_PlayerNotActive() {
        controller.setPlayerAndMatchState(PlayerState.IDLE, MatchState.PLACING_WORKERS);
        state.nextState(controller);
        assertEquals(WaitingStatus.class,controller.getControlState().getClass());
    }

    @Test
    void nexState_MatchPlacingWorkers_PlayerActive() {
        controller.setPlayerAndMatchState(PlayerState.ACTIVE, MatchState.PLACING_WORKERS);
        state.nextState(controller);
        assertEquals(PlacingWorkersStatus.class,controller.getControlState().getClass());
    }
}