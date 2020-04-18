package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.MatchState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author hamzahaddaoui
 *
 */
public class Match {
    private final int ID;
    private int playersNum; //number of players of the match
    private int playersCurrentCount; //current players of the match

    private final List<Player> players = new ArrayList<>(2);
    private Player currentPlayer;
    private Set<GodCards> cards = new HashSet<>(2);
    private final Billboard billboard;
    private MatchState currentState;
    private Player winner;

    private boolean started;
    private boolean moveUpActive = true;

    public Match(int matchID, Player matchMaster) {
        this.ID = matchID;
        billboard = new Billboard();
        currentState = MatchState.GETTING_PLAYERS_NUM;
        matchMaster.setPlayerState();
        addPlayer(matchMaster);
        currentPlayer = matchMaster;
    }

    public int getID() {
        return ID;
    }

    public Integer getPlayersNum() {
        return playersNum;
    }

    public int getPlayersCurrentCount() {
        return playersCurrentCount;
    }

    public boolean isNumReached() {
        return playersNum == playersCurrentCount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Set<GodCards> getCards() {
        return cards;
    }

    public boolean isDeckFull() {
        return cards.size() >= playersNum;
    }

    public Billboard getBillboard() {
        return billboard;
    }

    public MatchState getCurrentState() {
        return currentState;
    }

    public Player getWinner() { return winner; }

    public boolean isStarted() {
        return started;
    }

    public boolean isMoveUpActive() {
        return moveUpActive;
    }



    public void setPlayersNum(int playersNum){
        this.playersNum = playersNum;
    }

    public void addPlayer(Player player) {
        players.add(player);
        playersCurrentCount++;
    }

    public void removePlayer(Player player){
        players.remove(player);
        playersCurrentCount--;
    }

    public void nextTurn() {
        currentPlayer.resetPlayerState();
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        currentPlayer.setPlayerState();
    }

    public void setCards(Set<GodCards> cards) {
        this.cards = cards;
    }

    public void removeCard(GodCards card) {
        cards.remove(card);
    }

    public void nextState() {
        currentState = currentState.next();
    }

    public void setWinner(Player winner) { this.winner = winner; }

    public void start() {
        started = true;
    }

    public void setMoveUpActive(boolean moveUpActive) {
        this.moveUpActive = moveUpActive;
    }
}