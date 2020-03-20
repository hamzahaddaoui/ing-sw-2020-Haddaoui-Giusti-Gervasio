package it.polimi.ingsw;

import java.util.HashSet;

public class Match {
    private Match MatchID;
    private int playersNum;
    private int playersCount;
    private boolean isStarted;
    private HashSet<giocatori> players = new HashSet<giocatori>();
    private Billboard billboardID;

    public Match(int n){
        playersNum = n;
        billboardID = new Billboard();
    }

    public void addPlayer(Player player){
        if (isStarted) return; //eccezione
        giocatori gioc = new giocatori();
        gioc.player = player;
        gioc.worker1 = new Worker(billboardID);
        gioc.worker2 = new Worker(billboardID);
        players.add(gioc);
        player.initWorkers(gioc.worker1, gioc.worker2);
        playersCount++;
        if (playersCount == playersNum){
            isStarted = true;
        }
    }

    public int getPlayersNum() {
        return playersNum;
    }

    class giocatori {
        Player player;
        Worker worker1, worker2;
    }

}
