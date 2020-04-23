package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.ACommand;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SelectingSpecialCommandStatusTest {

    Controller controller = new Controller();
    ControlState state;
    InsertCharacter viewObject;
    Set<String> testingCards = new HashSet<String>();

    @BeforeEach
    void setUp() {
        controller.setState(new SelectingSpecialCommandStatus());
        state = controller.getControlState();

        testingCards.add("APOLLO");
        testingCards.add("ARTHEMIS");

        View.setGodCards(testingCards);
        View.setColoredGodCard("APOLLO");
        View.setPlayersNum(2);
    }

    @AfterEach
    void tearDown() {}

    @Test
    void processingMessage_ACommand() {
        viewObject = InsertCharacter.A;
        state.processingMessage(viewObject);
        assertEquals("ARTHEMIS",View.getColoredGodCard());
    }

}