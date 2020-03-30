package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Message;

import it.polimi.ingsw.utilities.Observer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class GameModel {
    private static Observer<Message> observer;
    private static List<Match> activeMatches = new ArrayList<>();

    public static void addObserver(Observer<Message> obs){
        observer = obs;
    }

    public static void removeObserver(Observer<Message> obs){}

    public static void notifyObserver(Message message){
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

    public static boolean isNickAvailable(String nickname){
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
        activeMatches.get(0).setCurrentState(WAITING_FOR_PLAYERS);
    }

    public static void addRemoveCardToMatch(Match match, GodCards card){
        if (match.getCards().contains(card)) match.removeCard(card);
        else match.addCard(card);
    }

    public static void confirmCards(Match match){
        match.nextTurn();
    }

    public static void selectPlayerCard(Match match, GodCards card){
        match.getCurrentPlayer().setCommands(card);
        match.removeCard(card);
        match.nextTurn();
    }

    public static void playerCommand(Match match){

    }



    public static int[][][] getBillboard(){
        return null;
    }
}
