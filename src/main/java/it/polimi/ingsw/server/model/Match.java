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

    private boolean playerNumSetted;
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

    public String getPlayerNick(Integer playerID){
        return getAllPlayers().stream().filter(player -> player.getID() == playerID).findAny().get().toString();
    }

    public Integer getPlayersNum() {
        return playersNum;
    }

    public boolean isNumReached() {
        return playersNum == playersCurrentCount;
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

    public void setPlayersNum(int playersNum) throws IllegalArgumentException{
        if (!(playersNum == 2 || playersNum == 3))
            throw new IllegalArgumentException("players Num must be 2 or 3");
        playerNumSetted = true;
        this.playersNum = playersNum;
    }

    public void addPlayer(Player player) throws IllegalStateException {
        if (playerNumSetted && playersCurrentCount == playersNum)
            throw new IllegalStateException("Players num reached");
        else {
            players.add(player);
            player.setMatch(this);
            playersCurrentCount++;
        }
    }

    public void removePlayer(Player player) throws NoSuchElementException{
        if (!players.contains(player)) {
            throw new NoSuchElementException("Player not found");
        }

        player.lost();
        players.remove(player);
        playersCurrentCount--;
        losers.add(player);

        if (playersCurrentCount == 1){
            currentPlayer = players.get(0); //the remaining player is the winner
            currentPlayer.win();
            return;
        }
        else if(currentPlayer == player)    //if the deleted player was the current player
            nextTurn();

        //delete player workers from the billboard
        billboard
                .getCells()
                .keySet()
                .stream()
                .filter(position -> billboard.getPlayer(position) != 0 && billboard.getPlayer(position) == player.getID())
                .forEach(billboard::resetPlayer);
    }

    public boolean checkPlayers(){
        players.stream()
                .filter(player -> player.getPlayerState() == PlayerState.LOST)
                .findAny()
                .ifPresent(this::removePlayer);

        Optional<Player> winPlayer = players.stream()
                .filter(player -> player.getPlayerState() == PlayerState.WIN)
                .findAny();

        if (winPlayer.isPresent()){
            List<Player> lost = new ArrayList<>(players);
            if (playersCurrentCount != 1)
                lost.stream()
                    .filter(player -> !player.equals(winPlayer.get()))
                    .forEach(this::removePlayer);

            winner = winPlayer.get();
            currentState = MatchState.FINISHED;
            return true;
        }

        else if (winner == null && currentPlayer.hasFinished()){

            nextTurn();

        }
        return false;
    }

    public void nextTurn() throws UnsupportedOperationException{
        if (winner!=null || currentState == MatchState.FINISHED)
            throw new UnsupportedOperationException("Match is finished!");

        if (currentPlayer.getPlayerState() == PlayerState.ACTIVE){
            currentPlayer.resetPlayerState();
        }
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        currentPlayer.setPlayerState();
    }

    public void setCards(Set<GodCards> cards) throws IllegalArgumentException {
        if (cards.size() == playersNum)
            this.cards = cards;
        else
            throw new IllegalArgumentException("Cards not matching players num");
    }

    public void removeCard(GodCards card) throws NoSuchElementException {
        if(!cards.contains(card))
            throw new NoSuchElementException("Card is not in deck");
        else
            cards.remove(card);
    }

    public void nextState() throws IllegalStateException{
        if (currentState.equals(MatchState.FINISHED))
            throw new IllegalStateException("Match is finished");

        else if (currentState.equals(MatchState.PLACING_WORKERS)) {
            currentState = currentState.next();
            currentPlayer.setPlayerState();
        }

        else
            currentState = currentState.next();

    }

    public void start(){
        started = true;
    }

    public void setMoveUpActive(boolean moveUpActive) {
        this.moveUpActive = moveUpActive;
    }

    public Player getWinner(){
        return winner;
    }
}