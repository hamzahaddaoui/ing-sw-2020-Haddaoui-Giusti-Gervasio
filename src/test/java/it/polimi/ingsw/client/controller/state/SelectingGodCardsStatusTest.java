package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
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
    View view = new View();
    Controller controller = new Controller();
    ControlState ctrlStatus = new SelectingGodCardsStatus();
    ArrayList<String> godCards = new ArrayList<>() ;

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
        View.setGodCards(godCards);
        View.setColoredGodCard(View.getGodCards().get(0));
    }

    @Test
    void processingMessageA() {

        commandCharacter = inputA.apply();

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(view.getGodCards().size()));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(view.getGodCards().size() - 1));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(view.getGodCards().size() - 2));
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageD() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputD.apply();

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),View.getGodCards().get(0));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),View.getGodCards().get(View.getGodCards().size() + 1));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),View.getGodCards().get(View.getGodCards().size() + 2));
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageEnterOrE() {
        view = new View();
        view.viewSetUp();
        commandCharacter = inputEnter.apply();

        assertEquals(controller.isMessageReady(),false);
        assertTrue( View.getColoredGodCard() != null);

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getSelectedGodCards().size() == 1);
        assertTrue( View.getGodCards().size() == 6);
        assertEquals(view.getColoredGodCard(),View.getGodCards().get(0));
        assertEquals(controller.isMessageReady(),false);

        commandCharacter = inputE.apply();
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getSelectedGodCards().size() == 0);
        assertTrue( View.getGodCards().size() == 7);
        assertEquals(controller.isMessageReady(),false);
        assertTrue( View.getColoredGodCard() != null);

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getSelectedGodCards().size() == 0);
        assertEquals(controller.isMessageReady(),false);
        assertTrue( View.getGodCards().size() == 7);
        assertTrue( View.getColoredGodCard() != null);

        commandCharacter = inputEnter.apply();
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getSelectedGodCards().size() == 1);
        assertTrue( View.getGodCards().size() == 6);
        assertEquals(view.getColoredGodCard(),View.getGodCards().get(0));
        assertEquals(controller.isMessageReady(),false);

        commandCharacter = inputEnter.apply();
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getSelectedGodCards().size() == 2);
        assertTrue( View.getGodCards().size() == 5);
        assertEquals(controller.isMessageReady(),true);
    }

    @Test
    void processingMessageW() {
        commandCharacter = inputW.apply();

        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageS() {
        commandCharacter = inputS.apply();

        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageF() {
        commandCharacter = inputF.apply();

        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageQ() {
        commandCharacter = inputQ.apply();

        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void nextState() {
        controller.setPlayerState(PlayerState.ACTIVE);
        controller.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);

        controller.nextState();

        assertEquals( controller.getControlState().getClass() , new SelectingSpecialCommandStatus().getClass() );

        controller.setPlayerState(PlayerState.IDLE);
        controller.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);
        controller.nextState();

        assertEquals( controller.getControlState().getClass(), new WaitingStatus().getClass() );

    }

}