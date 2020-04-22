package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class ApolloDecoratorTest {

    Commands commands1,commands2;
    Player player1 = new Player(1,"leo");
    Match match = new Match(1, player1);
    Player player2 = new Player(2,"dario");
    Set<Position> positions = new HashSet<Position>();
    Set<GodCards> godCards = new HashSet<GodCards>();

    Position position00=new Position(0,0);
    Position position01=new Position(0,1);
    Position position02=new Position(0,2);
    Position position03=new Position(0,3);
    Position position04=new Position(0,4);
    Position position10=new Position(1,0);
    Position position11=new Position(1,1);
    Position position12=new Position(1,2);
    Position position13=new Position(1,3);
    Position position14=new Position(1,4);
    Position position20=new Position(2,0);
    Position position21=new Position(2,1);
    Position position22=new Position(2,2);
    Position position23=new Position(2,3);
    Position position24=new Position(2,4);
    Position position30=new Position(3,0);
    Position position31=new Position(3,1);
    Position position32=new Position(3,2);
    Position position33=new Position(3,3);
    Position position34=new Position(3,4);
    Position position40=new Position(4,0);
    Position position41=new Position(4,1);
    Position position42=new Position(4,2);
    Position position43=new Position(4,3);
    Position position44=new Position(4,4);

    @BeforeEach
    void setUp() {
        match.addPlayer(player2);
        match.setPlayersNum(2);
        godCards.add(GodCards.Apollo);
        godCards.add(GodCards.Artemis);
        match.setCards(godCards);
        player1.setCommands(GodCards.Apollo);
        player2.setCommands(GodCards.Artemis);
        commands1=player1.getCommands();
        commands2=player2.getCommands();
    }

    @Test
    void testMoveWorkerChange() {
        player1.setWorker(position12);
        player2.setWorker(position11);
        player1.setCurrentWorker(position12);
        commands1.moveWorker(position11, player1);

        assertEquals(match.getBillboard().getPlayer(position12),player2.getID());
        assertEquals(match.getBillboard().getPlayer(position11),player1.getID());

        commands1.build(position00,player1);
        match.setMoveUpActive(true);
        assertEquals(match.getBillboard().getTowerHeight(position00),1);
        player1.setCurrentWorker(position11);
        commands1.moveWorker(position00, player1);

        assertEquals(match.getBillboard().getPlayer(position00),player1.getID());
        assertEquals(match.getBillboard().getPlayer(position12),player2.getID());

        commands1.build(position01,player1);
        player1.setCurrentWorker(position00);
        match.setMoveUpActive(true);
        commands1.moveWorker(position01, player1);

        assertEquals(match.getBillboard().getPlayer(position01),player1.getID());

        player1.setCurrentWorker(position01);
        commands1.moveWorker(position11, player1);

        assertEquals(match.getBillboard().getPlayer(position11),player1.getID());
    }

    @Test
    void testMoveWorkerNonChange() {

        player1.setWorker(position12);
        player2.setWorker(position11);
        player1.setCurrentWorker(position12);
        commands1.moveWorker(position13, player1);

        assertEquals(match.getBillboard().getPlayer(position13),player1.getID());
        assertEquals(match.getBillboard().getPlayer(position12),null);
        assertEquals(match.getBillboard().getPlayer(position11),player2.getID());

    }


    @Test
    void testComputeAvailableMovements() {
        player1.setWorker(position12);
        player1.setWorker(position22);
        player2.setWorker(position11);
        player2.setWorker(position03);
        player1.setCurrentWorker(position12);
        Worker worker=player1.getCurrentWorker();

        Set<Position> positionSet= commands1.computeAvailableMovements(player1,worker);

        assertTrue( positionSet.contains(position03));
        assertTrue( positionSet.contains(position11));

        Set<Position> positionCheck=new HashSet<>();
        positionCheck.add(position03);
        positionCheck.add(position02);
        positionCheck.add(position01);
        positionCheck.add(position13);
        positionCheck.add(position11);
        positionCheck.add(position23);
        positionCheck.add(position21);

        //positionSet.stream().forEach(position -> System.out.println("Position(" + position.getX() + ";" + position.getY() + ")"));

        assertTrue(positionCheck.containsAll(positionSet));
        assertTrue(positionSet.containsAll(positionCheck));

        match.getBillboard().incrementTowerHeight(position11);
        player1.setCurrentWorker(position12);
        Set<Position> positionSet0= commands1.computeAvailableMovements(player1,worker);
        positionCheck.clear();
        positionCheck.add(position03);
        positionCheck.add(position02);
        positionCheck.add(position01);
        positionCheck.add(position13);
        positionCheck.add(position11);
        positionCheck.add(position23);
        positionCheck.add(position21);

        assertTrue(positionCheck.containsAll(positionSet0));
        assertTrue(positionSet0.containsAll(positionCheck));
    }


}