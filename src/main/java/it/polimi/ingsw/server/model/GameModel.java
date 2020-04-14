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

    public static int getPlayersWaitingListSize(){
        return playersWaitingList.size();
    }

    public static int getNotInitMatchesListSize(){
        return (int) activeMatches
                .keySet()
                .stream()
                .map(matchID -> activeMatches.get(matchID))
                .filter(match -> !match.isStarted() && (match.getPlayersNum()==null))
                .count();
    }

    public static int getInitMatchesListSize(){
        return (int) activeMatches
                .keySet()
                .stream()
                .map(matchID -> activeMatches.get(matchID))
                .filter(match -> !match.isStarted() && (match.getPlayersNum()!=null))
                .count();
    }

    /**
     * Checks if the match waiting to start has a certain nickname available.
     *
     * @param nickname contains the nickname choosen by the user
     * @return false if the nickname is not available, true otherwise
     */
    public static boolean isNickAvailable(java.lang.String nickname){
        return playersWaitingList
                .stream()
                .noneMatch(player -> player.getNickname().equals(nickname))
               &&
               activeMatches
                       .keySet()
                       .stream()
                       .map(matchID -> activeMatches.get(matchID))
                       .filter(match -> !match.isStarted())
                       .noneMatch(match -> match.getPlayers()
                               .stream().anyMatch(player -> player.getNickname().equals(nickname)));
    }

    public static Integer getInitMatchID(){
        if (getInitMatchesListSize() != 0)
            return activeMatches
                    .keySet()
                    .stream()
                    .map(matchID -> activeMatches.get(matchID))
                    .filter(match -> !match.isStarted() && (match.getPlayersNum()!=null))
                    .findFirst()
                    .get()
                    .getID();
        else
            return null;
    }

    /**
     * Creates a new instance of match, with an assigned unique ID
     */
    public synchronized static int createMatch(Integer playerID, java.lang.String nickname){
        progressiveMatchID++;
        Match match = new Match(progressiveMatchID);
        activeMatches.put(progressiveMatchID, match);
        match.addPlayer(new Player(playerID, nickname, TurnState.SETTING_MATCH));

        return progressiveMatchID;
    }

    /**
     * Add a player to the "waiting to start" match
     *
     * @param playerID the ID of the player associated with that nickname and the waiting to start match
     * @param nickname the nickname of the player
     */
    public static void addPlayerToMatch(Integer matchID, Integer playerID, java.lang.String nickname){
        translateMatchID(matchID).addPlayer(new Player(playerID, nickname, TurnState.INITIALIZED));
    }

    public static void addToWaitingList(Integer playerID, java.lang.String nickname){
        playersWaitingList.addLast(new Player(playerID, nickname, TurnState.INITIALIZED));
    }


    /**
     * This method returns the playerID of the current player of the given match
     *
     * @param matchID specified matchID
     * @return the current player of the specified match
     */
    public static int getCurrentPlayerID(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().getID();
    }

    /**
     * Changes the number of the players of the "waiting to start" match
     *
     * @param playerNum the number of players wanted for the "waiting to start" match
     */
    public static void setMatchPlayersNum(Integer matchID, int playerNum){
        activeMatches.get(matchID).setPlayersNum(playerNum);
    }

    public static Integer unstackPlayerToMatch(Integer matchID){
        Player player = playersWaitingList.removeFirst();
        addPlayerToMatch(matchID, player);
        return player.getID();
    }

    private static void addPlayerToMatch(Integer matchID, Player player){
        translateMatchID(matchID).addPlayer(player);
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



    public static void nextMatchState(Integer matchID){
        translateMatchID(matchID).nextState();
    }


    public static String getMatchState(Integer matchID){
        try{
            return translateMatchID(matchID).getCurrentState().toString();
        }
        catch (NullPointerException exception){
            return null;
        }

    }

    public static String getPlayerState(Integer matchID, Integer playerID){
        try{
            return translatePlayerID(translateMatchID(matchID), playerID).getState().toString();
        }
        catch (NullPointerException exception){
            return null;
        }
    }

    public synchronized static void setMatchCards(Integer matchID, Set<java.lang.String> cards){
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
    public static void selectPlayerCard(Integer matchID, java.lang.String card){
        Match match = translateMatchID(matchID);
        GodCards godCard = GodCards.valueOf(card);
        match.getCurrentPlayer().setCommands(godCard);
    }

    public static void placeWorker(Integer matchID, Integer playerID, Position position){
        Player player = translatePlayerID(translateMatchID(matchID), playerID);
        player.setWorker(position);
    }

    public static void setUnsetSpecialFunction(Integer matchID, Integer playerID){
        Player player =  translatePlayerID(translateMatchID(matchID), playerID);
        player.setUnsetSpecialFunction();
    }

    public static void playerTurn(Integer matchID, Integer playerID, Position startPosition, Position endPosition){
        Player player = translatePlayerID(translateMatchID(matchID), playerID);
        player.setCurrentWorker(startPosition);
        player.playerAction(endPosition);
    }

    /**
     * Make the next player gain the control of the match, by passing the turn
     *
     * @param matchID selected match
     */
    public static void nextMatchTurn(Integer matchID){
        translateMatchID(matchID).nextTurn();
    }

    public static boolean hasPlayerPlacedWorkers(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().hasPlacedWorkers();
    }

    public static boolean hasPlayerFinished(Integer matchID){
        return translateMatchID(matchID).getCurrentPlayer().hasFinished();
    }

    public static Set<java.lang.String> getMatchCards(Integer matchID){
        return translateMatchID(matchID)
                .getCards()
                .stream()
                .map(godCard -> godCard.toString())
                .collect(Collectors.toSet());
    }

    public static Map<Integer, java.lang.String> getMatchPlayers(Integer matchID){
        return activeMatches
                .get(matchID)
                .getPlayers()
                .stream()
                .collect(Collectors.toMap(player -> player.getID(), player -> player.getNickname()));
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

        messageEvent = new MessageEvent("MODEL_VIEW_UPDATE", matchID, billboardStatus, workersAvailableCells);

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

