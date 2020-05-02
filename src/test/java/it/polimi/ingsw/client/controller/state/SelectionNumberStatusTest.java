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
    Player player = view.getPlayer();
    GameBoard gameBoard = view.getGameBoard();

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
        player.setPlayersNum(new ArrayList<>());
        player.getPlayersNum().add(2);
        player.getPlayersNum().add(3);
        player.setPlayerNumber(player.getPlayersNum().get(0));

        System.out.println("\n"+player.getPlayersNum()+"\n");
    }

    @Test
    void processingMessageA() {
        commandCharacter = inputA;

        assertEquals(player.getPlayerNumber(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),3);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),2);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),3);
    }

    @Test
    void processingMessageD() {
        commandCharacter = inputD;

        assertEquals(player.getPlayerNumber(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),3);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),3);
        assertEquals(controller.isMessageReady(),false);
    }

    @Test
    void processingMessageW() {
        commandCharacter = inputW;

        assertEquals(player.getPlayerNumber(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageS() {
        commandCharacter = inputS;

        assertEquals(player.getPlayerNumber(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageE() {
        commandCharacter = inputE;

        assertEquals(player.getPlayerNumber(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageQ() {
        commandCharacter = inputQ;

        assertEquals(player.getPlayerNumber(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageF() {
        commandCharacter = inputF;

        assertEquals(player.getPlayerNumber(),2);
        assertEquals(controller.isMessageReady(),false);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),2);
        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageEnter() {
        commandCharacter = inputD;

        assertEquals(player.getPlayerNumber(),2);

        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),3);

        commandCharacter = inputEnter;
        ctrlStatus.processingMessage(commandCharacter);

        assertEquals(player.getPlayerNumber(),3);
        assertTrue(ctrlStatus.processingMessage(commandCharacter));
    }

}