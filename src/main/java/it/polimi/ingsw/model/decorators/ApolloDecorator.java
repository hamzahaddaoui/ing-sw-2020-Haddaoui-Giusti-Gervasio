package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;

public class ApolloDecorator extends CommandsDecorator {
    private GodCards card = GodCards.Apollo;

    private int movesBeforeBuild = 2;
    private int numOfBuilds = 1;
    private int movesAfterBuild = 1;
    private boolean doneStandard = false;
    private boolean positionedWorkers = false;


    public ApolloDecorator(Commands commands){
        this.commands=commands;
    }

    public GodCards getCard () {
        return card;
    }

    @Override
    public void placeWorker(Worker worker, Position position, Billboard billboard) {
        super.placeWorker(worker, position, billboard);
    }

    @Override
    public void moveWorker(Worker worker, Position position, Billboard billboard) {
        super.moveWorker(worker, position, billboard);
    }

    @Override
    public void build(Worker worker, Position position, Billboard billboard) {
        super.build(worker, position, billboard);
    }

    @Override
    public void availableCells(Billboard billboard) {
        super.availableCells(billboard);
    }

    public void buildBlock() {

    }


    public void buildDome() {

    }


    public HashSet<Position> checkAvailableMovements() {

        return null;
    }


    public HashSet<Position> checkAvailableBuildBlocks() {
        return null;
    }


    public HashSet<Position> checkAvailableBuildDomes() {
        return null;
    }


    public boolean hasWon() {
        return false;
    }


    public boolean hasLost() {
        return false;
    }


    public boolean hasMoved() {
        return false;
    }


    public boolean hasBuilt() {
        return false;
    }


    public boolean hasDoneStandard() {
        return false;
    }
}
