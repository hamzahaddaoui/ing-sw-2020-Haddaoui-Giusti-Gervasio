package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Observable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main model class: represents the facade of the entire model package
 * All user-useful model classes are accessible from the methods listed here.
 */

public class GameModel extends Observable<MessageEvent> {
    private static int progressivePlayerID;
    private static int progressiveMatchID;
    private static final Map<Integer, Player> initializedPlayers = new HashMap<>();
    private static final LinkedList<Player> playersWaitingList = new LinkedList<>();
    private static final Map<Integer, Match> activeMatches = new HashMap<>(); //id match, match

    /*
    -------------------------------------------------------------------------------
    -----------------------MULTIPLAYER MANAGEMENT----------------------------------
    -------------------------------------------------------------------------------
     */

    private GameModel(){}

    public static int getPlayersWaitingListSize(){
        return playersWaitingList.size();
    }

    public static int getNotInitMatchesListSize(){
            return (int) activeMatches
                    .keySet()
                    .stream()
                    .map(activeMatches::get)
                    .filter(match -> !match.isStarted() && (match.getPlayersNum() == 0))
                    .count();
    }

    public static int getInitMatchesListSize(){
        return (int) activeMatches
                .keySet()
                .stream()
                .map(activeMatches::get)
                .filter(match -> !match.isStarted() && (match.getPlayersNum() != 0))
                .count();
    }

    public static int getInitMatchID(){
        if (getInitMatchesListSize() != 0)
            return activeMatches
                    .keySet()
                    .stream()
                    .map(activeMatches::get)
                    .filter(match -> !match.isStarted() && (match.getPlayersNum() != 0))
                    .findFirst()
                    .get()
                    .getID();
        else
            return 0;
    }

    public static int createPlayer(String nickname){
        Player player;
        if (!isNickAvailable(nickname))
            return 0;

        progressivePlayerID++;
        player = new Player(progressivePlayerID, nickname);
        initializedPlayers.put(progressivePlayerID, player);
        return progressivePlayerID;
    }

    /**
     * Creates a new instance of match, with an assigned unique ID
     */
    public synchronized static int createMatch(Integer playerID){
        try{
            progressiveMatchID++;
            activeMatches.put(progressiveMatchID,
                    new Match(progressiveMatchID, initializedPlayers.remove(playerID)));
                    return progressiveMatchID;
        }
        catch (NullPointerException e){
            progressiveMatchID--;
            return 0;
        }
    }

    /**
     * Add a player to the "waiting to start" match
     *
     * @param playerID the ID of the player associated with that nickname and the waiting to start match
     */
    public static void addPlayerToMatch(Integer matchID, Integer playerID){
        translateMatchID(matchID).addPlayer(initializedPlayers.remove(playerID));
    }

    public static void addPlayerToWaitingList(Integer playerID){
        playersWaitingList.addLast(initializedPlayers.remove(playerID));
    }

    /**
     * Changes the number of the players of the "waiting to start" match
     *
     * @param playerNum the number of players wanted for the "waiting to start" match
     */
    public static void setMatchPlayersNum(Integer matchID, int playerNum){
        activeMatches.get(matchID).setPlayersNum(playerNum);
    }

    public static int unstackPlayer(){
        Player player = playersWaitingList.removeFirst();
        initializedPlayers.put(player.getID(), player);
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
        activeMatches.get(matchID).start();
    }


    /*
    -------------------------------------------------------------------------------
    -----------------------MATCH MANAGEMENT----------------------------------------
    -------------------------------------------------------------------------------
     */

    public static Set<String> getGameCards(){
        return Arrays.stream(GodCards.values()).map(Enum::toString).collect(Collectors.toSet());
    }

    public static MatchState getMatchState(Integer matchID){
        try{
            return translateMatchID(matchID).getCurrentState();
        }
        catch (NullPointerException exception){
            return MatchState.NONE;
        }
    }

    public static void nextMatchState(int matchID){
        translateMatchID(matchID).nextState();
    }

    public static PlayerState getPlayerState(int matchID, int playerID){
        try{
            return translatePlayerID(translateMatchID(matchID), playerID).getPlayerState();
        }
        catch (NullPointerException exception){
            return null;
        }
    }

    public static TurnState getPlayerTurn(int matchID, int playerID){
        try{
            return translatePlayerID(translateMatchID(matchID), playerID).getTurnState();
        }
        catch (NullPointerException exception){
            return null;
        }
    }

    /**
     * Make the next player gain the control of the match, by passing the turn
     *
     * @param matchID selected match
     */
    public static void nextMatchTurn(int matchID){
        try{
            translateMatchID(matchID).nextTurn();
        }
        catch (NullPointerException ignored){}
    }

    public static int getMatchWinner(int matchID){
        if (getMatchState(matchID) == MatchState.FINISHED) {
            return translateMatchID(matchID).getWinner().getID();
        }
        else return 0;
    }

    public static void deleteMatch(int matchID){
        activeMatches.remove(matchID);
    }

    public static void removeLosers(int matchID){
        translateMatchID(matchID).resetLosers();
    }


    /*
    -------------------------------------------------------------------------------
    -----------------------CARDS MANAGEMENT----------------------------------------
    -------------------------------------------------------------------------------
     */
    public synchronized static void setMatchCards(Integer matchID, Set<String> cards){
        Set<GodCards> godCards;
        Match match = translateMatchID(matchID);

         godCards = cards
                .stream()
                .map(GodCards::valueOf)
                .collect(Collectors.toSet());

        match.setCards(godCards);
    }

    /**
     *  Link the current player of the selected match, to the specified GodCard.
     *
     * @param matchID selected match
     * @param card special card selected by the current user
     */
    public synchronized static void selectPlayerCard(Integer matchID, String card){
        translateMatchID(matchID).getCurrentPlayer().setCommands(GodCards.valueOf(card));
    }

    public static boolean hasSelectedCard(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().hasSelectedCard();
    }


    /*
    -------------------------------------------------------------------------------
    -----------------------PLACING MANAGEMENT--------------------------------------
    -------------------------------------------------------------------------------
     */
    public static void placeWorker(Integer matchID, Position position){
        translateMatchID(matchID).getCurrentPlayer().setWorker(position);
    }

    public static boolean hasPlacedWorkers(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().hasPlacedWorkers();
    }

    /*
    -------------------------------------------------------------------------------
    -----------------------GAME MANAGEMENT-----------------------------------------
    -------------------------------------------------------------------------------
     */
    public static int getCurrentPlayer(int matchID){
        return translateMatchID(matchID).getCurrentPlayer().getID();
    }

    public static boolean isTerminateTurnAvailable(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().isTerminateTurnAvailable();
    }

    public static Map<Position, Boolean>  isSpecialFunctionAvailable(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().isSpecialFunctionAvailable();
    }

    public static void setUnsetSpecialFunction(Integer matchID, boolean specialFunction){
        translateMatchID(matchID).getCurrentPlayer().setUnsetSpecialFunction(specialFunction);
    }

    public static void setHasFinished(Integer matchID){
        translateMatchID(matchID).getCurrentPlayer().setHasFinished();
        translateMatchID(matchID).nextTurn();
    }

    public static void playerTurn(Integer matchID, Position startPosition, Position endPosition){
        Match match = translateMatchID(matchID);
        Player player = match.getCurrentPlayer();

        if (!player.hasSelectedWorker())
            player.setCurrentWorker(startPosition);
        player.playerAction(endPosition);

        match.checkPlayers();
    }

    /*
    -------------------------------------------------------------------------------
    -----------------------VIEW FUNCTIONS-----------------------------------------
    -------------------------------------------------------------------------------
     */

    public static Map<Integer, String> getMatchLosers(Integer matchID){
        return translateMatchID(matchID).getLosers().stream()
                .collect(Collectors.toMap(Player::getID, Player::toString));
    }

    public static Map<Integer, String> getMatchPlayers(Integer matchID){
        return translateMatchID(matchID).getAllPlayers().stream()
                .collect(Collectors.toMap(Player::getID, Player::toString));
    }

    public static Map<Position, Cell> getBillboardStatus (Integer matchID){
        return translateMatchID(matchID).getBillboard().getCells();
    }

    /**
     * Make a copy of the state of the billboard, made of 3 layers:
     * first layer for the height of the buildings
     * second layer for the players (each player has a different number)
     * third layer for the domes
     */
    public static Set<String>  getMatchCards(Integer matchID){
        return translateMatchID(matchID)
                .getCards()
                .stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());
    }

    /*public static Set<Position> getWorkersPosition(Integer matchID){
        return translateMatchID(matchID)
                .getCurrentPlayer()
                .getWorkers()
                .stream()
                .map(Worker::getPosition)
                .collect(Collectors.toSet());
    }*/

    public static Map<Position, Set<Position>> getWorkersAvailableCells(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().getWorkersAvailableCells();
    }

    public static Set<Position> getPlacingAvailableCells(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().getPlacingAvailableCells();
    }

    /*public static int getPlayersConnected(){
        return progressivePlayerID;
    }

    public static int getActiveMatches(){
        return progressiveMatchID;
    }*/

    /*
    -------------------------------------------------------------------------------
    -----------------------PRIVATE FUNCTIONS-----------------------------------------
    -------------------------------------------------------------------------------
     */


    /**
     * Translates the matchID to the related instance of match
     * @param matchID id of the match whose instance is requested
     * @return the instance of match related to the matchID
     */
    private static Match translateMatchID (int matchID){
        return activeMatches
                .get(matchID);
    }

    /**
     * Translates the playerID to the related instance of player
     * @param match instance of match joined by the player
     * @param playerID id of the player whose instance is requested
     * @return the instance of player related to the playerID
     */
    private static Player translatePlayerID (Match match, int playerID){
        if (match == null)
            return playersWaitingList
                    .stream()
                    .filter(player -> player.getID() == playerID)
                    .findAny()
                    .get();
        else
            return match
                .getAllPlayers()
                .stream()
                .filter(player -> player.getID()==playerID)
                .findAny().get();
    }

    /**
     * Checks if the match waiting to start has a certain nickname available.
     *
     * @param nickname contains the nickname choosen by the user
     * @return false if the nickname is not available, true otherwise
     */
    private static boolean isNickAvailable(String nickname){
        return playersWaitingList.stream()
                       .noneMatch(player -> player.toString().equals(nickname))
               &&
               activeMatches
                       .keySet()
                       .stream()
                       .map(activeMatches::get)
                       .filter(match -> !match.isStarted())
                       .noneMatch(match -> match.getPlayers()
                               .stream().anyMatch(player -> player.toString()
                                       .equals(nickname)));
    }

}

