package it.polimi.ingsw.server.model.decorators;


import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

public class ArtemisDecoratorTest {

    Commands commands1,commands2;
    Match match=new Match(2);
    Player player1=new Player(1,"leo",match);
    Player player2=new Player(2,"dario",match);
    Position p;

    @BeforeEach
    void setUp() {
        match.addPlayer(player1);
        match.setPlayersNum(2);
        match.addPlayer(player2);
        match.addCard(GodCards.Apollo);
        match.addCard(GodCards.Artemis);
        player1.setCommands(GodCards.Artemis);
        player2.setCommands(GodCards.Apollo);
        commands1=player1.getCommands();
        commands2=player2.getCommands();
    }

    @Test
    public void nextStateCaseSpecialFunctionNotInserted() {
        Position pos1=new Position(1,2);
        Position pos2=new Position(1,1);
        player1.setWorker(pos1);
        player1.setState(TurnState.WAIT);
        player1.setState(commands1.nextState(player1));
        player1.setCurrentWorker(pos1);
        commands1.moveWorker(pos2,player1);
        player1.setState(commands1.nextState(player1));
        Assert.assertTrue("ERROR",player1.getState()==TurnState.BUILD);
    }

    @Test
    public void nextStateCaseSpecialFunctionInserted() {
        Position pos1=new Position(1,2);
        Position pos2=new Position(1,1);
        player1.setWorker(pos1);
        player1.setState(TurnState.WAIT);
        player1.setState(commands1.nextState(player1));
        player1.setCurrentWorker(pos1);
        commands1.moveWorker(pos2,player1);
        player1.setUnsetSpecialFunction();
        player1.setState(commands1.nextState(player1));
        Assert.assertTrue("ERROR",player1.getState()==TurnState.MOVE);
    }

    @Test
    public void computeAvailableMovements() {
        Position position1=new Position(3,3);
        player1.setWorker(position1);
        Position position3=new Position(4,1);
        player2.setWorker(position3);
        player1.setCurrentWorker(position1);
        Position positionX= new Position(2,3);
        commands1.moveWorker(positionX,player1);
        Worker worker=player1.getCurrentWorker();
        Set<Position> positionSet= commands1.computeAvailableMovements(player1,worker);
        Assert.assertTrue("Error",positionSet.contains(position1));
    }

    @Test
    public void move(){}
}