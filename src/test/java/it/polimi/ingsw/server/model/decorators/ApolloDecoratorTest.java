package it.polimi.ingsw.server.model.decorators;


import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

class ApolloDecoratorTest {

    Commands commands1,commands2;
    Match match=new Match(1);
    Player player1=new Player(1,"leo",match);
    Player player2=new Player(2,"dario",match);
    Position position;

    @BeforeEach
    void setUp() {
        match.addPlayer(player1);
        match.setPlayersNum(2);
        match.addPlayer(player2);
        match.addCard(GodCards.Apollo);
        match.addCard(GodCards.Artemis);
        player1.setCommands(GodCards.Apollo);
        player2.setCommands(GodCards.Artemis);
        commands1=player1.getCommands();
        commands2=player2.getCommands();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testMoveWorkerChange() {
        //IN: player1(pos1), player2(pos3)
        //END: player1(pos3), player2(pos1)
        Position position1=new Position(1,2);
        player1.setWorker(position1);
        Position position3=new Position(1,1);
        player2.setWorker(position3);
        player1.setCurrentWorker(position1);
        commands1.moveWorker(position3, player1);
        Assert.assertEquals(match.getBillboard().getPlayer(position1),player2.getID());
        Assert.assertEquals(match.getBillboard().getPlayer(position3),player1.getID());
    }

    @Test
    void testMoveWorkerNonChange() {
        //IN: player1(pos1), player2(pos3)
        //END: player1(pos), player2(pos3)
        Position position=new Position(1,3);
        Position position1=new Position(1,2);
        player1.setWorker(position1);
        Position position3=new Position(1,1);
        player2.setWorker(position3);
        player1.setCurrentWorker(position1);
        commands1.moveWorker(position, player1);
        Assert.assertEquals(match.getBillboard().getPlayer(position),player1.getID());
        Assert.assertEquals(match.getBillboard().getPlayer(position1),-1);
        Assert.assertEquals(match.getBillboard().getPlayer(position3),player2.getID());
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
        Set<Position> positionSet= commands1.computeAvailableMovements(player1,worker);
        Set<Position> positions= new HashSet<Position>();
        Assert.assertTrue("NOT OK", positionSet.contains(position4));
        Assert.assertTrue("not ok", positionSet.contains(position3));
    }

    @Test
    void testFindOpponentPlayer(){

    }

}