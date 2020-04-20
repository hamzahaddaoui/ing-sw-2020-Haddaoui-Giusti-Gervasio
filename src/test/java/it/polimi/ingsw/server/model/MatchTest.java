package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    Player matchMaster = new Player(20, "PincoPallino");
    Player player2, player3;
    Match match = new Match(30, matchMaster);

    List<Player> playersList = new ArrayList<>();
    Player player;

    @BeforeEach
    void setUp(){
        assertEquals(30, match.getID());
        assertEquals(0, match.getPlayersNum());
    }


    @Test
    void testInitialConditions(){
        assertFalse(match.isNumReached());
        assertEquals(Collections.singletonList(matchMaster),match.getPlayers());
        assertEquals(Collections.emptyList(), match.getLosers());
        assertEquals(Collections.singletonList(matchMaster), match.getAllPlayers());
        assertEquals(matchMaster, match.getCurrentPlayer());
        assertEquals(Collections.emptySet(),match.getCards());
        assertEquals(MatchState.GETTING_PLAYERS_NUM, match.getCurrentState());
        assertFalse(match.isStarted());
        assertTrue(match.isMoveUpActive());
    }

    @Test
    void testAddingPlayers(){
        match.setPlayersNum(2);

        playersList.add(matchMaster);

        assertFalse(match.isNumReached());

        player2 = new Player(1,"A");
        playersList.add(player2);
        match.addPlayer(player2);
        assertTrue(match.isNumReached());

        match.setPlayersNum(3);
        assertFalse(match.isNumReached());

        player3 = new Player(2,"B");
        playersList.add(player3);
        match.addPlayer(player3);
        assertTrue(match.isNumReached());


        assertEquals(playersList,match.getPlayers());
        assertEquals(Collections.emptyList(), match.getLosers());
        assertEquals(playersList, match.getAllPlayers());
        assertEquals(matchMaster, match.getCurrentPlayer());
    }

    @Test
    void testRemovingPlayers(){
        testAddingPlayers();

        List<Player> modifiedlist = new ArrayList<>(playersList);

        //elimino un giocatore
        player = modifiedlist.remove(0);
        match.removePlayer(player);
        assertEquals(PlayerState.LOST, player.getPlayerState());

        //della lista dei perdenti va il giocatore eliminato
        List<Player> losersList = new ArrayList<>();
        losersList.add(player);
        assertEquals(losersList, match.getLosers());

        //verifico che siano rimasti tutti i giocatori (perdenti e non)
        assertTrue(match.getAllPlayers().containsAll(playersList) && playersList.containsAll(match.getAllPlayers()));

        //verifico che riano rimasti i giocatori attivi
        assertEquals(modifiedlist, match.getPlayers());
        assertEquals(modifiedlist.get(0), match.getCurrentPlayer());

        assertEquals(MatchState.GETTING_PLAYERS_NUM, match.getCurrentState());
        assertNull(match.getWinner());

        //rimozione secondo player
        player = modifiedlist.remove(0);
        match.removePlayer(player);
        assertEquals(PlayerState.LOST, player.getPlayerState());

        //lo aggiungo alla lista dei perdenti
        losersList.add(player);
        assertEquals(losersList, match.getLosers());

        //verifico che la lista dei giocatori sia immutata
        assertTrue(match.getAllPlayers().containsAll(playersList) && playersList.containsAll(match.getAllPlayers()));



        assertEquals(modifiedlist, match.getPlayers());
        assertEquals(modifiedlist.get(0), match.getCurrentPlayer());

        assertEquals(MatchState.FINISHED, match.getCurrentState());
        assertEquals(modifiedlist.get(0), match.getWinner());

        match.resetLosers();
        assertEquals(Collections.emptyList(), match.getLosers());
    }

    @Test
    void testCheckPlayersWinner(){
        testAddingPlayers();

        player2.win();
        match.checkPlayers();
        List<Player> losersList = new ArrayList<>();
        losersList.add(matchMaster);
        losersList.add(player3);

        assertEquals(losersList, match.getLosers());
        assertEquals(Collections.singletonList(player2), match.getPlayers());
        assertEquals(MatchState.FINISHED, match.getCurrentState());
        assertEquals(player2, match.getWinner());
    }

    @Test
    void testCheckPlayersLoser(){
        testAddingPlayers();
        List<Player> modifiedlist = new ArrayList<>(playersList);

        player2.lost();
        match.checkPlayers();
        List<Player> losersList = new ArrayList<>();
        losersList.add(player2);

        assertEquals(losersList, match.getLosers());

        modifiedlist.remove(player2);
        assertEquals(modifiedlist, match.getPlayers());
        assertEquals(MatchState.GETTING_PLAYERS_NUM, match.getCurrentState());
        assertEquals(null, match.getWinner());

        player3.lost();
        match.checkPlayers();
        losersList.add(player3);

        assertEquals(losersList, match.getLosers());

        modifiedlist.remove(player3);
        assertEquals(modifiedlist, match.getPlayers());
        assertEquals(MatchState.FINISHED, match.getCurrentState());
        assertEquals(matchMaster, match.getWinner());
    }


    @Test
    void testNextTurn(){
        testAddingPlayers();
        assertEquals(matchMaster, match.getCurrentPlayer());
        assertEquals(PlayerState.ACTIVE, matchMaster.getPlayerState());
        assertEquals(PlayerState.INITIALIZED, player2.getPlayerState());
        assertEquals(PlayerState.INITIALIZED, player3.getPlayerState());

        matchMaster.setHasFinished();
        match.checkPlayers();
        assertEquals(player2, match.getCurrentPlayer());
        assertEquals(PlayerState.IDLE, matchMaster.getPlayerState());
        assertEquals(PlayerState.ACTIVE, player2.getPlayerState());
        assertEquals(PlayerState.INITIALIZED, player3.getPlayerState());

        player2.setHasFinished();
        match.checkPlayers();
        assertEquals(player3, match.getCurrentPlayer());
        assertEquals(PlayerState.IDLE, matchMaster.getPlayerState());
        assertEquals(PlayerState.IDLE, player2.getPlayerState());
        assertEquals(PlayerState.ACTIVE, player3.getPlayerState());

        player3.setHasFinished();
        match.checkPlayers();
        assertEquals(matchMaster, match.getCurrentPlayer());
        assertEquals(PlayerState.ACTIVE, matchMaster.getPlayerState());
        assertEquals(PlayerState.IDLE, player2.getPlayerState());
        assertEquals(PlayerState.IDLE, player3.getPlayerState());

        matchMaster.lost();
        match.checkPlayers();
        assertEquals(player2, match.getCurrentPlayer());
        assertEquals(PlayerState.LOST, matchMaster.getPlayerState());
        assertEquals(PlayerState.ACTIVE, player2.getPlayerState());
        assertEquals(PlayerState.IDLE, player3.getPlayerState());

        player3.lost();
        match.checkPlayers();
        assertEquals(player2, match.getCurrentPlayer());
        assertEquals(PlayerState.LOST, matchMaster.getPlayerState());
        assertEquals(PlayerState.WIN, player2.getPlayerState());
        assertEquals(PlayerState.LOST, player3.getPlayerState());

    }


    @Test
    void testNextState(){
        testAddingPlayers();
        assertEquals(MatchState.GETTING_PLAYERS_NUM, match.getCurrentState());
        match.nextState();
        assertEquals(MatchState.WAITING_FOR_PLAYERS, match.getCurrentState());
        match.nextState();
        assertEquals(MatchState.SELECTING_GOD_CARDS, match.getCurrentState());
        match.nextState();
        assertEquals(MatchState.SELECTING_SPECIAL_COMMAND, match.getCurrentState());
        match.nextState();
        assertEquals(MatchState.PLACING_WORKERS, match.getCurrentState());
        match.nextState();
        assertEquals(MatchState.RUNNING, match.getCurrentState());
        match.nextState();
        assertEquals(MatchState.FINISHED, match.getCurrentState());
    }



}