package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Observable;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Main model class: represents the facade of the entire model package
 * All user-useful model classes are accessible from the methods listed here.
 */

public class GameModel extends Observable {
    private static int progressiveMatchID;
    private static LinkedList<Player> playersWaitingList = new LinkedList<>();
    private static Map<Integer, Match> activeMatches = new HashMap<>(); //id match, match

    public static boolean isPlayersWaitingListEmpty(){
        return playersWaitingList.isEmpty();
    }

    public static boolean isNewMatchinstantiable(){
        return playersWaitingList.size()
               >= 2*(activeMatches
                .keySet()
                .stream()
                .map(matchID -> activeMatches.get(matchID))
                .filter(match -> !match.isStarted() && (match.getPlayersNum()==null))
                .count());
    }

    public static Integer getAvailableMatchID(){
        return activeMatches
                .keySet()
                .stream()
                .map(matchID -> activeMatches.get(matchID))
                .filter(match -> !match.isStarted() && (match.getPlayersNum()!=null))
                .findFirst()
                .get()
                .getID();
    }

    public static boolean isNotInitializedMatchPresent(){
        return activeMatches
                .keySet()
                .stream()
                .map(matchID -> activeMatches.get(matchID))
                .filter(match -> !match.isStarted() && (match.getPlayersNum()==null))
                .findAny()
                .isPresent();
    }

    public static void playerAddToWaitingList(Integer playerID, String nickname){
        playersWaitingList.addLast(new Player(playerID, nickname));
    }

    /**
     * Creates a new instance of match, with an assigned unique ID
     */
    public synchronized static int createMatch(){
        progressiveMatchID++;
        activeMatches.put(progressiveMatchID, new Match(progressiveMatchID));
        return progressiveMatchID;
    }

    /**
     * Checks if the match waiting to start has a certain nickname available.
     *
     * @param nickname contains the nickname choosen by the user
     * @return false if the nickname is not available, true otherwise
     */
    public static boolean isNickAvailable(String nickname){
        return !playersWaitingList
                .stream()
                .filter(player -> player.getNickname() == nickname)
                .findAny()
                .isPresent()
                &&
               !activeMatches
                .keySet()
                .stream()
                .map(matchID -> activeMatches.get(matchID))
                .filter(match -> !match.isStarted())
                .filter(match -> match.getPlayers()
                        .stream().filter(player -> player.getNickname() == nickname)
                        .findAny()
                        .isPresent())
                .findAny()
                .isPresent();
    }

    public static List<Integer> getMatchPlayers(Integer matchID){
        return activeMatches
                .get(matchID)
                .getPlayers()
                .stream()
                .map(player -> player.getID())
                .collect(Collectors.toList());
    }

    /**
     * Changes the number of the players of the "waiting to start" match
     *
     * @param playerNum the number of players wanted for the "waiting to start" match
     */
    public synchronized static void setMatchPlayersNum(Integer matchID, int playerNum){
        activeMatches.get(matchID).setPlayersNum(playerNum);
    }

    /**
     * Add a player to the "waiting to start" match
     *
     * @param playerID the ID of the player associated with that nickname and the waiting to start match
     * @param nickname the nickname of the player
     */
    public static void addPlayer(Integer matchID, Integer playerID, String nickname){
        translateMatchID(matchID).addPlayer(new Player(playerID, nickname));
    }

    private static void addPlayer(Integer matchID, Player player){
        translateMatchID(matchID).addPlayer(player);
    }

    public static String getMatchState(Integer matchID){
        return activeMatches.get(matchID).getCurrentState().toString();
    }

    public static Integer unstackPlayerToMatch(Integer matchID){
        Player player = playersWaitingList.removeFirst();
        addPlayer(matchID, player);
        return player.getID();
    }

    /**
     * Checks if the required number of the players for the starting match is reached
     *
     * @return true if the number of players is reached
     */
    public static boolean isNumReached(Integer matchID){
        return activeMatches.get(matchID).isNumReached();
    }

    /**
     * Starts the "waiting to start" match
     */
    public synchronized static void startMatch(Integer matchID){
        activeMatches.get(matchID).matchStart();
    }




    /**
     * Add (or removes) a specific card to the match deck
     *
     * @param matchID specified matchID
     * @param card special card selected by the current user
     */
    public synchronized static void addRemoveCardToMatchDeck(Integer matchID, String card){
        Match match = translateMatchID(matchID);
        GodCards godCard = GodCards.valueOf(card);
        if (match.getCards().contains(godCard))
            match.removeCard(godCard);
        else
            match.addCard(godCard);
    }

    /**
     * Checks if the deck of the match is full
     *
     * @param matchID specified matchID
     * @return true if the deck is full, false otherwise
     */
    public synchronized static boolean isMatchDeckFull(Integer matchID){
        return translateMatchID(matchID).isDeckFull();
    }

    /**
     * This method returns the playerID of the current player of the given match
     *
     * @param matchID specified matchID
     * @return the current player of the specified match
     */
    public synchronized static int getCurrentPlayerID(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().getID();
    }

    /**
     * Changes the current worker of the currentPlayer on the specified match
     *
     * @param matchID specified matchID
     * @param position the position of the worker chosen by the player
     */
    /*public synchronized static void setCurrentPlayerWorker(Integer matchID, Position position){
        translateMatchID(matchID).getCurrentPlayer().setCurrentWorker(position);
    }*/

    /**
     *  Link the current player of the selected match, to the specified GodCard.
     *
     * @param matchID selected match
     * @param card special card selected by the current user
     */
    public static void selectPlayerCard(Integer matchID, String card){
        Match match = translateMatchID(matchID);
        GodCards godCard = GodCards.valueOf(card);
        match.getCurrentPlayer().setCommands(godCard);
    }

    /**
     * Get available cells for move/build related to the given player,
     *
     * @param matchID selected match
     * @param playerID player liked to the match
     *
     * @return the list of cells on the billboard, where the player could move
     */
    /*public static Set<Position> getAvailableCells(Integer matchID, Integer playerID){
        Player player = translatePlayerID(translateMatchID(matchID), playerID);
    }*/

    public static void placeWorker(Integer matchID, Integer playerID, Position position){
        Player player = translatePlayerID(translateMatchID(matchID), playerID);
        player.setWorker(position);

    }

    public static void setUnsetSpecialFunction(Integer matchID, Integer playerID){
        Player player =  translatePlayerID(translateMatchID(matchID), playerID);
        player.setUnsetSpecialFunction();
    }

    public static void playerTurn(Integer matchID, Integer playerID, Position initPosition, Position finalPosition){
        Player player = translatePlayerID(translateMatchID(matchID), playerID);
        player.setCurrentWorker(initPosition);
        player.playerAction(finalPosition);
    }

    /**
     * Make the next player gain the control of the match, by passing the turn
     *
     * @param matchID selected match
     */
    public static void nextMatchTurn(Integer matchID){
        translateMatchID(matchID).nextTurn();
    }

    public static boolean playerHasPlacedWorkers(Integer matchID, Integer playerID){
        return translatePlayerID(translateMatchID(matchID), playerID).hasPlacedWorkers();
    }

    public static boolean playerHasFinished(Integer matchID, Integer playerID){
        return translatePlayerID(translateMatchID(matchID), playerID).hasFinished();
    }

    /**
     * Make a copy of the state of the billboard, made of 3 layers:
     * first layer for the height of the buildings
     * second layer for the players (each player has a different number)
     * third layer for the domes
     * @return
     */
    public void sendBillboardStatus(Integer matchID, Integer playerID){
        MessageEvent messageEvent;
        Map<Position, Cell> billboardStatus;
        Map<Position, Set<Position>> workersAvailableCells;


        Match match = translateMatchID(matchID);
        Billboard billboard = match.getBillboard();
        Player player = translatePlayerID(match, playerID);

        billboardStatus = billboard.getCells();

        if (match.getCurrentPlayer() == player) {
            player.setAvailableCells();
            workersAvailableCells = player.getAvailableCells();
        }
        else
            workersAvailableCells = null;

        messageEvent = new MessageEvent(MessageType.MODEL_VIEW_UPDATE, matchID, billboardStatus, workersAvailableCells);

        notify(messageEvent);
    }


    /**
     * Translates the matchID to the related instance of match
     * @param matchID id of the match whose instance is requested
     * @return the instance of match related to the matchID
     */
    protected static Match translateMatchID (Integer matchID){
        return activeMatches
                .get(matchID);
    }

    /**
     * Translates the playerID to the related instance of player
     * @param match instance of match joined by the player
     * @param playerID id of the player whose instance is requested
     * @return the instance of player related to the playerID
     */
    protected static Player translatePlayerID (Match match, Integer playerID){
        return match
                .getPlayers()
                .stream()
                .filter(player1 -> player1.getID()==playerID)
                .findAny().get();
    }
}

