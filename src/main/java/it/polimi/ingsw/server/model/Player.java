package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;

import java.util.*;

import static it.polimi.ingsw.server.model.TurnState.*;

/**
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

    public Player(int ID,String nickname, TurnState state) {
        this.ID = ID;
        this.nickname = nickname;
        this.state = state;
    }

    public void setMatch(Match match){
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

    public Commands getCommands() {return commands;}

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
            case IDLE:
                finished = false;
                specialFunction = false;
                state = commands.nextState(this);
            case MOVE:
                commands.moveWorker(position, this);
            case BUILD:
                commands.build(position, this);
        }
        state = commands.nextState(this);
    }

    public Map<Position,Set<Position>> getAvailableCells() {
        Map<Position,Set<Position>> positionSetMap = new HashMap<>();
        workers.stream().forEach(worker -> {
            positionSetMap.put(worker.getPosition(),worker.getAvailableCells(state));
        });
        return positionSetMap;
    }

    public void setHasFinished(boolean hasFinished) {
        this.finished = hasFinished;
    }

    public boolean hasFinished() {
        return finished;
    }

    public void setAvailableCells() {
        workers.stream().forEach(worker -> {
        if (!placedWorkers)
            worker.setAvailableCells(PLACING, commands.computeAvailablePlacing(this, worker));
        else {
            worker.setAvailableCells(MOVE, commands.computeAvailableMovements(this, worker));
            worker.setAvailableCells(BUILD, commands.computeAvailableBuildings(this, worker));
        }});
    }
}
