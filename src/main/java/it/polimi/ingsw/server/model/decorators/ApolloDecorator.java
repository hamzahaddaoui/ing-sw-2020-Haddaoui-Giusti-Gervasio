package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void placeWorker(Position position, Player player) {
        super.placeWorker(position,player);
    }

    /**
     * worker may move into ah opponent Worker's space by forcing their worker to the space yours just vacated
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
       // super.moveWorker(worker,position,billboard);
    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param player
     * @param position  is the position that player have inserted
     */
    @Override
    public void build(Position position, Player player) {
        super.build(position, player);
    }

    /**
     * return the spaces that are available after a check on billboard
     *
     *
     * @param player@return
     */
    @Override
    public Set<Position> getAvailableCells(Player player) {



        return null;
    }


    public List<Position> checkAvailableMovements() {

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
