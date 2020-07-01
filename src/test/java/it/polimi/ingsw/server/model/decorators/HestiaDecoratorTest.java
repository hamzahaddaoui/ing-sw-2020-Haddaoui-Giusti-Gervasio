package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utilities.TurnState.*;
import static org.junit.jupiter.api.Assertions.*;

class HestiaDecoratorTest {

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

        testingCards.add(GodCards.Hestia);
        testingCards.add(GodCards.Hephaestus);
        match.setCards(testingCards);

        p1.setCommands(GodCards.Hestia);
        commands = p1.getCommands();
        p2.setCommands(GodCards.Hephaestus);

        match.nextState();
        match.nextState();
        match.nextState();
        match.nextState();
        match.nextState();

        p1.setWorker(new Position(1,1));
        p1.setWorker(new Position(4,4));
        p2.setWorker(new Position(0,0));
        p2.setWorker(new Position(4,3));

        p1.setCurrentWorker(new Position(1,1));

        setCells();
        p1.getCurrentWorker().setAvailableCells(BUILD,testingPositions);
    }

    void setCells() {
        testingPositions.add(new Position(1,1));
        testingPositions.add(new Position(1,2));
    }

    Set<Position> positionSet1() {
        test1.add(new Position(1,0));
        test1.add(new Position(1,1));
        test1.add(new Position(1,2));
        test1.add(new Position(0,2));

        return test1;
    }

    Set<Position> positionSet2() {
        test2.add(new Position(1,2));

        return test2;
    }

    @Test
    void turn_DoubleBuild() {
        assertEquals(MOVE,p1.getTurnState());
        commands.moveWorker(new Position(0,1),p1);
        commands.nextState(p1);
        assertEquals(BUILD,p1.getTurnState());
        billboard.incrementTowerHeight(new Position(1,1));
        billboard.incrementTowerHeight(new Position(1,1));
        billboard.incrementTowerHeight(new Position(1,1));
        commands.computeAvailableBuildings(p1,p1.getCurrentWorker());
        assertTrue(commands.computeAvailableBuildings(p1,p1.getCurrentWorker()).containsAll(positionSet1()));
        assertTrue(positionSet1().containsAll(commands.computeAvailableBuildings(p1,p1.getCurrentWorker())));
        commands.build(new Position(1,1),p1);
        commands.nextState(p1);
        assertTrue(p1.isTerminateTurnAvailable());
        assertEquals(BUILD,p1.getTurnState());
        commands.computeAvailableBuildings(p1,p1.getCurrentWorker());
        assertTrue(commands.computeAvailableBuildings(p1,p1.getCurrentWorker()).containsAll(positionSet2()));
        assertTrue(positionSet2().containsAll(commands.computeAvailableBuildings(p1,p1.getCurrentWorker())));
        commands.build(new Position(1,2),p1);
        commands.nextState(p1);
        assertEquals(IDLE,p1.getTurnState());
    }

    @Test
    void turn_SingleBuild_TerminateTurnActive() {
        assertEquals(MOVE,p1.getTurnState());
        commands.moveWorker(new Position(0,1),p1);
        commands.nextState(p1);
        assertEquals(BUILD,p1.getTurnState());
        commands.build(new Position(1,1),p1);
        commands.nextState(p1);
        p1.setHasFinished();
        assertEquals(IDLE,p1.getTurnState());
    }

}