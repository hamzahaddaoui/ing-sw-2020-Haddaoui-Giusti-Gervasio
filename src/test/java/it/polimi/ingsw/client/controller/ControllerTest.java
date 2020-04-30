package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.controller.state.GettingPlayersNum;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
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
    Set<Position> pos = new HashSet<Position>();
    String stringMessage;
    InsertCharacter insertCharacter;

    void setCells() {
        for (int x = 0; x<5; x++)
            for (int y = 0; y<5; y++)
                pos.add(new Position(x,y));
    }

    @Test
    void update() {
        stringMessage = "LEO";

        assertTrue(controller.getControlState().getClass() == StartingStatus.class);
        assertTrue(controller.getMatchState().getClass() == null);
        assertTrue(controller.getPlayerState().getClass() == null);

        controller.update(stringMessage);

        assertTrue(controller.getControlState().getClass() == StartingStatus.class);
        assertTrue(controller.isMessageReady());

        player.setPlayerState(PlayerState.INITIALIZED);
        player.setMatchState(MatchState.WAITING_FOR_PLAYERS);
        controller.update(InsertCharacter.A);
        //WAITING_FOR_PLAYERS

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.isMessageReady());

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.GETTING_PLAYERS_NUM);
        controller.update(InsertCharacter.A);
        //GETTING_PLAYERS_NUM

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

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.SELECTING_GOD_CARDS);
        controller.update(InsertCharacter.A);
        //SelectionNumberStatus

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.isMessageReady());

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(controller.isMessageReady());

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



    }
}