package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.*;


/**
 * @author hamzahaddaoui
 *
 */
public class Match {
    private final int ID;
    private int playersNum; //number of players of the match
    private int playersCurrentCount; //current players of the match

    private final List<Player> players = new ArrayList<>(2);
    private final List<Player> losers = new ArrayList<>(2);
    private Player currentPlayer;
    private Set<GodCards> cards = new HashSet<>(2);
    private final Billboard billboard;
    private MatchState currentState;

    private boolean started;
    private boolean finished;
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

    public List<Player> getAllPlayers() {
        List<Player> allPlayers = players;
        allPlayers.addAll(losers);
        return allPlayers;
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
        player.lost();
        losers.add(player);
        players.remove(player);
        billboard
                .getCells()
                .keySet()
                .stream()
                .filter(position -> billboard.getPlayer(position) == player.getID())
                .forEach(billboard::resetPlayer);

        playersCurrentCount--;
    }

    public void checkPlayers(){
        players.stream()
                .filter(player -> player.getPlayerState() == PlayerState.WIN)
                .findAny()
                .ifPresent(winner -> players.stream()
                .filter(player -> player != winner)
                .forEach(this::removePlayer));

        players.stream()
                .filter(player -> player.getPlayerState() == PlayerState.LOST)
                .findAny()
                .ifPresent(this::removePlayer);

        if(playersCurrentCount == 1){
            finished = true;
        }

        else if (!players.contains(currentPlayer) || currentPlayer.hasFinished()){
            nextTurn();
        }
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

    public void start() {
        started = true;
    }

    public void setMoveUpActive(boolean moveUpActive) {
        this.moveUpActive = moveUpActive;
    }

    public boolean isFinished(){
        return finished;
    }
}