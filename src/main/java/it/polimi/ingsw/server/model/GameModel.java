package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Message;

import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.Position;
import java.util.*;
import java.util.stream.Collectors;


public class GameModel extends Observable {
    private static Observer<Message> observer;
    private static NavigableMap<Integer, Match> activeMatches = new TreeMap<>(); //id match, match

    public static Match translateMatchID (Integer matchID){
        return activeMatches
                .get(matchID);
    }

    public static Player translatePlayerID (Match match, Integer playerID){
        return match
                .getPlayers()
                .stream()
                .filter(player1 -> player1.getID()==playerID)
                .collect(Collectors.toList()).get(0);
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
     * @return false if the nickname is not available, true in any other case
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
     * @return a boolean value which indicates if the number of players is reached
     */
    public static boolean addPlayer(Integer playerID, String nickname){
        Match match = activeMatches.get(activeMatches.lastKey());
        match.addPlayer(new Player(playerID, nickname, match));
        return match.isNumReached();
    }

    /**
     * Creates a new instance of match, with the specified number of player,
     * with an assigned unique ID
     * @param playerNum number of players of the match
     */
    public static void createMatch(int playerNum){
        int matchID = 0;
        if (activeMatches.lastKey() != null) matchID = activeMatches.lastKey() + 1;
        activeMatches.put(matchID, new Match(activeMatches.lastKey(), playerNum));
    }

    /**
     * Add (or removes) a specific card to the match deck,
     * according to the number of players of the match
     *
     * @param matchID specified matchID
     * @param card special card selected by the current user
     * @return true if the cards deck is full (cards number is equal to players number)
     */
    public static boolean addRemoveCardToMatch(Integer matchID, String card){
        Match match = translateMatchID(matchID);
        GodCards godCard = GodCards.valueOf(card);
        if (match.getCards().contains(godCard)) match.removeCard(godCard);
        else match.addCard(godCard);

        if (match.isDeckFull()){
            match.nextTurn();
        }

        return match.isDeckFull();
    }

    /**
     * This method returns the playerID of the current player of the given match
     *
     * @param matchID specified matchID
     * @return the current player of the specified match
     */
    public int getCurrentPlayerID(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().getID();
    }

    public static void setCurrentPlayerWorker(Integer matchID, Integer playerID, int worker){
        translatePlayerID(translateMatchID(matchID), playerID).setCurrentWorkerID(worker);
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
    public static List<Position> getAvailableCells(Integer matchID, Integer playerID){
        Match match = translateMatchID(matchID);
        Player player = translatePlayerID(match, playerID);
        return player.Commands().getAvailableCells(player);
    }

    public static void setUnsetSpecialFunction(Integer matchID, Integer playerID, int worker){
        Player player =  translatePlayerID(translateMatchID(matchID), playerID);
        player.Commands().specialFunctionSetUnset(player);
    }

    public static void playerMoveBuild(Integer matchID, Integer playerID, Position position){
        translatePlayerID(translateMatchID(matchID), playerID).playerTurn(position);
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
