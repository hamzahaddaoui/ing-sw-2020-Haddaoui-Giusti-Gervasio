package it.polimi.ingsw.server.model.decorators;


import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

public class ArtemisDecoratorTest {

    Commands commands1, commands2;
    Match match = new Match(2);
    Player player1 = new Player(1, "leo", match);
    Player player2 = new Player(2, "dario", match);

    Position position00 = new Position(0, 0);
    Position position01 = new Position(0, 1);
    Position position02 = new Position(0, 2);
    Position position03 = new Position(0, 3);
    Position position04 = new Position(0, 4);
    Position position10 = new Position(1, 0);
    Position position11 = new Position(1, 1);
    Position position12 = new Position(1, 2);
    Position position13 = new Position(1, 3);
    Position position14 = new Position(1, 4);
    Position position20 = new Position(2, 0);
    Position position21 = new Position(2, 1);
    Position position22 = new Position(2, 2);
    Position position23 = new Position(2, 3);
    Position position24 = new Position(2, 4);
    Position position30 = new Position(3, 0);
    Position position31 = new Position(3, 1);
    Position position32 = new Position(3, 2);
    Position position33 = new Position(3, 3);
    Position position34 = new Position(3, 4);
    Position position40 = new Position(4, 0);
    Position position41 = new Position(4, 1);
    Position position42 = new Position(4, 2);
    Position position43 = new Position(4, 3);
    Position position44 = new Position(4, 4);


    @BeforeEach
    void setUp() {
        match.addPlayer(player1);
        match.setPlayersNum(2);
        match.addPlayer(player2);
        match.addCard(GodCards.Apollo);
        match.addCard(GodCards.Artemis);
        player1.setCommands(GodCards.Artemis);
        player2.setCommands(GodCards.Apollo);
        commands1 = player1.getCommands();
        commands2 = player2.getCommands();
    }

    @Test
    public void nextStateCaseSpecialFunctionNotInserted() {
        player1.setWorker(position12);
        player1.setTurnState(TurnState.IDLE);
        player1.setTurnState(commands1.nextState(player1));
        player1.setCurrentWorker(position12);
        commands1.moveWorker(position11, player1);
        player1.setTurnState(commands1.nextState(player1));

        Assert.assertTrue("ERROR", player1.getTurnState() == TurnState.BUILD);

        player1.setTurnState(commands1.nextState(player1));
        Assert.assertTrue("ERROR", player1.getTurnState() == TurnState.IDLE);
    }

    @Test
    public void nextStateCaseSpecialFunctionInserted() {
        player1.setWorker(position12);
        player1.setTurnState(TurnState.IDLE);
        player1.setTurnState(commands1.nextState(player1));
        player1.setCurrentWorker(position12);
        commands1.moveWorker(position11, player1);
        player1.setUnsetSpecialFunction();
        player1.setTurnState(commands1.nextState(player1));

        Assert.assertTrue("ERROR", player1.getTurnState() == TurnState.MOVE);
    }

    @Test
    public void computeAvailableMovements() {
        player1.setWorker(position33);
        player2.setWorker(position41);
        player1.setCurrentWorker(position33);

        Worker worker = player1.getCurrentWorker();
        Set<Position> positionSet0 = commands1.computeAvailableMovements(player1, worker);

        Set<Position> positionCheck = new HashSet<>();
        positionCheck.add(position22);
        positionCheck.add(position23);
        positionCheck.add(position24);
        positionCheck.add(position32);
        positionCheck.add(position34);
        positionCheck.add(position42);
        positionCheck.add(position43);
        positionCheck.add(position44);

        Assert.assertTrue("ERRORE1", positionCheck.containsAll(positionSet0));
        Assert.assertTrue("ERRORE2", positionSet0.containsAll(positionCheck));

        commands1.moveWorker(position23, player1);
        worker = player1.getCurrentWorker();
        Set<Position> positionSet1 = commands1.computeAvailableMovements(player1, worker);

        Assert.assertTrue("ERRORE3", positionSet1.contains(position33));

        worker = player1.getCurrentWorker();
        Set<Position> positionSet2 = commands1.computeAvailableMovements(player1, worker);
        player1.setCurrentWorker(position23);
        positionCheck.clear();
        positionCheck.add(position12);
        positionCheck.add(position13);
        positionCheck.add(position14);
        positionCheck.add(position22);
        positionCheck.add(position24);
        positionCheck.add(position32);
        positionCheck.add(position33);
        positionCheck.add(position34);

        //positionSet2.stream().forEach(position -> System.out.println("Position(" + position.getX() + ";" + position.getY() + ")"));

        Assert.assertTrue("ERRORE4", positionCheck.containsAll(positionSet2));
        Assert.assertTrue("ERRORE5", positionSet2.containsAll(positionCheck));

    }
}