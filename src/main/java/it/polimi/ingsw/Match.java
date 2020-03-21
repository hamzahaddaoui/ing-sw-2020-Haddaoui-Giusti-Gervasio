package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashSet;

public class Match {
    static final int PLAYERS_NUM = 2;

    private int playersCount;

    private ArrayList<Player> players = new ArrayList<Player>();
    private Player currentPlayer;
    private Billboard billboardID;

    private boolean isStarted;

    public Match(int n){
        billboardID = new Billboard();
    }

    public void addPlayer(Player player){
        players.add(player);
        player.initWorkers(new Worker(), new Worker());
        playersCount++;
    }

    public Player matchStart(){
        isStarted = true;
        currentPlayer = players.get(0);
        return currentPlayer;
    }

    public Player nextTurn(){
        if (players.indexOf(currentPlayer)== PLAYERS_NUM-1){
            currentPlayer = players.get(0);
        }
        else currentPlayer = players.get(players.indexOf(currentPlayer)+1);
        return currentPlayer;
    }


}
