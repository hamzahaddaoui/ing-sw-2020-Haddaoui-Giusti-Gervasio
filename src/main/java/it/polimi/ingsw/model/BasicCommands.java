package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class BasicCommands implements Commands {

    private Billboard billboard;

    /**
     * method that allows the stardard placing movement
     *
     * @param worker     the player's selected worker, not null
     * @param position   the position that player have inserted, not null
     * @param billboard  the reference to the gameboard, not null
     */
    @Override
    public void placeWorker(Worker worker, Position position, Billboard billboard) {

    }

    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     *
     * @param worker     the player's selected worker, not null
     * @param position   the position that player have inserted, not null
     * @param billboard  the reference to the gameboard, not null
     */
    @Override
    public void moveWorker(Worker worker, Position position, Billboard billboard) {

    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param worker     the player's selected worker, not null
     * @param position   the position that player have inserted, not null
     * @param billboard  the reference to the gameboard, not null
     */
    @Override
    public void build(Worker worker, Position position, Billboard billboard) {

    }


    /**
     * method that allows the standard building dome action
     * the player can build a dome on an unoccupied space neighbouring the worker
     *
     * @param worker     the player's selected worker, not null
     * @param position   the position that player have inserted, not null
     * @param billboard  the reference to the gameboard, not null
     */

    public void buildDome(Worker worker, Position position, Billboard billboard) {

    }

    /**
     * Return the spaces that are available after a check on billboard.
     *
     * @param worker     the current worker, not null
     * @param billboard  the reference to the gameboard, not null
     *
     */
    @Override
    public List<Position> getAvailableCells(Worker worker, Billboard billboard) {
        return null;
    }
}
