package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SelectionNumberStatusTest {

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
    }

    @Test
    void processingMessageA() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputA.apply();

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),3);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(controller.isMessageReady(),false);
        assertEquals(View.getPlayersNum(),2);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(controller.isMessageReady(),false);
        assertEquals(View.getPlayersNum(),3);
    }

    @Test
    void processingMessageD() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputD.apply();

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),3);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),3);
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageW() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputW.apply();

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageS() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputS.apply();

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageE() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputE.apply();

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageQ() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputQ.apply();

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageF() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputF.apply();

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageEnter() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputEnter.apply();

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),true);
    }

    @Test
    void nextState() {
        controller.setPlayerState(PlayerState.ACTIVE);
        controller.setMatchState(MatchState.SELECTING_GOD_CARDS);
        controller.nextState();

        assertEquals(controller.isMessageReady(),true);
        assertEquals(View.getColoredGodCard(), View.getGodCards().get(0));
    }
}