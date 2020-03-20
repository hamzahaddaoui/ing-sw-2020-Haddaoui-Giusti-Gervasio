package it.polimi.ingsw;

public class Player {
    private String nickname;
    private Worker worker1, worker2;
    private boolean positionedWorkers;
    private Match matchID;

    public Player(String nickname) {
        this.nickname = nickname;
        matchID = MatchCreator.create(this, 2);
    }

    public void placeWorkers(Worker worker1, Worker worker2){
        int x = 0,y = 0;
        if (positionedWorkers) return; //lancia eccezione

        this.worker1 = worker1;
        this.worker2 = worker2;
        //richiesta all'utente di dove posizionare il primo worker
        worker1.setPosition(x,y);

        //richiesta all'utente di dove posizionare il secondo worker
        worker2.setPosition(x,y);

        positionedWorkers = true;
    }

    public void moveWorker(){
        int x = 0, y = 0;
        Worker worker = null;
        if(!positionedWorkers) return; //eccezione

        //richiesta di quale worker spostare, e dove
        worker.setPosition(x,y);
    }

    public void build(){

    }

    public void buildDome(){

    }



}
