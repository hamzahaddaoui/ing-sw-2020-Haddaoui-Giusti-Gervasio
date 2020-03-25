package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player extends GameEntity{
    private String nickname;
    private ArrayList<Worker> workers = new ArrayList<>();
    private Match matchID;
    private Worker currentWorker;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public void createMatch(int playersNum){
        if(GameModel.getMatch()==null){
            GameModel.setMatch(new Match());
        }
        setMatchID(GameModel.getMatch());
    }

    public void setMatchID(Match matchID) {
        this.matchID = matchID;
        matchID.addPlayer(this);
    }

    public Match getMatchID(){
        return matchID;
    }

    protected void setWorker(){
       workers.add(new Worker());
       workers.add(new Worker());
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
