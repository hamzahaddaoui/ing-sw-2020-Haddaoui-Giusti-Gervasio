package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Message;

import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.Position;
import java.util.*;
import java.util.stream.Collectors;


public class GameModel extends Observable {
    private static Observer<Message> observer;
    private static NavigableMap<Integer, Match> activeMatches = new TreeMap<>();

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
     * @param nickname contains the nickname choosen by the user
     * Checks if the match waiting to start has that nickname available.
     *
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
     */
    public static boolean addPlayer(Integer playerID, String nickname){
        Player player = new Player(playerID, nickname);
        Match match = activeMatches.get(activeMatches.lastKey());
        match.addPlayer(player);
        return match.isNumReached();
    }

    /**
     * @param playerNum number of players of the match
     * Creates a new instance of match, with the specified number of players
     * with an assigned unique ID
     */
    public static void createMatch(int playerNum){
        int matchID = 0;
        if (activeMatches.lastKey() != null) matchID = activeMatches.lastKey() + 1;
        activeMatches.put(matchID, new Match(activeMatches.lastKey(), playerNum));
    }

    /**
     * @param matchID specified matchID 
     * @param card special card selected by the current user
     * Add (or removes) a specific card to the match deck,
     * according to the number of players of the match
     *
     * @return true if the cards deck is full (cards number is equal to players number)
     */
    public static boolean addRemoveCardToMatch(Integer matchID, String card){
        Match match = activeMatches.get(matchID);
        GodCards godCard = GodCards.valueOf(card);
        if (match.getCards().contains(godCard)) match.removeCard(godCard);
        else match.addCard(godCard);

        if (match.isDeckFull()){
            match.nextTurn();
        }

        return match.isDeckFull();
    }

    /**
     * @param matchID specified matchID
     * @return the current player of the specified match
     */
    public int getCurrentPlayerID(Integer matchID){
        Match match = activeMatches.get(matchID);
        return match.getCurrentPlayer().getID();
    }

    /**
     * @param matchID selected match
     * @param card special card selected by the current user
     * Link the current player of the selected match, to the specified GodCard.
     */
    public static void selectPlayerCard(Integer matchID, String card){
        Match match = activeMatches.get(matchID);
        GodCards godCard = GodCards.valueOf(card);
        match.getCurrentPlayer().setCommands(godCard);
        match.removeCard(godCard);
        match.nextTurn();
    }

    /**
     * @param matchID selected match
     *
     * Make the next player gain the control of the match, by passing the turn
     */
    public static void nextMatchTurn(Integer matchID){
        activeMatches.get(matchID).nextTurn();
    }

    /**
     * @param matchID selected match
     * @param playerID player liked to the match
     *
     * @return the list of cells on the billboard, where the player could move
     */
    public static List<Position> getAvailableCells(Match matchID, Integer playerID){
        Match match = activeMatches.get(matchID);
        List<Player> player = match.getPlayers().stream().filter(player1 -> player1.getID()==playerID).collect(Collectors.toList());
        if (player.size()==1)
            return player.get(0).Commands().getAvailableCells(match.getBillboardID());
        else
            return null; //errore!
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

    /*
    public workerSelection() -> seleziona quale worker usare
    public playerPosition() -> invia una posizione per muoversi o costruire
    public setUnsetSpecialFunction() -> seleziona se disponibile una funzione speciale
    */
}

