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
    private boolean moveUpActive = true;
    private Player winner;

    public Match(int matchID, Player matchMaster) {
        this.ID = matchID;
        billboard = new Billboard();
        currentState = MatchState.GETTING_PLAYERS_NUM;
        addPlayer(matchMaster);
        matchMaster.setPlayerState();
        currentPlayer = matchMaster;
    }

    public int getID() {
        return ID;
    }

    public Integer getPlayersNum() {
        return playersNum;
    }

    public boolean isNumReached() {
        return playersNum == playersCurrentCount;
    }

    public int getPlayersCurrentCount(){
        return playersCurrentCount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getLosers(){
        return losers;
    }

    public List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>(players);
        allPlayers.addAll(losers);
        return allPlayers;
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

    public boolean isStarted() {
        return started;
    }

    public boolean isMoveUpActive() {
        return moveUpActive;
    }




    public void resetLosers(){
        losers.clear();
    }

    public void setPlayersNum(int playersNum){
        this.playersNum = playersNum;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setMatch(this);
        playersCurrentCount++;
    }

    public void removePlayer(Player player){
        players.remove(player);

        player.lost();
        losers.add(player);


        playersCurrentCount--;
        if (playersCurrentCount == 1){
            currentPlayer = players.get(0);
            currentPlayer.win();
            winner = currentPlayer;
            currentState = MatchState.FINISHED;
            return;
        }
        else if(currentPlayer == player)
            nextTurn();

        billboard
                .getCells()
                .keySet()
                .stream()
                .filter(position -> billboard.getPlayer(position) != null && billboard.getPlayer(position) == player.getID())
                .forEach(billboard::resetPlayer);


    }

    public void checkPlayers(){
        players.stream()
                .filter(player -> player.getPlayerState() == PlayerState.LOST)
                .findAny()
                .ifPresent(this::removePlayer);

        Optional<Player> winPlayer = players.stream()
                .filter(player -> player.getPlayerState() == PlayerState.WIN)
                .findAny();


        if (winPlayer.isPresent()){
            List<Player> lost = new ArrayList<>(players);
            lost.stream()
                    .filter(player -> !player.equals(winPlayer.get()))
                    .forEach(this::removePlayer);

            winner = winPlayer.get();
            currentState = MatchState.FINISHED;


        }

        else if (currentPlayer != null && currentPlayer.hasFinished()){
            nextTurn();
        }
    }


    public void nextTurn() {
        if (currentState != MatchState.FINISHED) {
            if (currentPlayer.getPlayerState() == PlayerState.ACTIVE)
                currentPlayer.resetPlayerState();
            currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
            currentPlayer.setPlayerState();
        }
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

    public Player getWinner(){
        return winner;
    }
}