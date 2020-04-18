package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utilities.TurnState.*;

class PrometheusDecoratorTest {

    Set<Position> positions;
    HashSet<Position> positionsToCheck = new HashSet<>();
    Match match = new Match(1);
    Player player = new Player(1,"Vasio",match);
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
        match.addPlayer(player);
        player.setCommands(GodCards.Prometheus);
        commands=player.getCommands();
        player.setWorker(position1);
        player.setCurrentWorker(position1);
        billboard.incrementTowerHeight(position2);
        player.setTurnState(IDLE);
    }

    @AfterEach
    void tearDown() {
        billboard.getTowerHeight().replace(position2,0);
        billboard.getTowerHeight().replace(position3,0);
    }

    @Test
    void turn_NotSpecialFunction() {
        player.setTurnState(commands.nextState(player));
        Assert.assertEquals(MOVE,player.getTurnState());
        commands.moveWorker(position2,player);
        player.setTurnState(commands.nextState(player));
        Assert.assertEquals(BUILD,player.getTurnState());
        commands.build(position3,player);
        Assert.assertEquals("Build problem.", 1, billboard.getTowerHeight(position3));
        player.setTurnState(commands.nextState(player));
        Assert.assertEquals(IDLE,player.getTurnState());
        Assert.assertTrue("Next state problem.",player.hasFinished());
    }

    @Test
    void turn_SpecialFunction() {
        player.setUnsetSpecialFunction();
        player.setTurnState(commands.nextState(player));
        Assert.assertEquals(BUILD,player.getTurnState());
        commands.build(position3,player);
        Assert.assertEquals("Build problem.", 1, billboard.getTowerHeight(position3));
        player.setTurnState(commands.nextState(player));
        Assert.assertEquals(MOVE,player.getTurnState());
        commands.moveWorker(position4,player);
        player.setTurnState(commands.nextState(player));
        Assert.assertEquals(BUILD,player.getTurnState());
        commands.build(position3,player);
        Assert.assertEquals("Build problem.", 2, billboard.getTowerHeight(position3));
        player.setTurnState(commands.nextState(player));
        Assert.assertEquals(IDLE,player.getTurnState());
        Assert.assertTrue("Next state problem.",player.hasFinished());
        player.setUnsetSpecialFunction();
    }

    @Test
    void build_BuildDomeCase() {
        billboard.incrementTowerHeight(position3);
        billboard.incrementTowerHeight(position3);
        billboard.incrementTowerHeight(position3);
        commands.build(position3,player);
        Assert.assertTrue("Build problem", billboard.getDome(position3));
    }

    @Test
    void computeAvailableMovements_SpecialFunction() {
        player.setUnsetSpecialFunction();
        position2.setZ(1);
        positions = commands.computeAvailableMovements(player,player.getCurrentWorker());
        player.setUnsetSpecialFunction();
        Assert.assertFalse("Wrong Special Function.",positions.contains(position2));
    }

    @Test
    void computeAvailableMovements_NotSpecialFunctionWithDoubleSetUnset() {
        player.setUnsetSpecialFunction();
        player.setUnsetSpecialFunction();
        positions = commands.computeAvailableMovements(player,player.getCurrentWorker());
        Assert.assertTrue("Wrong Special Function.",positions.contains(position2));
    }
}