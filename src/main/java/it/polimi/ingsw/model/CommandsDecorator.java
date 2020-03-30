package it.polimi.ingsw.model;


import it.polimi.ingsw.utilities.Position;

public class CommandsDecorator implements Commands {
    static final GodCards card = null;
    protected Commands commands;


    @Override
    public void placeWorker(Worker worker, Position position, Billboard billboard) {

    }

    @Override
    public void moveWorker(Worker worker, Position position, Billboard billboard) {

    }

    @Override
    public void build(Worker worker, Position position, Billboard billboard) {

    }

    @Override
    public void availableCells(Billboard billboard) {

    }
}
