package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Commands;
import it.polimi.ingsw.model.CommandsDecorator;
import it.polimi.ingsw.model.GodCards;
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
    public void placeWorker(Position position) {
        super.placeWorker(position);
    }

    @Override
    public void moveWorker(Position position) {
        super.moveWorker(position);
    }

    @Override
    public void build(Position position) {
        super.build(position);
    }

    @Override
    public void availableCells() {
        switch(PlayerState):
        case MOVE:


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
