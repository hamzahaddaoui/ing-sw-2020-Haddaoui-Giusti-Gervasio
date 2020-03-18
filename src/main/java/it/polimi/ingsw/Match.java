package it.polimi.ingsw;

public class Match {
    private static Match MatchID;
    private static int playersNum = 0;
    private static Player players[];
    private Billboard billboardID;

    private Match(){
        billboardID = new Billboard();
    }

    public static Match MatchID(Player playerID, int n) {
        if (MatchID == null) {
            players = new Player[n];
            MatchID = new Match();
        }
        players[playersNum] = playerID;
        playersNum++;
        return MatchID;
    }

    public static int getPlayersNum() {
        return playersNum;
    }

    public void matchStart(){
        Worker worker1, worker2;

        if (playersNum == 2){ //o 3
            for(Player player: players){

                worker1 = new Worker(billboardID);
                worker2 = new Worker(billboardID);
                player.placeWorkers(worker1, worker2);


                //scelta carta divinità
            }
        }
    }

}
