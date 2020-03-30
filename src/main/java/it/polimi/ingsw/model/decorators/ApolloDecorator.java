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

    /**
     * decorate the object Command with Apollo's special power
     *
     * @param commands represent the player behaviour
     */
    public ApolloDecorator(Commands commands){
        this.commands=commands;
    }

    public GodCards getCard () {
        return card;
    }

    /**
     * method that allows the stardard placing movement
     * also if the selected position is free
     * @param worker is the player's selected worker
     * @param position is the position that player have inserted
     * @param billboard is reference to the gameboard
     */
    @Override
    public void placeWorker(Worker worker, Position position, Billboard billboard) {
        super.placeWorker(worker,position,billboard);
    }

    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     * @param worker is the player's selected worker
     * @param position is the position that player have inserted
     * @param billboard is reference to the gameboard
     */
    @Override
    public void moveWorker(Worker worker, Position position, Billboard billboard) {
        super.moveWorker(worker,position,billboard);
    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     * @param worker is the player's selected worker
     * @param position is the position that player have inserted
     * @param billboard is the reference to the gameboard
     */
    @Override
    public void build(Worker worker, Position position, Billboard billboard) {
        super.build(worker,position,billboard);
    }

    /**
     * return the spaces that are available after a check on billboard
     * @param billboard  is the reference to the gameboard
     */
    @Override
    public void availableCells( Billboard billboard) {
        // switch(PlayerState):
        // case MOVE:


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
