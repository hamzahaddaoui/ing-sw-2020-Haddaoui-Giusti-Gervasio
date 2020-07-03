package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utilities.TurnState.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vasio1298
 */

class PrometheusDecoratorTest {

    Player p1 = new Player(1,"vasio");
    Match match = new Match(1,p1);
    Player p2 = new Player(2,"dario");
    Commands commands;
    Billboard billboard;
    Set<GodCards> testingCards = new HashSet<>();
    Set<Position> testingPositions = new HashSet<>();
    Set<Position> test1 = new HashSet<>();
    Set<Position> test2 = new HashSet<>();

    @BeforeEach
    void setUp() {
        match.setPlayersNum(2);
        billboard = match.getBillboard();
        match.addPlayer(p2);

        testingCards.add(GodCards.Prometheus);
        testingCards.add(GodCards.Pan);
        match.setCards(testingCards);

        p1.setCommands(GodCards.Prometheus);
        commands = p1.getCommands();
        p2.setCommands(GodCards.Pan);

        match.nextState();
        match.nextState();
        match.nextState();
        match.nextState();
        match.nextState();

        p1.setWorker(new Position(0,0));
        p1.setWorker(new Position(4,3));
        p2.setWorker(new Position(1,1));
        p2.setWorker(new Position(0,1));
    }

    @Test
    void playerTurn_SpecialFunctionAvailableForOnlyOne() {
        Worker curr;
        p1.setTurnState(IDLE);
        commands.nextState(p1);
        assertFalse(p1.isSpecialFunctionAvailable().get(new Position(0,0)));
        assertTrue(p1.isSpecialFunctionAvailable().get(new Position(4,3)));
        p1.setUnsetSpecialFunction(false);
        billboard.incrementTowerHeight(new Position(3,3));
        billboard.incrementTowerHeight(new Position(4,2));
        billboard.incrementTowerHeight(new Position(3,2));
        billboard.incrementTowerHeight(new Position(3,4));
        p1.setUnsetSpecialFunction(true);
        p1.setCurrentWorker(new Position(4,3));
        curr = p1.getCurrentWorker();
        commands.computeAvailableBuildings(p1,curr);
        commands.build(new Position(3,3),p1);
        commands.nextState(p1);
        commands.computeAvailableMovements(p1,curr);
        assertEquals(2, billboard.getTowerHeight(new Position(3, 3)));
        assertEquals(0, billboard.getTowerHeight(new Position(4, 3)));
        assertTrue(p1.hasSpecialFunction());
        commands.moveWorker(new Position(4,4),p1);
        commands.nextState(p1);
        commands.computeAvailableBuildings(p1,curr);
        commands.build(new Position(3,3),p1);
        commands.nextState(p1);
        assertEquals(p1.getTurnState(),IDLE);
    }
}