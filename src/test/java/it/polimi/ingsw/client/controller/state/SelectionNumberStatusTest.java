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
import static org.junit.jupiter.api.Assertions.assertTrue;


class SelectionNumberStatusTest {

    InsertCharacter inputA = InsertCharacter.A;
    InsertCharacter inputD = InsertCharacter.D;
    InsertCharacter inputW = InsertCharacter.W;
    InsertCharacter inputE = InsertCharacter.E;
    InsertCharacter inputF = InsertCharacter.F;
    InsertCharacter inputQ = InsertCharacter.Q;
    InsertCharacter inputS = InsertCharacter.S;
    InsertCharacter inputEnter = InsertCharacter.ENTER;
    InsertCharacter commandCharacter;
    View view = new View();
    Controller controller = new Controller();
    ControlState ctrlStatus = new SelectionNumberStatus();
    ArrayList<String> godCards = new ArrayList<String>();

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

        controller.setState(ctrlStatus);
        view.setGodCards(godCards);
        view.setColoredPlayersNum(new ArrayList<>());
        view.getColoredPlayersNum().add(2);
        view.getColoredPlayersNum().add(3);
        view.setPlayersNum(View.getColoredPlayersNum().get(0));
    }

    @Test
    void processingMessageA() {
        commandCharacter = inputA;

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),3);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),3);
    }

    @Test
    void processingMessageD() {
        commandCharacter = inputD;

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
        commandCharacter = inputW;

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageS() {
        commandCharacter = inputS;

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageE() {
        commandCharacter = inputE;

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageQ() {
        commandCharacter = inputQ;

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageF() {
        commandCharacter = inputF;

        assertEquals(view.getPlayersNum(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageEnter() {
        commandCharacter = inputEnter;

        assertEquals(view.getPlayersNum(),2);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getPlayersNum(),2);
        assertTrue(ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void nextState() {
        controller.setPlayerState(PlayerState.ACTIVE);
        controller.setMatchState(MatchState.SELECTING_GOD_CARDS);
        controller.nextState();

        assertEquals(View.getColoredGodCard(), View.getGodCards().get(0));
        assertEquals( controller.getControlState().getClass(), new SelectingGodCardsStatus().getClass() );

        controller.setPlayerState(PlayerState.IDLE);
        controller.setMatchState(MatchState.SELECTING_GOD_CARDS);
        controller.nextState();

        assertEquals( controller.getControlState().getClass() , new WaitingStatus().getClass()  );
    }
}