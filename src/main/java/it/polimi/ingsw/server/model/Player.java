package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.Set;

import static it.polimi.ingsw.server.model.TurnState.*;

/**
 * @author hamzahaddaoui
 * Class managing the instance of a certain user, liked to a match.
 */

public class Player{
    private int ID; //id connessione del giocatore
    private String nickname;
    private Match match;
    private ArrayList<Worker> workers = new ArrayList<>(2);

    private Worker currentWorker;
    private Commands commands;
    GodCards card;
    private TurnState state;

    private boolean placedWorkers;
    private boolean specialFunction;

    protected Player(int ID,String nickname, Match match) {
        this.ID = ID;
        this.nickname = nickname;
        this.match = match;
        workers.add(new Worker());
        workers.add(new Worker());
        currentWorker = workers.get(0);
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
        this.card = card;
        this.commands = card.apply(new BasicCommands());
        match.removeCard(card);
    }

    public TurnState getState() {
        return state;
    }

    public void setState(TurnState state) {
        this.state = state;
    }

    public void setUnsetSpecialFunction(){
        specialFunction ^= true;
    }

    public boolean getSpecialFunction() {
        return specialFunction;
    }



    //la funzione non va bene!
    public void playerAction(Position position){
        switch (state){
            case PLACING:
                commands.placeWorker(position, this);
                if (workers.iterator().hasNext())
                    currentWorker = workers.iterator().next();
                else
                    state = commands.nextState(this);
            case WAIT:
                workers.stream().forEach(this::setAvailableCells);
                state = commands.nextState(this);
            case MOVE:
                //le celle dove muoversi sono già state computate, poichè inviate all'utente
                commands.moveWorker(position, this);
            case BUILD:
                //le celle dove muoversi sono già state computate, poichè inviate all'utente
                commands.build(position, this);
        }
        state = commands.nextState(this);
    }

    public Set<Position> getAvailableCells() {
        return currentWorker.getAvailableCells(this.state);
    }


    private void setAvailableCells(Worker worker) {
        worker.setAvailableCells(PLACING, commands.computeAvailablePlacing(this, worker));
        worker.setAvailableCells(MOVE, commands.computeAvailableMovements(this, worker));
        worker.setAvailableCells(BUILD, commands.computeAvailableBuildings(this, worker));
    }
}
