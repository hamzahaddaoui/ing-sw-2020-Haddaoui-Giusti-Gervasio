package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class DemeterDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Demeter;

    /**
     * decorate the object Command with Demeter's special power
     *
     * @param commands represent the player behaviour
     */
    public DemeterDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * method that allows the stardard placing movement
     * also if the selected position is free
     *
     * @param worker  is the player's selected worker
     * @param position  is the position that player have inserted
     * @param billboard  is reference to the gameboard
     */
    @Override
    public void placeWorker(Worker worker, Position position, Billboard billboard) {
        super.placeWorker(worker,position,billboard);
    }

    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     *
     * @param worker  is the player's selected worker
     * @param position  is the position that player have inserted
     * @param billboard  is reference to the gameboard
     */
    @Override
    public void moveWorker(Worker worker, Position position, Billboard billboard) {
        super.moveWorker(worker,position,billboard);
    }

    /**
     * method that allows the special building block action
     * the worker may build one additional time but not on the same space
     *
     * @param worker  is the player's selected worker
     * @param position  is the position that player have inserted
     * @param billboard  is the reference to the gameboard
     */
    @Override
    public void build(Worker worker, Position position, Billboard billboard) {
        super.build(worker,position,billboard);
    }

    /**
     * method that allows the standard building dome action
     * the player can build a dome on an unoccupied space neighbouring the worker
     *
     * @param worker  is the player's selected worker
     * @param position  is the position that player have inserted
     * @param billboard  is the reference to the gameboard
     */
    @Override
    public void buildDome(Worker worker, Position position, Billboard billboard) {

    }

    /**
     * return the spaces that are available after a check on billboard
     *
     * @param billboard  is the reference to the gameboard
     * @return
     */
    @Override
    public List<Position> getAvailableCells(Billboard billboard) {
        // switch(PlayerState):
        // case MOVE:


        return null;
    }
}