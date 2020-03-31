package it.polimi.ingsw.model;


import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class CommandsDecorator implements Commands {
    static final GodCards card = null;
    protected Commands commands;

    /**
     * method that allows the stardard placing movement
     *
     * @param worker  is the player's selected worker
     * @param position  is the position that player have inserted
     * @param billboard  is reference to the gameboard
     */
    @Override
    public void placeWorker(Worker worker, Position position, Billboard billboard) {
        commands.placeWorker(worker,position,billboard);
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
        commands.moveWorker(worker,position,billboard);
    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param worker  is the player's selected worker
     * @param position  is the position that player have inserted
     * @param billboard  is the reference to the gameboard
     */
    @Override
    public void build(Worker worker, Position position, Billboard billboard) {
        commands.build(worker,position,billboard);
    }

    /**
     * method that allows the standard building dome action
     * the player can build a dome on an unoccupied space neighbouring the worker
     *
     * @param worker  is the player's selected worker
     * @param position  is the position that player have inserted
     * @param billboard  is the reference to the gameboard
     */
    public void buildDome(Worker worker, Position position, Billboard billboard) {

    }

    /**
     * return the spaces that are available after a check on billboard
     *
     * @param billboard  is the reference to the gameboard
     * @return
     */
    @Override
    public List<Position> getAvailableCells(Worker worker, Billboard billboard) {
        commands.getAvailableCells(worker, billboard);
        return null;
    }


}
