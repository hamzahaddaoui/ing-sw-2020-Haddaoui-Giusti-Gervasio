package it.polimi.ingsw.model;


import it.polimi.ingsw.utilities.Position;

public class CommandsDecorator implements Commands {
    static final GodCards card = null;
    protected Commands commands;

    /**
     * method that allows the stardard placing movement
     * also if the selected position is free
     * @param worker is the player's selected worker
     * @param position is the position that player have inserted
     * @param billboard is reference to the gameboard
     */
    @Override
    public void placeWorker(Worker worker, Position position, Billboard billboard) {
        commands.placeWorker(worker,position,billboard);
    }

    @Override
    public void moveWorker(Worker worker, Position position, Billboard billboard) {
        commands.moveWorker(worker,position,billboard);
    }

    @Override
    public void build(Worker worker, Position position, Billboard billboard) {
        commands.build(worker,position,billboard);
    }

    @Override
    public void availableCells(Billboard billboard) {
        commands.availableCells(billboard);
    }


}
