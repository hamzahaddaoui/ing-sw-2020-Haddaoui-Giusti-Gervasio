package it.polimi.ingsw;

public class Match {
    private Match MatchID;
    private int playersNum;
    private int playersCount;
    private boolean isStarted;
    private Player players[];
    private Billboard billboardID;

    public Match(int n){
        players = new Player[n];
        playersNum = n;
        billboardID = new Billboard();
    }

    public void addPlayer(Player player){
        players[playersCount] = player;
        playersCount++;
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public void matchStart(){
        Worker worker1, worker2;
        isStarted = true;
        if (playersNum >= 2){ //o 3
            for(Player player: players){

                worker1 = new Worker(billboardID);
                worker2 = new Worker(billboardID);
                player.placeWorkers(worker1, worker2);


                //scelta carta divinità
            }
        }
    }

}
