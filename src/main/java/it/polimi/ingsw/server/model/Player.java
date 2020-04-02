package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;

public class Player{
    private int ID; //id connessione del giocatore
    private String nickname;
    private Match match;
    private ArrayList<Worker> workers = new ArrayList<>(2);
    //Worker 0 - Worker 1
    private Worker currentWorker;
    private Commands commands;
    private TurnState state;

    protected Player(int ID,String nickname, Match match) {
        this.ID = ID;
        this.nickname = nickname;
        this.match = match;
    }

    public String getNickname() {
        return nickname;
    }

    public int getID() {
        return ID;
    }

    public Match getMatch() {
        return match;
    }

    public void setWorkers(Position position){
        workers.add(new Worker(position));
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public void setCurrentWorkerID(Integer worker) {
        currentWorker = workers.get(worker);
    }

    public int getCurrentWorkerID() {
        return workers.indexOf(currentWorker);
    }

    public void setCommands(GodCards card) {
        this.commands = card.apply(new BasicCommands());
        match.removeCard(card);
    }

    public Commands Commands() {
        return commands;
    }

    public TurnState getState() {
        return state;
    }

    public void setState(TurnState state) {
        this.state = state;
    }

    public void playerTurn(Position position){
        switch(state){
            case START:
                Commands().reset();
            case PLACING:
                Commands().placeWorker(position, this);
            case MOVE:
                Commands().moveWorker(position,this);
            case BUILD:
                Commands().build(position, this);
            case END:

        }
    }
}
