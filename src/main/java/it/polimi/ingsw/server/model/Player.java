package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.Set;

import static it.polimi.ingsw.server.model.TurnState.*;
import static it.polimi.ingsw.server.model.TurnState.START;

public class Player{
    private int ID; //id connessione del giocatore
    private String nickname;
    private Match match;
    private ArrayList<Worker> workers = new ArrayList<>(2);

    private Worker currentWorker;
    private Commands commands;
    GodCards card;
    private TurnState state;

    private int movesBeforeBuild;
    private int totalBuilds;
    private int totalMoves;
    private boolean specialFunction;
    private boolean turnStart;

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

    public boolean isSpecialFunction() {
        return specialFunction;
    }

    public void playerAction(Position position){
        switch (state) {
            case START:
                movesBeforeBuild = card.getMovesBeforeBuilding();
                totalBuilds = card.getNumOfBuilding();
                totalMoves = card.getMovesAfterBuilding();
                workers.stream().forEach(this::setAvailableCells);
                break;
            case PLACING:
                commands.placeWorker(position, this);
            case MOVE:
                commands.moveWorker(position, this);
                if (movesBeforeBuild == 0) {
                    totalMoves--;
                    if (totalMoves == 0)
                        state = END;
                } else {
                    movesBeforeBuild--;
                    totalMoves--;
                    if (movesBeforeBuild == 0)
                        state = BUILD;
                }
            case BUILD:
                commands.build(position, this);
                if (-- totalBuilds == 0)
                    if (totalMoves != 0) state = MOVE;
                    else state = END;
        }
    }

    public Set<Position> getAvailableCells() {
        return currentWorker.getAvailableCells(this.state);
    }

    public int getMovesBeforeBuild(){
        return this.movesBeforeBuild;
    }

    public int getTotalBuilds(){
        return this.totalBuilds;
    }

    private void setAvailableCells(Worker worker) {
        worker.setAvailableCells(PLACING, commands.computeAvailablePlacing(this, worker));
        worker.setAvailableCells(MOVE, commands.computeAvailableMovements(this, worker));
        worker.setAvailableCells(BUILD, commands.computeAvailableBuildings(this, worker));
    }
}
