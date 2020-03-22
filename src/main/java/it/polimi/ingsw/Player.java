package it.polimi.ingsw;
import java.util.ArrayList;

public class Player {
    private String nickname;
    private ArrayList<Worker> workers = new ArrayList<>();
    private Match matchID;
    private Worker currentWorker;
    private GodCards card;
    private boolean positionedWorkers;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public void setMatchID(Match matchID) {
        this.matchID = matchID;
    }

    public Match getMatchID(){
        return matchID;
    }

    public void setCard(GodCards card){
        this.card = card;
    }

    public GodCards getCard(){
        return this.card;
    }

    protected void setWorkers(Worker worker1, Worker worker2){
        workers.add(worker1);
        workers.add(worker2);
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setCurrentWorker (Worker worker) {
        this.currentWorker = worker;
    }

    public Worker getCurrentWorker () {
        return currentWorker;
    }



}
