package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Position;

public class ArtemisDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Artemis;

    /**
     * decorate the object Command with Artemis's special power
     *
     * @param commands represent the player behaviour
     */
    public ArtemisDecorator(Commands commands){
        this.commands=commands;
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

}
