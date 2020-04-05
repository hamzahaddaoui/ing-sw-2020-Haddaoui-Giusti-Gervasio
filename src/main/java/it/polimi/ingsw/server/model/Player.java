package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.HashSet;
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
    private Set<Worker> workers = new HashSet<>(2);

    private Worker currentWorker;
    private Commands commands;
    GodCards card;
    private TurnState state;
    private boolean finished;
    private boolean placedWorkers;

    private boolean specialFunction;

    public Player(int ID,String nickname, Match match) {
        this.ID = ID;
        this.nickname = nickname;
        state = PLACING;
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

    public Set<Worker> getWorkers() {
        return workers;
    }

    public void setWorker(Position position) {
        position.setZ(0);
        workers.add(new Worker(position));
        commands.placeWorker(position, this);
        if (workers.size() == 2){
            state = commands.nextState(this);
            placedWorkers = true;
        }

    }

    public boolean hasPlacedWorkers() {
        return placedWorkers;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public void setCurrentWorker(Position position) {
        currentWorker = workers.stream().filter(worker -> worker.getPosition()==position).findAny().get();
    }

    public Position getCurrentWorkerPosition() {
        return currentWorker.getPosition();
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



    public void playerAction(Position position){
        switch (state){
            case WAIT:
                finished = false;
                workers.stream().forEach(this::setAvailableCells);
                state = commands.nextState(this);
            case MOVE:
                commands.moveWorker(position, this);
            case BUILD:
                commands.build(position, this);
        }
        state = commands.nextState(this);
    }

    public Set<Position> getAvailableCells() {
        return currentWorker.getAvailableCells(this.state);
    }

    public void setHasFinished(boolean hasFinished) {
        this.finished = hasFinished;
    }

    public boolean hasFinished() {
        return finished;
    }

    public void setAvailableCells(Worker worker) {
        worker.setAvailableCells(PLACING, commands.computeAvailablePlacing(this, worker));
        worker.setAvailableCells(MOVE, commands.computeAvailableMovements(this, worker));
        worker.setAvailableCells(BUILD, commands.computeAvailableBuildings(this, worker));
    }
}
