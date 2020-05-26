package it.polimi.ingsw.CLI.controller.state;

import it.polimi.ingsw.client.CLI.controller.Controller;
import it.polimi.ingsw.client.CLI.controller.state.ControlState;
import it.polimi.ingsw.client.CLI.controller.state.SelectingGodCards;
import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SelectingGodCardsStatusTest {

    View view = new View();
    Controller controller = new Controller();
    DataBase dataBase = new DataBase();
    ControlState ctrlStatus = new SelectingGodCards();
    ArrayList<String> godCards = new ArrayList<>() ;
    String input;

    @BeforeEach
    void setUp(){
        godCards.add("APOLLO");
        godCards.add("ARTEMIS");
        godCards.add("ATHENA");
        godCards.add("ATLAS");
        godCards.add("DEMETER");
        godCards.add("HEPHAESTUS");
        godCards.add("MINOTAUR");
        godCards.add("PAN");
        godCards.add("PROMETHEUS");

        DataBase.setControlState(ctrlStatus);
        DataBase.setPlayerNumber(3);
        DataBase.setMatchPlayers(new HashMap<Integer,String>(3));

        DataBase.setMatchCards(new HashSet<>(godCards));
        //DataBase.setGodCard(DataBase.getMatchCards().get(0));

    }

    @Test
    void computeInput(){
        //DataBase.setSelectedGodCards(testingCards);
        input = "apollo";
        ctrlStatus.computeInput(input);

        assertFalse(DataBase.getMatchCards().contains("APOLLO"));
        assertEquals(8, DataBase.getMatchCards().size());
        assertEquals(1, DataBase.getSelectedGodCards().size());
        assertFalse(DataBase.isMessageReady());
        assertTrue(DataBase.isActiveInput());

        input = "apollo";
        ctrlStatus.computeInput(input);

        assertFalse(DataBase.getMatchCards().contains("APOLLO"));
        assertEquals(8, DataBase.getMatchCards().size());
        assertEquals(1, DataBase.getSelectedGodCards().size());
        assertFalse(DataBase.isMessageReady());
        assertTrue(DataBase.isActiveInput());

        input = "artemis";
        ctrlStatus.computeInput(input);

        assertFalse(DataBase.getMatchCards().contains("APOLLO"));
        assertFalse(DataBase.getMatchCards().contains("ARTEMIS"));
        assertEquals(7, DataBase.getMatchCards().size());
        assertEquals(2, DataBase.getSelectedGodCards().size());
        assertFalse(DataBase.isMessageReady());

        input = "pan";
        ctrlStatus.computeInput(input);

        assertFalse(DataBase.getMatchCards().contains("APOLLO"));
        assertFalse(DataBase.getMatchCards().contains("ARTEMIS"));
        assertFalse(DataBase.getMatchCards().contains("PAN"));
        assertEquals(6, DataBase.getMatchCards().size());
        assertEquals(3, DataBase.getSelectedGodCards().size());
        assertTrue(DataBase.isMessageReady());
    }
}