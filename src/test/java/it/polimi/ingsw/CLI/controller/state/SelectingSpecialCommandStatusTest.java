package it.polimi.ingsw.CLI.controller.state;

import it.polimi.ingsw.client.CLI.controller.state.ControlState;
import it.polimi.ingsw.client.CLI.controller.state.SelectingSpecialCommand;
import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.client.CLI.controller.Controller;
import it.polimi.ingsw.utilities.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SelectingSpecialCommandStatusTest {

    Controller controller = new Controller();
    View view = new View();
    DataBase dataBase = new DataBase();
    ControlState state;
    String input;
    Set<String> testingCards = new HashSet<String>();
    Map<Integer,String> players = new HashMap<>();

    @BeforeEach
    void setUp() {
        testingCards.add("APOLLO");
        testingCards.add("ARTHEMIS");
        state = new SelectingSpecialCommand();
        DataBase.setControlState(state);
        DataBase.setMatchPlayers(players);
    }

    @Test
    void computeInput(){
        input = "";

        assertThrows(IllegalArgumentException.class, () -> state.computeInput(input) );

        DataBase.setSelectedGodCards(testingCards);
        input = "apolso";
        state.computeInput(input);

        assertFalse(DataBase.isMessageReady());
        assertTrue(DataBase.isActiveInput());

        input = "apollo";
        state.computeInput(input);

        assertTrue(DataBase.isMessageReady());
        assertTrue(DataBase.getPlayerState() == PlayerState.IDLE);

    }

}