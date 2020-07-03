package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.*;


/**
 * @author hamzahaddaoui
 *
 * Class related to a match. Linked to a certain billboard, and a list of players.
 * The match moves through a list of predefined state, from the INITIALIZED to FINISH.
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

    private String info;

    private int index = 0;
    private Player winner;

    /**
     * Constructor that instantiates a new match; a game billboard is instantiated too.
     * The new match, is set on the GETTING_PLAYERS_NUM status
     * The player that has created the match, is added immediatly and set as the current player.
     * @param matchID progressive ID given to any existing match
     * @param matchMaster the player that has created the match
     */
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

    /**
     * This method is used to set the players number of the match.
     * @param playersNum the number of the players for the match. Can be either 2 or 3
     */
    public void setPlayersNum(int playersNum){
        playerNumSetted = true;
        this.playersNum = playersNum;
    }

    /**
     * This method is used to add a new player to the match.
     * The players counter is increased too.
     * @param player the new player being added to the match.
     */
    public void addPlayer(Player player)  {
        players.add(player);
        player.setMatch(this);
        playersCurrentCount++;
    }

    /**
     * This method is used to remove a certain player, that has lost, from the match.
     * At first, the player is being removed from the billboard, and then removed from the players list
     * This player then is added to the losers list. In fact, even if this player has lost, it keeps receiving updates from the match.
     * At last, if there is only one remaining player, this is declared winner.
     *  @param player the player that has lost
     */
    public void removePlayer(Player player) {
        billboard
                .getCells()
                .keySet()
                .stream()
                .filter(position -> billboard.getPlayer(position) == player.getID())
                .forEach(billboard::resetPlayer);

        player.lost();
        players.remove(player);
        playersCurrentCount--;
        losers.add(player);



        if (playersCurrentCount == 1){
            currentPlayer = players.get(0); //the remaining player is the winner
            currentPlayer.win();
        }
        else if(currentPlayer == player) {    //if the deleted player was the current player
            index--;
            nextTurn();
        }
        //delete player workers from the billboard


    }

    /**
     * This method is used to check the status of the match.
     * If a player state is LOST, this is removed.
     * Otherwise if a player state is WIN, this is declared winner, and the other players are declared losers.
     */
    public boolean checkPlayers(){
        boolean retVal = false;
        Optional<Player> lostPlayer = players.stream()
                .filter(player -> player.getPlayerState() == PlayerState.LOST)
                .findAny();

        if(lostPlayer.isPresent()){
            removePlayer(lostPlayer.get());
            retVal = true;
        }

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
            retVal = true;
        }

        else if (winner == null && currentPlayer.hasFinished()){
            nextTurn();
        }

        return retVal;
    }

    /**
     * Changes the current player.
     */
    public void nextTurn() {
        if (currentPlayer.getPlayerState() == PlayerState.ACTIVE){
            currentPlayer.resetPlayerState();
        }
        index = (index + 1) % playersCurrentCount;
        currentPlayer = players.get(index);
        currentPlayer.setPlayerState();
    }

    public void setCards(Set<GodCards> cards) {
        this.cards = cards;
    }

    public void removeCard(GodCards card) {
        cards.remove(card);
    }

    /**
     * This method is used to change the current state of the match.
     * The match moves sequentially through the states described here:
     * GETTING_PLAYERS_NUM -> WAITING_FOR_PLAYERS -> SELECTING_GOD_CARDS -> SELECTING_SPECIAL_COMMAND -> PLACING_WORKERS -> RUNNING -> FINISHED
     */
    public void nextState() {
        if (currentState.equals(MatchState.SELECTING_SPECIAL_COMMAND)){
            //commentare per test!
            //Collections.shuffle(players);
            //nextTurn();
            currentState = currentState.next();
        }
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

    public String getInfo(){
        return info;
    }

    public void setInfo(String info){
        this.info = info;
    }
}