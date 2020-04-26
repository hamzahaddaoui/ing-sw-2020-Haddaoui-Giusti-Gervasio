package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static java.lang.StrictMath.abs;
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
    InsertCharacter commandCharacter;

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

        view.setGodCards(godCards);
        view.setColoredGodCard(View.getGodCards().get(0));
        view.setSelectedGodCards(new ArrayList<>());
        view.setPlayersNum(2);
    }

    @Test
    void processingMessageA() {
        commandCharacter = inputA;
        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(view.getGodCards().size() -  1));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(view.getGodCards().size() - 2));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(view.getGodCards().size() - 3));
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageD() {
        commandCharacter = inputD;

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(1));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(2));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(view.getColoredGodCard(),view.getGodCards().get(3));
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageEnterOrE() {
        commandCharacter = inputEnter;
        assertTrue( view.getColoredGodCard() != null);
        assertTrue( view.getGodCards() != null);
        assertTrue( view.getColoredGodCard() != null);
        //System.out.println(view.getGodCards().size());

        ctrlStatus.processingMessage(commandCharacter);
        //System.out.println(view.getGodCards().size());

        assertTrue(view.getSelectedGodCards().size() == 1);
        assertTrue( view.getGodCards().size() == 8);
        assertEquals(view.getColoredGodCard(),View.getGodCards().get(0));

        commandCharacter = inputE;
        view.setColoredGodCard(View.getSelectedGodCards().stream().findAny().get());
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( view.getGodCards().size() == 9);
        assertTrue( view.getColoredGodCard() != null);

        assertThrows( NoSuchElementException.class , () -> view.setColoredGodCard(View.getSelectedGodCards().stream().findAny().get()));

        commandCharacter = inputEnter;
        view.setColoredGodCard(View.getGodCards().stream().findAny().get());
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(view.getSelectedGodCards().size() == 1);
        assertTrue( view.getGodCards().size() == 8);

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(view.getSelectedGodCards().size() == 2);
        assertTrue( view.getGodCards().size() == 7);
    }

    @Test
    void processingMessageW() {
        commandCharacter = inputW;

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageS() {
        commandCharacter = inputS;

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageF() {
        commandCharacter = inputF;

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageQ() {
        commandCharacter = inputQ;

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void nextState() {

        controller.setPlayerState(PlayerState.ACTIVE);
        controller.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);

        ctrlStatus.nextState(controller);

        assertEquals( controller.getControlState().getClass() , new SelectingSpecialCommandStatus().getClass() );

        controller.setPlayerState(PlayerState.IDLE);
        controller.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);
        ctrlStatus.nextState(controller);

        assertEquals( controller.getControlState().getClass(), new WaitingStatus().getClass() );

        controller.setPlayerState(null);

        assertThrows( IllegalArgumentException.class , () -> ctrlStatus.nextState(controller));

        controller.setPlayerState(PlayerState.ACTIVE);
        controller.setMatchState(null);

        assertThrows( IllegalArgumentException.class , () -> ctrlStatus.nextState(controller));
    }

}