package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PanDecoratorTest {

    Player player;
    Commands commands;
    Position startingPosition1;
    Position endingPosition;

    @BeforeEach
    void setUp() {
        player = new Player(0,"Vasio",new Match(23));
        player.setCommands(GodCards.Pan);
        commands = player.getCommands();
    }

    @AfterEach
    void tearDown() {
        startingPosition1 = null;
        endingPosition = null;
    }

    @Test
    void winningCondition_StartingLevelZero_EndingLevelOne_NotWinning() {
        startingPosition1 = new Position(1,2);
        endingPosition = new Position(2,2);

        player.setWorker(startingPosition1);

        player.setCurrentWorker(startingPosition1);
        player.getCurrentWorker().setPosition(endingPosition);

        Assert.assertFalse("Qualcosa è andato storto.",commands.winningCondition(player));
    }

    @Test
    void winningCondition_StartingLevelZero_EndingLevelOne_NotWinning_2DEndingPosition() {
        startingPosition1 = new Position(1,2);
        endingPosition = new Position(2,2);
        endingPosition.setZ(1);

        player.setWorker(startingPosition1);
        player.setCurrentWorker(startingPosition1);
        player.getCurrentWorker().setPosition(endingPosition);

        Assert.assertFalse("Qualcosa è andato storto.",commands.winningCondition(player));
    }

    @Test
    void winningConditionPan_StartingLevelThree_EndingLevelOne() {
        startingPosition1 = new Position(1,2);
        endingPosition = new Position(2,2);

        player.setWorker(startingPosition1);

        player.setCurrentWorker(startingPosition1);
        player.getCurrentWorker().getPosition().setZ(3);
        endingPosition.setZ(1);
        player.getCurrentWorker().setPosition(endingPosition);
        Assert.assertTrue("Qualcosa è andato storto.",commands.winningCondition(player));
    }


    @Test
    void winningConditionStandard_StartingLevelTwo_EndingLevelThree() {
        startingPosition1 = new Position(1,2);
        endingPosition = new Position(2,2);

        player.setWorker(startingPosition1);

        player.setCurrentWorker(startingPosition1);
        player.getCurrentWorker().getPosition().setZ(2);
        endingPosition.setZ(3);
        player.getCurrentWorker().setPosition(endingPosition);

        Assert.assertTrue("Qualcosa è andato storto.",commands.winningCondition(player));
    }
}



