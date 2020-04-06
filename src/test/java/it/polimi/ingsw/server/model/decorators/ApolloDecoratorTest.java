package it.polimi.ingsw.server.model.decorators;

import org.junit.Assert.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class ApolloDecoratorTest {

    Commands commands;
    Match match=new Match(1);
    Player player1=new Player(1,"leo",match);
    Player player2=new Player(2,"dario",match);
    Position position;

    @BeforeEach
    void setUp() {
        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addCard(GodCards.Apollo);
        player1.setCommands(GodCards.Apollo);
        player2.setCommands(GodCards.Artemis);
        commands=player1.getCommands();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testMoveWorker() {
    }

    @Test
    void exchangePosition() {
    }

    @Test
    void testComputeAvailableMovements() {
        Position position1=new Position(1,2);
        player1.setWorker(position1);
        Position position2=new Position(2,2);
        player1.setWorker(position2);
        Position position3=new Position(1,1);
        player2.setWorker(position3);
        Position position4=new Position(0,2);
        player2.setWorker(position4);
        player1.setCurrentWorker(position1);
        Worker worker=player1.getCurrentWorker();
        Set<Position> positionSet= commands.computeAvailableMovements(player1,worker);
        Set<Position> positions= new HashSet<Position>();
        positions.add(new Position(0,0));
        positions.add(new Position(0,3));
        positions.add(new Position(1,3));
        positions.add(new Position(2,1));
        positions.add(new Position(2,3));
        A
    }
}