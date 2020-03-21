package it.polimi.ingsw;

public class Player {
    private String nickname;
    private Worker worker1, worker2;
    private Match matchID;
    private Commands commands;

    private boolean positionedWorkers;

    public Player(String nickname) {
        this.nickname = nickname;
        matchID = MatchCreator.create(this, 2);
    }

    protected void initWorkers(Worker worker1, Worker worker2){
        this.worker1 = worker1;
        this.worker2 = worker2;
    }
}
