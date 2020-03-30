package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player{
    private int ID; //id connessione del giocatore
    private String nickname;
    private ArrayList<Worker> workers = new ArrayList<>(2);
    private Worker currentWorker;
    private Commands commands;




    public void setCommands(GodCards card) {
        this.commands = card.apply(new BasicCommands());
    }

    public Commands getCommands() {
        return commands;
    }

    protected Player(String nickname) {
        this.nickname = nickname;
        workers.add(new Worker());
        workers.add(new Worker());
    }

    public String getNickname() {
        return nickname;
    }

    public void setID(int playerID) {
        this.ID = playerID;
    }

    public int getID() {
        return ID;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setCurrentWorker(Worker worker) {
        currentWorker = worker;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public void move(){

    }
}
