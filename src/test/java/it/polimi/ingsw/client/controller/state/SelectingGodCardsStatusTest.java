package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;

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
    Player player = View.getPlayer();
    GameBoard gameBoard = View.getGameBoard();
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

        gameBoard.setMatchCards(new HashSet<>(godCards));
        gameBoard.setColoredGodCard(gameBoard.getMatchCards().get(0));
        player.setPlayerNumber(2);

        System.out.println("\n"+gameBoard.getMatchCards()+"\n");
    }

    @Test
    void processingMessageD() {
        commandCharacter = inputD;
        gameBoard.setColoredGodCard(gameBoard.getMatchCards().get(gameBoard.getMatchCards().size() -  1));
        System.out.println(gameBoard.getColoredGodCard());
        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(gameBoard.getColoredGodCard(),gameBoard.getMatchCards().get(0));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(gameBoard.getColoredGodCard(),gameBoard.getMatchCards().get(1));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(gameBoard.getColoredGodCard(),gameBoard.getMatchCards().get(2));
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageA() {
        commandCharacter = inputA;
        System.out.println(gameBoard.getColoredGodCard());
        gameBoard.setColoredGodCard(gameBoard.getMatchCards().get(0));
        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(gameBoard.getColoredGodCard(),gameBoard.getMatchCards().get(gameBoard.getMatchCards().size() -  1));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(gameBoard.getColoredGodCard(),gameBoard.getMatchCards().get(gameBoard.getMatchCards().size() -  2));
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(gameBoard.getColoredGodCard(),gameBoard.getMatchCards().get(gameBoard.getMatchCards().size() -  3));
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageEnterOrE() {
        commandCharacter = inputEnter;
        assertTrue( gameBoard.getColoredGodCard() != null);
        assertTrue( gameBoard.getMatchCards() != null);
        assertTrue( gameBoard.getColoredGodCard() != null);
        //System.out.println(view.getGodCards().size());

        ctrlStatus.processingMessage(commandCharacter);
        //System.out.println(view.getGodCards().size());

        assertTrue(gameBoard.getSelectedGodCards().size() == 1);
        assertTrue( gameBoard.getMatchCards().size() == 8);
        assertEquals(gameBoard.getColoredGodCard(),gameBoard.getMatchCards().get(0));

        commandCharacter = inputE;
        gameBoard.setColoredGodCard(gameBoard.getSelectedGodCards().stream().findAny().get());
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( gameBoard.getMatchCards().size() == 9);
        assertTrue( gameBoard.getColoredGodCard() != null);

        assertThrows( NoSuchElementException.class , () -> gameBoard.setColoredGodCard(gameBoard.getSelectedGodCards().stream().findAny().get()));

        commandCharacter = inputEnter;
        gameBoard.setColoredGodCard(gameBoard.getMatchCards().stream().findAny().get());
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getSelectedGodCards().size() == 1);
        assertTrue( gameBoard.getMatchCards().size() == 8);

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getSelectedGodCards().size() == 2);
        assertTrue( gameBoard.getMatchCards().size() == 7);
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


}