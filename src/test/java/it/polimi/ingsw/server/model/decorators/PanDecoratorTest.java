package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class PanDecoratorTest {
    Match match;
    Player player;
    Commands commands;
    Position startingPosition1;
    Position endingPosition;
    Set<GodCards> textCards = new HashSet<GodCards>();

    @BeforeEach
    void setUp() {
        player = new Player(0,"Vasio");
        match = new Match(1,player);
        match.addPlayer(player);
        textCards.add(GodCards.Pan);
        match.setCards(textCards);
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

        assertFalse(commands.winningCondition(player), "Qualcosa è andato storto.");
    }

    @Test
    void winningCondition_StartingLevelZero_EndingLevelOne_NotWinning_2DEndingPosition() {
        startingPosition1 = new Position(1,2);
        endingPosition = new Position(2,2);
        endingPosition.setZ(1);

        player.setWorker(startingPosition1);
        player.setCurrentWorker(startingPosition1);
        player.getCurrentWorker().setPosition(endingPosition);

        assertFalse(commands.winningCondition(player), "Qualcosa è andato storto.");
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
        assertTrue(commands.winningCondition(player), "Qualcosa è andato storto.");
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

        assertTrue(commands.winningCondition(player), "Qualcosa è andato storto.");
    }
}



