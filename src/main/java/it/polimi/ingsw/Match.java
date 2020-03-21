package it.polimi.ingsw;

import java.util.HashSet;

public class Match {
    static final int PLAYERS_NUM = 2;

    private int playersCount;
    private HashSet<Player> players = new HashSet<Player>();
    private Billboard billboardID;

    private boolean isStarted;

    public Match(int n){
        billboardID = new Billboard();
    }

    public void addPlayer(Player player){
        players.add(player);
        player.initWorkers(new Worker(billboardID), new Worker(billboardID));
        playersCount++;
    }
}
