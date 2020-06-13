package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utilities.TurnState.*;
import static org.junit.jupiter.api.Assertions.*;

class PrometheusDecoratorTest {

    Set<Position> positions;
    Set<GodCards> testingCards = new HashSet<GodCards>();
    Set<Position> testingPosition = new HashSet<Position>();
    HashSet<Position> positionsToCheck = new HashSet<>();
    Player player = new Player(1,"Vasio");
    Match match = new Match(1,player);
    Billboard billboard;
    Commands commands;
    Position position1 = new Position(2,2);
    Position position2 = new Position(3,1);
    Position position3 = new Position(2,1);
    Position position4 = new Position(2,3);

    @BeforeEach
    void setUp() {
        billboard = match.getBillboard();
        match.setPlayersNum(2);

        testingCards.add(GodCards.Prometheus);
        testingCards.add(GodCards.Apollo);
        match.setCards(testingCards);
        player.setCommands(GodCards.Prometheus);
        commands=player.getCommands();

        match.nextState();
        match.nextState();
        match.nextState();
        match.nextState();
        match.nextState();

        player.setWorker(position1);
        player.setPlayerState();
        player.setCurrentWorker(position1);

        billboard.incrementTowerHeight(position2);

        //player.setTurnState(IDLE);

    }

    @AfterEach
    void tearDown() {
        billboard.getCells().get(position2).setTowerHeight(0);
        billboard.getCells().get(position3).setTowerHeight(0);
    }

    @Test
    void turn_NotSpecialFunction() {
        assertEquals(MOVE,player.getTurnState());
        commands.moveWorker(position2,player);
        commands.nextState(player);
        assertEquals(BUILD,player.getTurnState());
        commands.build(position3,player);
        assertEquals(1, billboard.getTowerHeight(position3), "Build problem.");
        commands.nextState(player);
        assertEquals(IDLE,player.getTurnState());
        assertTrue(player.hasFinished(), "Next state problem.");
    }

    @Test
    void turn_SpecialFunction() {
        player.setUnsetSpecialFunction(true);
        assertEquals(BUILD,player.getTurnState());
        commands.build(position3,player);
        assertEquals(1, billboard.getTowerHeight(position3), "Build problem.");
        commands.nextState(player);
        assertEquals(MOVE,player.getTurnState());
        commands.moveWorker(position4,player);
        commands.nextState(player);
        assertEquals(BUILD,player.getTurnState());
        commands.build(position3,player);
        assertEquals( 2, billboard.getTowerHeight(position3), "Build problem.");
        commands.nextState(player);
        assertEquals(IDLE,player.getTurnState());
        assertTrue(player.hasFinished(), "Next state problem.");
    }

    @Test
    void turn_SpecialFunction_NoBuildBeforeMovingPermitted() {
        testingPosition.add(new Position(2,3));
        billboard.incrementTowerHeight(new Position(2,3));
        player.getCurrentWorker().setAvailableCells(MOVE,testingPosition);
        player.getCurrentWorker().setAvailableCells(BUILD,testingPosition);
        commands.notifySpecialFunction(player);
        assertTrue(player.isSpecialFunctionAvailable().get(position1));
    }

    @Test
    void turn_SpecialFunction_BuildBeforeMovingPermitted() {
        testingPosition.add(new Position(2,3));
        player.getCurrentWorker().setAvailableCells(BUILD,testingPosition);
        player.setPlayerState();
        assertTrue(player.isSpecialFunctionAvailable().get(position1));
    }

    @Test
    void build_BuildDomeCase() {
        billboard.incrementTowerHeight(position3);
        billboard.incrementTowerHeight(position3);
        billboard.incrementTowerHeight(position3);
        commands.build(position3,player);
        assertTrue(billboard.getDome(position3), "Build problem");
    }

    @Test
    void computeAvailableMovements_SpecialFunction() {
        player.setUnsetSpecialFunction(true);
        position2.setZ(1);
        positions = commands.computeAvailableMovements(player,player.getCurrentWorker());
        assertFalse(positions.contains(position2), "Wrong Special Function.");
    }

    @Test
    void computeAvailableMovements_NotSpecialFunctionWithDoubleSetUnset() {
        player.setUnsetSpecialFunction(true);
        player.setUnsetSpecialFunction(false);
        positions = commands.computeAvailableMovements(player,player.getCurrentWorker());
        assertTrue(positions.contains(position2), "Wrong Special Function.");
    }
}