package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    Match match = new Match (2);

    @Test
    void isDeckFull() {
        Set<GodCards> list = new HashSet<>();

        assertEquals(false, match.isDeckFull());
        assertTrue(match.getCards().isEmpty());

        list.add(GodCards.Apollo);
        match.addCard(GodCards.Apollo);
        assertEquals(false, match.isDeckFull());
        assertEquals(list, match.getCards());

        list.add(GodCards.Apollo);
        match.addCard(GodCards.Demeter);
        list.add(GodCards.Demeter);
        assertEquals(true, match.isDeckFull());
        assertEquals(list, match.getCards());


        match.removeCard(GodCards.Apollo);
        assertEquals(false, match.isDeckFull());
        match.removeCard(GodCards.Demeter);
        assertEquals(false, match.isDeckFull());
        assertTrue(match.getCards().isEmpty());

        match.setPlayersNum(3);
        match.addCard(GodCards.Apollo);
        assertEquals(false, match.isDeckFull());
        match.addCard(GodCards.Athena);
        assertEquals(false, match.isDeckFull());
        match.addCard(GodCards.Demeter);
        assertEquals(true, match.isDeckFull());
        list.add(GodCards.Athena);
        assertEquals(list, match.getCards());

    }


    @Test
    void addPlayer() {
        ArrayList<Player> playerSet = new ArrayList<>();

        //SITUAZIONE INIZIALE. NESSUN GIOCATORE, GIOCO DA 2
        assertTrue(match.getAllPlayers().isEmpty());
        assertEquals(2, match.getPlayersNum());
        assertEquals(0, match.getPlayersCurrentCount());

        //AGGIUNGO UN GIOCATORE
        Player player = new Player(230,"Hamza", match);
        playerSet.add(player);
        match.addPlayer(player);
        assertEquals(playerSet, match.getAllPlayers());
        assertEquals(1, match.getPlayersCurrentCount());
        assertEquals(false, match.isNumReached());

        //AGGIUNGO LO STESSO GIOCATORE GIA' INSERITO (non dovrebbe cambiare nulla)
        match.addPlayer(player);
        assertEquals(playerSet, match.getAllPlayers());
        assertEquals(1, match.getPlayersCurrentCount());
        assertEquals(false, match.isNumReached());

        //aggiungo un secondo giocatore. raggiungo il  numero richiesto
        player = new Player(2,"Leo", match);
        playerSet.add(player);
        match.addPlayer(player);
        assertEquals(playerSet, match.getAllPlayers());
        assertEquals(2, match.getPlayersCurrentCount());
        assertEquals(true, match.isNumReached());

        //aggiungo un terzo giocatore. non dovrebbe cambiare nulla
        player = new Player(2,"Dario", match);
        match.addPlayer(player);
        assertEquals(playerSet, match.getAllPlayers());
        assertEquals(2, match.getPlayersCurrentCount());
        assertEquals(true, match.isNumReached());

        //cambio il numero di giocatori
        //aggiungo un secondo giocatore. raggiungo il  numero richiesto
        match.setPlayersNum(3);
        assertEquals(3, match.getPlayersNum());
        assertEquals(2, match.getPlayersCurrentCount());
        assertEquals(false, match.isNumReached());

        //aggiungo un terzo giocatore. ora dovrebbe essere aggiunto
        player = new Player(24,"Dario", match);
        playerSet.add(player);
        match.addPlayer(player);
        assertEquals(playerSet, match.getAllPlayers());
        assertEquals(3, match.getPlayersCurrentCount());
        assertEquals(true, match.isNumReached());

    }



    @Test
    void nextTurn() {
        ArrayList<Player> playerSet = new ArrayList<>();
        Player Dario, Hamza, Leo;

        match.setPlayersNum(3);

        Dario = new Player(24,"Dario", match);
        playerSet.add(Dario);
        match.addPlayer(Dario);
        assertEquals(Dario, match.getCurrentPlayer());
        Leo = new Player(242,"Leo", match);
        playerSet.add(Leo);
        match.addPlayer(Leo);
        assertEquals(Leo, match.getCurrentPlayer());
        Hamza = new Player(14,"Hamza", match);
        playerSet.add(Hamza);
        match.addPlayer(Hamza);
        assertEquals(Hamza, match.getCurrentPlayer());

        match.nextTurn();
        assertEquals(Dario, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(Leo, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(Hamza, match.getCurrentPlayer());
        match.nextTurn();
        assertEquals(Dario, match.getCurrentPlayer());
    }
}