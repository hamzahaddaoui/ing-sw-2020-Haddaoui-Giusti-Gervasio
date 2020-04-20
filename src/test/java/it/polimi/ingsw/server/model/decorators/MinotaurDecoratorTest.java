package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class MinotaurDecoratorTest {

    Set<Position> positions;
    HashSet<Position> positionsToCheck = new HashSet<>();
    Match match = new Match(1);
    Player player1 = new Player(1,"Vasio",match);
    Player player2 = new Player(2,"Leo",match);
    Player player3 = new Player(3,"Hamza",match);
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
        player1.setCommands(GodCards.Minotaur);
        commands = player1.getCommands();
        player2.setCommands(GodCards.Minotaur);
        match.addPlayer(player1);
        match.addPlayer(player2);
        player1.setWorker(position1);
        player1.setCurrentWorker(position1);
        player2.setWorker(position2);
        player2.setCurrentWorker(position2);
    }

    @AfterEach
    void tearDown() {
        billboard.getDome().replace(position4,false);
        billboard.getTowerHeight().replace(position6,0);
        billboard.getDome().replace(position5,false);
    }


    @Test
    void moveWorker_InOpponentWorkerSpaceWithNextCellFree_ExpectNewPositions() {
        commands.moveWorker(position2,player1);
        Assert.assertEquals(position2,player1.getCurrentWorkerPosition());
        Assert.assertEquals(position4,player2.getCurrentWorkerPosition());
    }

    @Test
    void moveWorker_InOpponentWorkerSpaceWithNextCellOccupied_ExpectSamePositions() {
        billboard.setDome(position4);
        commands.moveWorker(position2,player1);
        Assert.assertEquals(position1,player1.getCurrentWorkerPosition());
        Assert.assertEquals(position2,player2.getCurrentWorkerPosition());
    }

    @Test
    void moveWorker_InFreeCell_ExpectNewPosition() {
        player1.setWorker(position3);
        player1.setCurrentWorker(position3);
        commands.moveWorker(position5,player1);
        Assert.assertEquals(position5,player1.getCurrentWorkerPosition());
    }


    @Test
    void computeAvailableMovements_OpponentWorkerAndNextCellFree() {
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        Assert.assertTrue("Problem with opponent.",positions.contains(position2));
    }

    @Test
    void computeAvailableMovements_OpponentWorkerAndNextCellHasDome() {
        billboard.setDome(position4);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        Assert.assertFalse("Problem with dome.",positions.contains(position2));
    }

    @Test
    void computeAvailableMovements_OpponentWorkerAndNextCellIsNull() {
        player1.setWorker(position7);
        player1.setCurrentWorker(position7);
        player2.setWorker(position3);
        player2.setCurrentWorker(position3);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        Assert.assertFalse("Problem with null cell.",positions.contains(position8));
    }

    @Test
    void computeAvailableMovements_OpponentWorkerAndNextCellHasPlayer() {
        match.setPlayersNum(3);
        match.addPlayer(player3);
        player3.setCommands(GodCards.Artemis);
        player3.setWorker(position4);
        player3.setCurrentWorker(position4);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        Assert.assertFalse("Problem with opponent player.",positions.contains(position2));
    }

    @Test
    void computeAvailableMovements_WithoutOpponentWorker() {
        player1.setWorker(position3);
        player1.setCurrentWorker(position3);
        positionsToCheck.add(position5);
        positionsToCheck.add(position6);
        positionsToCheck.add(position7);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        Assert.assertTrue("Problem with opponent.",positionsToCheck.containsAll(positions) &&
                positions.containsAll(positionsToCheck));
    }

    @Test
    void computeAvailableMovements_WithMoveUpUnsetAndDome() {
        player1.setWorker(position3);
        player1.setCurrentWorker(position3);
        billboard.setDome(position5);
        match.setMoveUpActive(false);
        billboard.incrementTowerHeight(position6);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        Assert.assertTrue("Problem with move up and dome.",!positions.contains(position5) &&
                !positions.contains(position6));
    }

    @Test
    void computeAvailableMovements_WithMoveUpSet() {
        player1.setWorker(position3);
        player1.setCurrentWorker(position3);
        match.setMoveUpActive(true);
        billboard.incrementTowerHeight(position6);
        positions = commands.computeAvailableMovements(player1,player1.getCurrentWorker());
        Assert.assertTrue("Problem with move up and dome.",positions.contains(position6));
    }

}