package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.controller.state.GettingPlayersNum;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    View view = View.constructor();
    Controller controller = new Controller();
    Player player = View.getPlayer();
    GameBoard gameBoard = View.getGameBoard();
    String stringMessage;
    InsertCharacter insertCharacter;
    Set <Position> placingAvailablePosition = gameBoard.getPlacingAvailableCells();

    void setCells() {
        for (int x = 0; x<5; x++)
            for (int y = 0; y<5; y++)
                placingAvailablePosition.add(new Position(x,y));
    }

    @Test
    void update() {
        stringMessage = "LEO";

        assertTrue(controller.getControlState().getClass() == StartingStatus.class);
        assertTrue(controller.getMatchState() == null);
        assertTrue(controller.getPlayerState() == null);

        controller.update(stringMessage);

        assertTrue(controller.getControlState().getClass() == StartingStatus.class);
        assertTrue(controller.getControlState().processingMessage(stringMessage));

        //WAITING_FOR_PLAYERS

        player.setPlayerState(PlayerState.INITIALIZED);
        player.setMatchState(MatchState.WAITING_FOR_PLAYERS);
        System.out.println();
        System.out.println(controller.getControlState());
        System.out.println();
        controller.update(InsertCharacter.A);
        System.out.println();
        System.out.println(controller.getControlState());
        System.out.println();
        assertTrue(controller.getControlState().getClass() == (WaitingStatus.class));
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.D));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.E));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        //GETTING_PLAYERS_NUM

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.GETTING_PLAYERS_NUM);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(controller.isMessageReady());
        
        //SELECTING_GOD_CARDS
        player.setPlayersNum(3);
        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.SELECTING_GOD_CARDS);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.SELECTING_GOD_CARDS);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.SELECTING_GOD_CARDS);

        assertThrows( IllegalArgumentException.class ,()-> controller.update(InsertCharacter.S));

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.SELECTING_GOD_CARDS);
        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(controller.isMessageReady());

        //WAITING STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        //SELECTING_SPECIAL_COMMANDS

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);

        assertTrue(player.getPlayersNum() != null);
        assertTrue(gameBoard.getSelectedGodCards() != null);
        assertTrue(gameBoard.getSelectedGodCards().size() == player.getPlayersNum());
        assertTrue(gameBoard.getColoredGodCard() != null);

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.isMessageReady());

        String chosenCard = gameBoard.getColoredGodCard();

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.isMessageReady());
        assertTrue(gameBoard.getColoredGodCard() == chosenCard);

        //WAITING STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());
        //PLACING_STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.PLACING_WORKERS);
        setCells();
        gameBoard.setColoredPosition(placingAvailablePosition.stream().findAny().get());

        assertTrue(gameBoard.getPlacingAvailableCells() != null);
        assertTrue(gameBoard.getColoredPosition() != null);

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.isMessageReady());

        Position position = gameBoard.getColoredPosition();

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue( gameBoard.getWorkersAvailableCells().keySet().contains(position));
        assertTrue(gameBoard.getWorkersAvailableCells().keySet().size() == 1);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.isMessageReady());

        position = gameBoard.getColoredPosition();
        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue( gameBoard.getWorkersAvailableCells().keySet().contains(position));
        assertTrue(gameBoard.getWorkersAvailableCells().keySet().size() == 2);
        assertTrue(controller.isMessageReady());

        //WAITING STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.PLACING_WORKERS);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());
        //RUNNING

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.RUNNING);
        player.setTurnState(TurnState.IDLE);

        assertTrue(gameBoard.getWorkersPositions() != null);

        gameBoard.setStartingPosition(gameBoard.getWorkersPositions().stream().findFirst().get());

        assertTrue(gameBoard.getStartingPosition() != null);

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == RunningStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == RunningStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == RunningStatus.class);
        assertTrue(controller.isMessageReady());

        //WAITING STATUS

        //WAITING STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.PLACING_WORKERS);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

    }
}