package it.polimi.ingsw;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    private String nickname;
    private ArrayList<Worker> workers = new ArrayList<>();
    private Match matchID;
    private Commands commands;
    private Worker currentWorker;

    private boolean positionedWorkers;

    public Player(String nickname) {
        this.nickname = nickname;
        matchID = MatchCreator.create(this, 2);
    }

    protected void initWorkers(Worker worker1, Worker worker2){
        workers.add(worker1);
        workers.add(worker2);
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void chooseWorker(Worker worker) {
        this.currentWorker = worker;
    }
}
