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

    protected void setWorker(){
        WorkerDecorator worker;
        switch(card){
            case Apollo:
                worker = new ApolloDecorator(new SimpleWorker());
                break;
            default:
                worker = null;
        }
        workers.add(worker);
        worker.placeWorker();
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
