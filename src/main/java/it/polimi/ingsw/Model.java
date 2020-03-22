package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Model {
    private static MVCObserver observer;
    private static ArrayList<Player> playersList;
    private static Match match;
    private static Billboard billboard;

    public static void addObserver(MVCObserver ob){
        observer = ob;
    }

    public static void notifyObserver(){
        observer.update();
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
