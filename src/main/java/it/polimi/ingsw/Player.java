package it.polimi.ingsw;
import org.graalvm.compiler.core.common.util.AbstractTypeReader;

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

    public void createMatch(int playersNum){
        if(Model.getMatch()==null){
            Model.setMatch(new Match());
        }
        setMatchID(Model.getMatch());
    }

    public void setMatchID(Match matchID) {
        this.matchID = matchID;
        matchID.addPlayer(this);
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

    protected void setWorkers(AbstractObject worker1, AbstractObject worker2){
        worker = new Apollo(worker1);
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
