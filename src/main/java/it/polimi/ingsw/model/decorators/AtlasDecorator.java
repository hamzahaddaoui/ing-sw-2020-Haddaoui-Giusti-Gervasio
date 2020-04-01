package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class AtlasDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Atlas;

    /**
     * decorate the object Command with Atlas's special power
     *
     * @param commands  represent the player behaviour
     */
    public AtlasDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * method that allows the stardard placing movement
     * also if the selected position is free
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void placeWorker(Position position, Player player) {
        super.placeWorker(position, player);
    }

    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        //super.moveWorker(worker,position,billboard);
    }

    /**
     * method that allows the special building block action
     * the worker may build a dome at any level
     *
     * @param player
     * @param position  is the position that player have inserted
     */
    @Override
    public void build(Player player, Position position) {
        //super.build(worker,position,billboard);
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
