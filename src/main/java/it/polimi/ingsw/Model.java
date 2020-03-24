package it.polimi.ingsw;

import java.util.ArrayList;

public class Model {
    private static MVCObserver observer;
    private static ArrayList<Player> playersList;
    private static Match match;
    private static Billboard billboard;

    public static void addObserver(MVCObserver ob){
        observer = ob;
    }

    public static void removeObserver(MVCObserver ob){}

    public static void notifyObserver(String message){
        observer.update(message);
    }

    public static void createPlayer(String nickname){
        playersList.add(new Player(nickname));
    }

    public static Match getMatch(){
        return match;
    }

    public static void setMatch(Match m){
        match = m;
    }

}
