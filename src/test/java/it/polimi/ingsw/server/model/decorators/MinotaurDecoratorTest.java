package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utilities.TurnState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class MinotaurDecoratorTest {

    Set<GodCards> testingCards = new HashSet<GodCards>();
    Set<Position> positions;
    HashSet<Position> positionsToCheck = new HashSet<>();
    Player player1 = new Player(1,"Vasio");
    Match match = new Match(1, player1);
    Player player2 = new Player(2,"Leo");
    Player player3 = new Player(3,"Hamza");
    Commands commands;
    Billboard billboard;
    Position position1 = new Position(1,2);
    Position position2 = new Position(2,2);
    Position position3 = new Position(4,4);
    Position position4 = new Position(3,2);
    Position position5 = new Position(4,3);
    Position position6 = new Position(3,4);
    Position position7 = new Position(3,3);
    Position position8 = new Position(5,5);

    @BeforeEach
    void setUp() {
        match.setPlayersNum(2);
        billboard = match.getBillboard();
        match.addPlayer(player2);

        testingCards.add(GodCards.Minotaur);
        testingCards.add(GodCards.Apollo);
        match.setCards(testingCards);

        player1.setCommands(GodCards.Minotaur);
        commands = player1.getCommands();
        player2.setCommands(GodCards.Apollo);

        player1.setWorker(position1);
        player2.setWorker(position2);

    }

    @AfterEach
    void tearDown() {
        billboard.getCells().get(position4).setDome(false);
        billboard.getCells().get(position6).setTowerHeight(0);
        billboard.getCells().get(position5).setDome(false);
    }


    @Test
    void moveWorker_InOpponentWorkerSpaceWithNextCellFree_ExpectNewPositions() {
        player1.setCurrentWorker(position1);
        player2.setCurrentWorker(position2);
        commands.moveWorker(position2,player1);
        assertEquals(position2,player1.getCurrentWorkerPosition());
        assertEquals(position4,player2.getCurrentWorkerPosition());
    }

    @Test
    void moveWorker_InOpponentWorkerSpaceWithNextCellOccupied_ExpectSamePositions() {
        player1.setCurrentWorker(position1);
        player2.setCurrentWorker(position2);
        billboard.setDome(position4);
        commands.moveWorker(position2,player1);
        assertEquals(position1,player1.getCurrentWorkerPosition());
        assertEquals(position2,player2.getCurrentWorkerPosition());
    }

    @Test
    void moveWorker_InFreeCell_ExpectNewPosition() {
        player1.setTurnState(TurnState.IDLE);
        player1.setWorker(position3);
        player1.setCurrentWorker(position3);
        commands.moveWorker(position5,player1);
        assertEquals(position5,player1.getCurrentWorkerPosition());
    }


    @Test
    void computeAvailableMovements_OpponentWorkerAndNextCellFree() {
        player1.setCurrentWorker(position1);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        assertTrue(positions.contains(position2), "Problem with opponent.");
    }

    @Test
    void computeAvailableMovements_OpponentWorkerAndNextCellHasDome() {
        player1.setCurrentWorker(position1);
        billboard.setDome(position4);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        assertFalse(positions.contains(position2), "Problem with dome.");
    }

    @Test
    void computeAvailableMovements_OpponentWorkerAndNextCellIsNull() {
        player1.setTurnState(TurnState.IDLE);
        player1.setWorker(position7);
        player1.setCurrentWorker(position7);
        player2.setWorker(position3);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        assertFalse(positions.contains(position8), "Problem with null cell.");
    }

    @Test
    void computeAvailableMovements_OpponentWorkerAndNextCellHasPlayer() {
        player1.setCurrentWorker(position1);
        match.setPlayersNum(3);
        testingCards.add(GodCards.Minotaur);
        testingCards.add(GodCards.Apollo);
        testingCards.add(GodCards.Artemis);
        match.setCards(testingCards);
        match.addPlayer(player3);
        player3.setCommands(GodCards.Artemis);
        player3.setWorker(position4);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        assertFalse(positions.contains(position2), "Problem with opponent player.");
    }

    @Test
    void computeAvailableMovements_WithoutOpponentWorker() {
        player1.setTurnState(TurnState.IDLE);
        player1.setWorker(position3);
        player1.setCurrentWorker(position3);
        positionsToCheck.add(position5);
        positionsToCheck.add(position6);
        positionsToCheck.add(position7);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        assertTrue(positionsToCheck.containsAll(positions) &&
                        positions.containsAll(positionsToCheck), "Problem with opponent.");
    }

    @Test
    void computeAvailableMovements_WithMoveUpUnsetAndDome() {
        player1.setTurnState(TurnState.IDLE);
        player1.setWorker(position3);
        player1.setCurrentWorker(position3);
        billboard.setDome(position5);
        match.setMoveUpActive(false);
        billboard.incrementTowerHeight(position6);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        assertTrue(!positions.contains(position5) &&
                        !positions.contains(position6), "Problem with move up and dome.");
    }

    @Test
    void computeAvailableMovements_WithMoveUpSet() {
        player1.setTurnState(TurnState.IDLE);
        player1.setWorker(position3);
        player1.setCurrentWorker(position3);
        match.setMoveUpActive(true);
        billboard.incrementTowerHeight(position6);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        assertTrue(positions.contains(position6), "Problem with move up and dome.");
    }

}