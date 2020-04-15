package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Main model class: represents the facade of the entire model package
 * All user-useful model classes are accessible from the methods listed here.
 */

public class GameModel extends Observable<MessageEvent> implements Observer<MessageEvent> {
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

    public static int getPlayersWaitingListSize(){
        return playersWaitingList.size();
    }

    public static int getNotInitMatchesListSize(){
        return (int) activeMatches
                .keySet()
                .stream()
                .map(activeMatches::get)
                .filter(match -> match.isStarted() && (match.getPlayersNum() == null))
                .count();
    }

    public static int getInitMatchesListSize(){
        return (int) activeMatches
                .keySet()
                .stream()
                .map(activeMatches::get)
                .filter(match -> match.isStarted() && (match.getPlayersNum() != null))
                .count();
    }

    /**
     * Checks if the match waiting to start has a certain nickname available.
     *
     * @param nickname contains the nickname choosen by the user
     * @return false if the nickname is not available, true otherwise
     */
    public static boolean isNickAvailable(String nickname){
        return playersWaitingList
                .stream()
                .noneMatch(player -> player.getNickname().equals(nickname))
               &&
               activeMatches
                       .keySet()
                       .stream()
                       .map(activeMatches::get)
                       .filter(match -> match.isStarted())
                       .noneMatch(match -> match.getPlayers()
                               .stream().anyMatch(player -> player.getNickname().equals(nickname)));
    }

    public static Integer getInitMatchID(){
        if (getInitMatchesListSize() != 0)
            return activeMatches
                    .keySet()
                    .stream()
                    .map(activeMatches::get)
                    .filter(match -> match.isStarted() && (match.getPlayersNum() != null))
                    .findFirst()
                    .get()
                    .getID();
        else
            return null;
    }

    public static int createPlayer(String nickname){
        Player player;
        progressivePlayerID++;
        player = new Player(progressivePlayerID, nickname);
        initializedPlayers.put(progressivePlayerID, player);
        return progressivePlayerID;
    }

    public static void removeInitPlayer(Integer playerID){
        initializedPlayers.remove(playerID);
    }

    /**
     * Creates a new instance of match, with an assigned unique ID
     */
    public synchronized static int createMatch(Integer playerID){
        progressiveMatchID++;

        activeMatches.put(progressiveMatchID,
                new Match(progressiveMatchID, initializedPlayers.remove(playerID)));
        return progressiveMatchID;
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

    public static Integer unstackPlayer(){
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


    public static String getMatchState(Integer matchID){
        try{
            return translateMatchID(matchID).getCurrentState().toString();
        }
        catch (NullPointerException exception){
            return null;
        }
    }

    public static void nextMatchState(Integer matchID){
        translateMatchID(matchID).nextState();
    }

    public static String getPlayerState(Integer matchID, Integer playerID){
        try{
            return translatePlayerID(translateMatchID(matchID), playerID).getPlayerState().toString();
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
    public static void nextMatchTurn(Integer matchID){
        translateMatchID(matchID).nextTurn();
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
    public static void selectPlayerCard(Integer matchID, String card){
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
    public static void setUnsetSpecialFunction(Integer matchID, boolean specialFunction){
        translateMatchID(matchID).getCurrentPlayer().setUnsetSpecialFunction(specialFunction);
    }

    public static void setHasFinished(Integer matchID){
        translateMatchID(matchID).getCurrentPlayer().setHasFinished();
    }

    public static void playerTurn(Integer matchID, Position startPosition, Position endPosition){
        Player player = translateMatchID(matchID).getCurrentPlayer();
        player.setCurrentWorker(startPosition);
        player.playerAction(endPosition);
    }

    public static String getCurrentPlayerState(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().getPlayerState().toString();
    }


    /*
    -------------------------------------------------------------------------------
    -----------------------VIEW FUNCTIONS-----------------------------------------
    -------------------------------------------------------------------------------
     */

    public static Map<Integer, String> getMatchPlayers(Integer matchID){
        return translateMatchID(matchID).getPlayers().stream()
                .collect(Collectors.toMap(Player::getID, Player::getNickname));
    }



    /**
     * Make a copy of the state of the billboard, made of 3 layers:
     * first layer for the height of the buildings
     * second layer for the players (each player has a different number)
     * third layer for the domes
     */
    public void pushChangesToView(Integer matchID){
        MessageEvent messageEvent;
        Map<Position, Cell> billboardStatus;
        Map<Integer, String> matchPlayers;
        Set<String> matchCards;

        matchPlayers = getMatchPlayers(matchID);

        Match match = translateMatchID(matchID);

        billboardStatus = match.getBillboard().getCells();

        matchCards = match.getCards().stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());

        messageEvent = new MessageEvent("MODEL_TO_VIEW", matchID, billboardStatus, matchPlayers, matchCards);

        notify(messageEvent);
    }

    public void pushChangesToView(Integer matchID, Integer playerID){
        MessageEvent messageEvent;
        Map<Position, Set<Position>> workersAvailableCells = null;
        Set<Position> placingAvailableCells = null;

        Match match = translateMatchID(matchID);
        Player player = match.getCurrentPlayer();


        if (match.getCurrentState() == MatchState.PLACING_WORKERS)
            placingAvailableCells = player.getPlacingAvailableCells();
        else
            workersAvailableCells = player.getWorkersAvailableCells();


        messageEvent = new MessageEvent("MODEL_TO_VIEW", playerID, matchID, placingAvailableCells, workersAvailableCells);

        notify(messageEvent);
    }

    public void modelUpdateView(Integer matchID, Integer playerID){
        pushChangesToView(matchID);
        pushChangesToView(matchID, playerID);
    }


    @Override
    public void update(MessageEvent message){
        if (!(message.getMsgType().equals("VIEW_TO_MODEL"))){
            return;
        }
        modelUpdateView(message.getMatchID(), message.getPlayerID());
    }


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
    private static Match translateMatchID (Integer matchID){
        return activeMatches
                .get(matchID);
    }

    /**
     * Translates the playerID to the related instance of player
     * @param match instance of match joined by the player
     * @param playerID id of the player whose instance is requested
     * @return the instance of player related to the playerID
     */
    private static Player translatePlayerID (Match match, Integer playerID){
        if (match == null)
            return playersWaitingList
                    .stream()
                    .filter(player -> player.getID() == playerID)
                    .findAny()
                    .get();
        else
            return match
                .getPlayers()
                .stream()
                .filter(player -> player.getID()==playerID)
                .findAny().get();
    }

}

