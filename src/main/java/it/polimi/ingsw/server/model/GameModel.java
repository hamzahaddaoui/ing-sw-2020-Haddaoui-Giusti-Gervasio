package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Message;

import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.Position;
import java.util.*;

/**
 * @author hamzahaddaoui
 * Main model class: represents the facade of the entire model package
 * All user-useful model classes are accessible from the methods listed here.
 */

public class GameModel extends Observable {
    //list of observers of the model state
    private static Observer<Message> observer;

    //navigableMap to get the last inserted element, which could correspond to the waiting to start watch
    //Made up of an integer, which represents the matchID, and the match entity.
    private static NavigableMap<Integer, Match> activeMatches = new TreeMap<>(); //id match, match
    //private static Map<Integer, Match> waitingToStart;  //non è creato il match

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

    /**
     * Checks if there is an instance of match, waiting to start
     * and returns that match to the caller.
     *
     * @return the ID of the waiting to start match. -1 in any other case
     */
    public static int getAvailableMatchID(){
        Match match;
        match = activeMatches.get(activeMatches.lastKey());

        if (!match.isStarted()) {
            return activeMatches.lastKey();
        }
        else {
            return -1;
        }
    }

    /**
     * Checks if the match waiting to start has a certain nickname available.
     *
     * @param nickname contains the nickname choosen by the user
     * @return false if the nickname is not available, true otherwise
     */
    public static boolean isNickAvailable(String nickname){
        if (getAvailableMatchID() != -1){
            Optional<Player> result = activeMatches
                    .get(activeMatches.lastKey())
                    .getPlayers()
                    .stream()
                    .filter(player1 -> player1.getNickname()==nickname)
                    .findAny();
            if (result.isPresent()) return false;
        }
        return true;
    }

    /**
     * Add a player to the "waiting to start" match
     *
     * @param playerID the ID of the player associated with that nickname and the waiting to start match
     * @param nickname the nickname of the player
     */
    public static void addPlayer(Integer playerID, String nickname){
        Match match = activeMatches.get(activeMatches.lastKey());
        match.addPlayer(new Player(playerID, nickname, match));
    }

    /**
     * Checks if the required number of the players for the starting match is reached
     *
     * @return true if the number of players is reached
     */
    public static boolean isNumReached(){
        return activeMatches.get(activeMatches.lastKey()).isNumReached();
    }

    /**
     * Creates a new instance of match, with an assigned unique ID
     */
    public synchronized static void createMatch(){
        Match match;
        int matchID = 0;
        if (activeMatches.lastKey() != null)
            matchID = activeMatches.lastKey() + 1;
        activeMatches.put(matchID, new Match(activeMatches.lastKey()));
    }

    /**
     * Changes the number of the players of the "waiting to start" match
     *
     * @param playerNum the number of players wanted for the "waiting to start" match
     */
    public synchronized static void setMatchPlayersNum(int playerNum){
        activeMatches.get(activeMatches.lastKey()).setPlayersNum(playerNum);
    }

    /**
     * Starts the "waiting to start" match
     */
    public synchronized static void startMatch(){
        activeMatches.get(activeMatches.lastKey()).matchStart();
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
    public synchronized static void setCurrentPlayerWorker(Integer matchID, Position position){
        translateMatchID(matchID).getCurrentPlayer().setCurrentWorker(position);
    }

    /**
     * Make the next player gain the control of the match, by passing the turn
     *
     * @param matchID selected match
     */
    public static void nextMatchTurn(Integer matchID){
        translateMatchID(matchID).nextTurn();
    }

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
    public static Set<Position> getAvailableCells(Integer matchID, Integer playerID){
        Player player = translatePlayerID(translateMatchID(matchID), playerID);
        player.setAvailableCells(player.getCurrentWorker());
        return player.getAvailableCells();
    }

    public static void placeWorker(Integer matchID, Integer playerID, Position position){
        Player player = translatePlayerID(translateMatchID(matchID), playerID);

    }

    public static void setUnsetSpecialFunction(Integer matchID, Integer playerID, int worker){
        Player player =  translatePlayerID(translateMatchID(matchID), playerID);
        //player.specialFunctionSetUnset();
    }

    public static boolean playerTurn(Integer matchID, Integer playerID, Position position){
        Player player = translatePlayerID(translateMatchID(matchID), playerID);
        player.playerAction(position);
        return player.hasFinished();
    }

    public static boolean playerHasPlacedWorkers(Integer matchID, Integer playerID){
        return translatePlayerID(translateMatchID(matchID), playerID).hasPlacedWorkers();
    }


    /**
     * Make a copy of the state of the billboard, made of 3 layers:
     * first layer for the height of the buildings
     * second layer for the players (each player has a different number)
     * third layer for the domes
     * @return
     */
    public static int[][][] getBillboard(){

        return null;
    }

}

