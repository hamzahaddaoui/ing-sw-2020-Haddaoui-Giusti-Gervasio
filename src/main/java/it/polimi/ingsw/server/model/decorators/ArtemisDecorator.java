package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class ArtemisDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Artemis;

    /**
     * decorate the object Command with Artemis's special power
     *
     * @param commands  represent the player behaviour
     */
    public ArtemisDecorator(Commands commands){
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
     * worker may move one additional time but not back to the initial space
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
       // super.moveWorker(position, player);
    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param player
     * @param position  is the position that player have inserted
     */
    @Override
    public void build(Player player, Position position) {
        super.build(player, position);
    }

    /**
     * method that allows the standard building dome action
     * the player can build a dome on an unoccupied space neighbouring the worker
     *
     * @param worker  is the player's selected worker
     * @param position  is the position that player have inserted
     */
    @Override
    public void buildDome(Player player, Position position) {
        super.buildDome(player, position);
    }

    /**
     * return the spaces that are available after a check on billboard
     *
     *
     * @param player@return
     */
    @Override
    public List<Position> getAvailableCells(Player player) {
        // switch(PlayerState):
        // case MOVE:


        return null;
    }

}
