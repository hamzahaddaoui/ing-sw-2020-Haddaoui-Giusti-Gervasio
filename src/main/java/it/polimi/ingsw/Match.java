package it.polimi.ingsw;

public class Match {
    private static Match MatchID;
    private static int playersNum = 0;
    private static Player players[];
    private Billboard billboard;

    private Match(){
        billboard = new Billboard();
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
        if (playersNum == 2){ //o 3

        }
    }

}
