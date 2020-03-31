package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;

public class Player{
    private int ID; //id connessione del giocatore
    private String nickname;
    private ArrayList<Worker> workers = new ArrayList<>(2);
    private Worker currentWorker;
    private Commands commands;
    private PlayerState state;

    protected Player(int ID,String nickname) {
        this.ID = ID;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public int getID() {
        return ID;
    }

    public void setWorkers(Position position){
        workers.add(new Worker(position));
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

    public void setCommands(GodCards card) {
        this.commands = card.apply(new BasicCommands());
    }

    public Commands Commands() {
        return commands;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void mossa(Position position, Billboard billboard){
        //a seconda dello stato corrente chiama le diverse funzioni di commands
        //se placing worker
        //placeWorker(currentWorker, position, billboard);
        //etc...

    }
}
