package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utilities.TurnState.BUILD;
import static it.polimi.ingsw.utilities.TurnState.IDLE;
import static org.junit.jupiter.api.Assertions.*;

class TritonDecoratorTest {

    Player p1 = new Player(1,"vasio");
    Match match = new Match(1,p1);
    Player p2 = new Player(2,"dario");
    Player p3 = new Player(3,"paolo");
    Commands commands;
    Billboard billboard;
    Set<GodCards> testingCards = new HashSet<>();
    Set<Position> testingPositions = new HashSet<>();
    Set<Position> test1 = new HashSet<>();
    Set<Position> test2 = new HashSet<>();

    @BeforeEach
    void setUp() {
        match.setPlayersNum(3);
        billboard = match.getBillboard();
        match.addPlayer(p2);
        match.addPlayer(p3);

        testingCards.add(GodCards.Triton);
        testingCards.add(GodCards.Pan);
        testingCards.add(GodCards.Apollo);
        match.setCards(testingCards);

        p1.setCommands(GodCards.Triton);
        commands = p1.getCommands();
        p2.setCommands(GodCards.Pan);
        p3.setCommands(GodCards.Apollo);

        match.nextState();
        match.nextState();
        match.nextState();
        match.nextState();
        match.nextState();

        p1.setWorker(new Position(0,0));
        p1.setWorker(new Position(4,4));
        p2.setWorker(new Position(1,3));
        p2.setWorker(new Position(4,3));
        p3.setWorker(new Position(1,1));
        p3.setWorker(new Position(1,4));

        p1.setCurrentWorker(new Position(0,0));
    }

    @Test
    void playerTurn_MoveUntilNoMoreAvailableCells() {
        Worker curr = p1.getCurrentWorker();
        commands.computeAvailableMovements(p1,curr);
        commands.moveWorker(new Position(0,1),p1);
        commands.nextState(p1);
        p1.setUnsetSpecialFunction(true);
        p1.setUnsetSpecialFunction(false);
        p1.setUnsetSpecialFunction(true);
        commands.computeAvailableMovements(p1,curr);
        commands.moveWorker(new Position(0,2),p1);
        commands.nextState(p1);
        commands.computeAvailableMovements(p1,curr);
        commands.moveWorker(new Position(0,3),p1);
        commands.nextState(p1);
        commands.computeAvailableMovements(p1,curr);
        commands.moveWorker(new Position(0,4),p1);
        commands.nextState(p1);
        assertEquals(p1.getTurnState(),BUILD);
        commands.computeAvailableBuildings(p1,curr);
        commands.build(new Position(0,3),p1);
        commands.nextState(p1);
        assertEquals(p1.getTurnState(),IDLE);
    }

    @Test
    void playerTurn_BasicTurn() {
        p1.setCurrentWorker(new Position(4,4));
        Worker curr = p1.getCurrentWorker();
        commands.computeAvailableMovements(p1,curr);
        commands.moveWorker(new Position(3,3),p1);
        commands.nextState(p1);
        assertEquals(p1.getTurnState(),BUILD);
        commands.computeAvailableBuildings(p1,curr);
        commands.build(new Position(4,4),p1);
        commands.nextState(p1);
        assertEquals(p1.getTurnState(),IDLE);
    }
}