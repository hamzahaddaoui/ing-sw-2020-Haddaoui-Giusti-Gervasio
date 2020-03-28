package it.polimi.ingsw.model;

import it.polimi.ingsw.model.decorators.ApolloDecorator;
import it.polimi.ingsw.utilities.MVCObserver;
import it.polimi.ingsw.utilities.Position;

import java.lang.reflect.Array;
import java.util.*;
import java.util.ArrayList;


public class GameModel {
    private static MVCObserver observer;
    private static List<Match> activeMatches = new ArrayList<>();

    public static void addObserver(MVCObserver obs){
        observer = obs;
    }

    public static void removeObserver(MVCObserver obs){}

    public static void notifyObserver(String message){
        observer.update(message);
    }

    public static Match getCurrentMatch(){
        Match match;
        match = activeMatches.get(activeMatches.size()-1);

        if (!match.isMatchStarted()) {
            return match;
        } else {
            return null;
        }
    }

    public boolean isNickAvailable(String nickname){
        /*Match match;
        match = getCurrentMatch();
        ArrayList<Player> player;
        if (match != null){
            player = match.getPlayers();
            for(Player p: player) if (p.getNickname()==nickname) return false;
        }*/

        if (getCurrentMatch() != null){
            Optional<Player> result = getCurrentMatch().getPlayers()
                    .stream()
                    .filter(player1 -> player1.getNickname()==nickname)
                    .findAny();
            if (result.isPresent()) return false;
        }
        return true;
    }

    public static void addPlayer(String nickname){
        Player player = new Player(nickname);
        getCurrentMatch().addPlayer(player);
    }

    public static void createMatch(int playerNum){
        activeMatches.add(new Match(playerNum));
    }

    public static void addRemoveCardToMatch(Match match, GodCards card){
        if (match.getCards().contains(card)) match.removeCard(card);
        else match.addCard(card);
    }

    public static void confirmCards(Match match){
        match.nextTurn();
    }

    public static void selectPlayerCard(Match match){
    }



    public int[][][] getBillboard(){
        return null;
    }
}
