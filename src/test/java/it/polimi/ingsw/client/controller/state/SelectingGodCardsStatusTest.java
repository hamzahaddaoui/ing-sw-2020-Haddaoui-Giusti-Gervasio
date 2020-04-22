package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SelectingGodCardsStatusTest {

    InsertCharacter inputA = InsertCharacter.A;
    InsertCharacter inputD = InsertCharacter.D;
    InsertCharacter inputW = InsertCharacter.W;
    InsertCharacter inputE = InsertCharacter.E;
    InsertCharacter inputF = InsertCharacter.F;
    InsertCharacter inputQ = InsertCharacter.Q;
    InsertCharacter inputS = InsertCharacter.S;
    InsertCharacter inputEnter = InsertCharacter.ENTER;
    CommandCharacter commandCharacter;
    View view;
    Controller controller = new Controller();
    ControlState ctrlStatus = new SelectionNumberStatus();
    ArrayList<String> godCards ;

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

        controller = new Controller();
        ctrlStatus = new SelectionNumberStatus();
        controller.setState(ctrlStatus);
        View.setGodCards(godCards);
        View.setColoredGodCard(View.getGodCards().get(0));
    }


    @Test
    void processingMessageA() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputA.apply();
        
        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),3);
        //assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void nextState() {
    }
}