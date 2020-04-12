package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author hamzahaddaoui
 *
 */
public class Match {
    private int ID;
    private Integer playersNum = null; //number of players of the match, 2 by default
    private int playersCurrentCount;

    private List<Player> players = new ArrayList<>(2);
    private Player currentPlayer;
    private Set<GodCards> cards = new HashSet<>(2);
    private Billboard billboard;
    private MatchState currentState;
    private Player winner;

    private boolean started;
    private boolean moveUpActive = true;
    private boolean finished;

    public Match(int matchID) {
        this.ID = matchID;
        billboard = new Billboard();
        currentState = MatchState.GETTING_PLAYERS_NUM;
    }

    public boolean setPlayersNum(int playersNum){
        this.playersNum = playersNum;
        return true;
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

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Set<GodCards> getCards() {
        return cards;
    }

    public Billboard getBillboard() {
        return billboard;
    }

    public MatchState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(MatchState currentState) {
        this.currentState = currentState;
    }

    public boolean isStarted() {
        return started;
    }

    public void addCard(GodCards card) {
        cards.add(card);
    }

    public void removeCard(GodCards card) {
        cards.remove(card);
    }

    public boolean isMoveUpActive() {
        return moveUpActive;
    }

    public void setMoveUpActive(boolean moveUpActive) {
        this.moveUpActive = moveUpActive;
    }

    public void setFinished(boolean finished) { this.finished = finished; }

    public boolean isFinished() { return finished; }

    public Player getWinner() { return winner; }

    public void setWinner(Player winner) { this.winner = winner; }

    public boolean isDeckFull() {
        if (cards.size() >= playersNum) return true;
        else return false;
    }

    public boolean isNumReached() {
        if (playersNum == playersCurrentCount)
            return true;
        else
            return false;
    }

    public void addPlayer(Player player) {
        if (playersCurrentCount >= playersNum || players.contains(player))
            return;
        players.add(player);
        currentPlayer = player;
        playersCurrentCount++;
    }

    public void matchStart() {
        started = true;
        finished = false;
    }

    public void nextTurn() {
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
    }
}