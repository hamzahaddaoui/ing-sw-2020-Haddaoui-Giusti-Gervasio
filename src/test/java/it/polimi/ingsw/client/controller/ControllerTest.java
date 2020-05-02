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

    Controller controller;
    View view;
    Player player;
    GameBoard gameBoard;
    String stringMessage;
    Set <Position> placingAvailablePosition ;
    ArrayList<String> matchCards;


    void setCells() {
        for (int x = 0; x<5; x++)
            for (int y = 0; y<5; y++)
                placingAvailablePosition.add(new Position(x,y));
            gameBoard.setColoredPosition(placingAvailablePosition.stream().findAny().get());
            System.out.println(gameBoard.getColoredPosition());
    }

    void setGodCards(){
        matchCards = new ArrayList<>();
        matchCards.add("APOLLO");
        matchCards.add("ARTEMIS");
        matchCards.add("PAN");
        matchCards.add("HEPHAESTUS");
        matchCards.add("ATHENA");
        matchCards.add("PROMETHEUS");
        matchCards.add("MINOTAUR");
        matchCards.add("DEMETER");
        matchCards.add("ATLAS");
        gameBoard.setColoredGodCard(matchCards.get(0));
    }

    @BeforeEach
    void add(){
        controller = new Controller();
        player = view.getPlayer();
        gameBoard = view.getGameBoard();
        placingAvailablePosition = gameBoard.getPlacingAvailableCells();
        ArrayList <Integer> nums = new ArrayList<>();
        nums.add(2);
        nums.add(3);
        setGodCards();
    }

    @Test
    void update() {
        stringMessage = "LEO";

        assertTrue(controller.getControlState().getClass() == StartingStatus.class);
        //assertTrue(controller.getMatchState() == null);
        //assertTrue(controller.getPlayerState() == null);

        controller.update(stringMessage);

        assertTrue(controller.getControlState().getClass() == StartingStatus.class);
        assertTrue(controller.getControlState().processingMessage(stringMessage));

        //WAITING_FOR_PLAYERS

        player.setPlayerState(PlayerState.INITIALIZED);
        player.setMatchState(MatchState.WAITING_FOR_PLAYERS);
        controller.update(InsertCharacter.A);

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
        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectionNumberStatus.class);
        assertTrue(controller.getControlState().processingMessage(InsertCharacter.ENTER));
        
        //SELECTING_GOD_CARDS
        //player.setPlayersNum(3);
        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.SELECTING_GOD_CARDS);
        gameBoard.setMatchCards(settingCards());
        gameBoard.setColoredGodCard(gameBoard.getMatchCards().get(0));
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.E));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.D));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == SelectingGodCardsStatus.class);
        assertTrue(controller.getControlState().processingMessage(InsertCharacter.ENTER));

        //WAITING STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.D));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.E));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));

        //SELECTING_SPECIAL_COMMANDS

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);
        gameBoard.setColoredGodCard(gameBoard.getMatchCards().get(0));

        assertTrue(player.getPlayersNum() != null);
        assertTrue(gameBoard.getSelectedGodCards() != null);
        //assertTrue(gameBoard.getSelectedGodCards().size() == player.getPlayersNum());
        assertTrue(gameBoard.getColoredGodCard() != null);

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.E));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.D));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == SelectingSpecialCommandStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        String chosenCard = gameBoard.getColoredGodCard();
        System.out.println(chosenCard);

        controller.update(InsertCharacter.ENTER);

        System.out.println(gameBoard.getColoredGodCard());
        assertTrue(controller.getControlState().processingMessage(InsertCharacter.ENTER));
        assertTrue(gameBoard.getColoredGodCard().equals(chosenCard));

        //WAITING STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.SELECTING_SPECIAL_COMMAND);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.D));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.E));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));
        //PLACING_STATUS

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.PLACING_WORKERS);
        setCells();
        gameBoard.setColoredPosition(placingAvailablePosition.stream().findAny().get());

        assertTrue(gameBoard.getPlacingAvailableCells() != null);
        assertTrue(gameBoard.getColoredPosition() != null);

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        Position positionX = gameBoard.getColoredPosition();
        System.out.println(positionX);

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        gameBoard.getWorkersAvailableCells().keySet().stream().forEach(w->System.out.println(w));
        assertTrue( gameBoard.getWorkersAvailableCells().keySet().contains(positionX));
        assertTrue(gameBoard.getWorkersAvailableCells().keySet().size() == 1);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.D));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.E));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        positionX = gameBoard.getColoredPosition();
        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == PlacingWorkersStatus.class);
        assertTrue( gameBoard.getWorkersAvailableCells().keySet().contains(positionX));
        assertTrue(gameBoard.getWorkersAvailableCells().keySet().size() == 2);

        //WAITING STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.PLACING_WORKERS);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.D));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.E));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));
        //RUNNING

        player.setPlayerState(PlayerState.ACTIVE);
        player.setMatchState(MatchState.RUNNING);
        player.setTurnState(TurnState.IDLE);

        assertTrue(gameBoard.getWorkersPositions() != null);

        gameBoard.setStartingPosition(gameBoard.getWorkersPositions().stream().findFirst().get());

        assertTrue(gameBoard.getStartingPosition() != null);

        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == RunningStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == RunningStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == RunningStatus.class);
        assertTrue(controller.getControlState().processingMessage(InsertCharacter.ENTER));

        //WAITING STATUS

        player.setPlayerState(PlayerState.IDLE);
        player.setMatchState(MatchState.PLACING_WORKERS);
        controller.update(InsertCharacter.A);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.A));

        controller.update(InsertCharacter.S);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.S));

        controller.update(InsertCharacter.ENTER);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.ENTER));

        controller.update(InsertCharacter.W);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.W));

        controller.update(InsertCharacter.D);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.D));

        controller.update(InsertCharacter.F);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.F));

        controller.update(InsertCharacter.E);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.E));

        controller.update(InsertCharacter.Q);

        assertTrue(controller.getControlState().getClass() == WaitingStatus.class);
        assertTrue(!controller.getControlState().processingMessage(InsertCharacter.Q));

    }

    private Set<String> settingCards() {
        Set<String> cards = new HashSet<>();
        cards.add("Apollo");
        cards.add("Artemis");
        cards.add("Athena");
        cards.add("Hepheastus");
        cards.add("Demeter");
        cards.add("Minotaur");
        cards.add("Prometheus");
        cards.add("Pan");
        cards.add("Atlas");
        return cards;
    }
}