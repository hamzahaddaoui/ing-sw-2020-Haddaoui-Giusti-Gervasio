package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.controller.state.GettingPlayersNum;
import it.polimi.ingsw.server.controller.state.SelectingGodCards;
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

    View view = new View();
    Controller controller = new Controller();
    ControlState state;
    Player player;
    GameBoard gameBoard;
    String stringMessage;
    Set<Position> placingAvailablePosition;


    void setCells() {
        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++)
                placingAvailablePosition.add(new Position(x, y));
        //System.out.println(gameBoard.getColoredPosition());
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

    private Set<String> selectedCards() {
        Set<String> cards = new HashSet<>();
        cards.add("Apollo");
        cards.add("Pan");
        return cards;
    }

    @BeforeEach
    void setUp() {
        player = View.getPlayer();
        gameBoard = View.getGameBoard();
        placingAvailablePosition = gameBoard.getPlacingAvailableCells();
        setCells();
        gameBoard.setMatchCards(settingCards());
        gameBoard.setSelectedGodCards(selectedCards());
    }

    synchronized void notifyController(PlayerState playerState, MatchState matchState, String viewObject) {
        player.setPlayerState(playerState);
        player.setMatchState(matchState);
        //controller.update(viewObject);
    }

}
