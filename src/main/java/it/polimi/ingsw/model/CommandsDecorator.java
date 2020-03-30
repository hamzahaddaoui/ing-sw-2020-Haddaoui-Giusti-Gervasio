package it.polimi.ingsw.model;


import it.polimi.ingsw.utilities.Position;

public class CommandsDecorator implements Commands {
    static final GodCards card = null;
    protected Commands commands;

    @Override
    public void placeWorker(Position position) {
        commands.placeWorker(position);
    }

    @Override
    public void moveWorker(Position position) {
        commands.moveWorker(position);
    }

    @Override
    public void build(Position position) {
        commands.build(position);
    }

    @Override
    public void availableCells() {
        commands.availableCells();
    }


}
