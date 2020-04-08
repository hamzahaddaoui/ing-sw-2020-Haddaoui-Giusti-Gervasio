package it.polimi.ingsw.server.model.decorators;


import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class DemeterDecoratorTest {

    Commands commands1,commands2;
    Match match=new Match(2);
    Player player1=new Player(1,"leo",match);
    Player player2=new Player(2,"dario",match);

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
        match.addPlayer(player1);
        match.setPlayersNum(2);
        match.addPlayer(player2);
        match.addCard(GodCards.Demeter);
        match.addCard(GodCards.Artemis);
        player1.setCommands(GodCards.Demeter);
        player2.setCommands(GodCards.Apollo);
        commands1=player1.getCommands();
        commands2=player2.getCommands();
    }

    @Test
    public void nextStateJustOneBuild() {
        player1.setWorker(position12);
        player1.setState(TurnState.WAIT);
        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("1",player1.getState()==TurnState.MOVE);

        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("2",player1.getState()==TurnState.BUILD);

        player1.setCurrentWorker(position12);
        commands1.build(position11,player1);
        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("3",player1.getState()==TurnState.WAIT);
        Assert.assertTrue("4",player1.hasFinished()==true);
    }

    @Test
    public void nextStateSecondBuild() {
        player1.setWorker(position12);
        player1.setState(TurnState.WAIT);
        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("E1",player1.getState()==TurnState.MOVE);

        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("E2",player1.getState()==TurnState.BUILD);

        player1.setCurrentWorker(position12);
        commands1.build(position11,player1);
        player1.setUnsetSpecialFunction();

        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("E3",player1.getState()==TurnState.BUILD);

        player1.setCurrentWorker(position12);
        commands1.build(position01,player1);
        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("E4",player1.getState()==TurnState.WAIT);
        Assert.assertTrue("E5",player1.hasFinished()==true);
    }

    @Test
    public void computeAvailableBuildings() {
        player1.setWorker(position12);
        player1.setWorker(position00);
        player2.setWorker(position13);
        player2.setWorker(position41);
        player1.setState(TurnState.WAIT);
        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("ER!",player1.getState()==TurnState.MOVE);

        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("ER0",player1.getState()==TurnState.BUILD);

        Set<Position> Set= new HashSet<>();
        Set.add(position03);
        Set.add(position02);
        Set.add(position01);
        Set.add(position11);
        Set.add(position23);
        Set.add(position22);
        Set.add(position21);
        player1.setCurrentWorker(position12);
        Set<Position> SetCheck=commands1.computeAvailableBuildings(player1,player1.getCurrentWorker());

        Assert.assertTrue("ER1",SetCheck.containsAll(Set));
        Assert.assertTrue("ER2",Set.containsAll(SetCheck));

        Set.clear();

        commands1.build(position11,player1);
        player1.setUnsetSpecialFunction();

        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("ER3",player1.getState()==TurnState.BUILD);

        player1.setCurrentWorker(position12);
        Set.add(position03);
        Set.add(position02);
        Set.add(position01);
        Set.add(position11);
        Set.add(position23);
        Set.add(position22);
        Set.add(position21);
        SetCheck=commands1.computeAvailableBuildings(player1,player1.getCurrentWorker());

        Assert.assertTrue(SetCheck.containsAll(Set));
        Assert.assertTrue(Set.containsAll(SetCheck));
        commands1.build(position01,player1);
        player1.setState(commands1.nextState(player1));

        Assert.assertTrue("ER4",player1.getState()==TurnState.WAIT);
        Assert.assertTrue("ER5",player1.hasFinished()==true);
    }

}