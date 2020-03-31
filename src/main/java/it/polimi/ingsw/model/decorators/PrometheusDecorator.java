package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class PrometheusDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Prometheus;
    public PrometheusDecorator(Commands commands){
        this.commands=commands;
    }
    private boolean hasBuiltBefore = false;
    private boolean hasMoved = false;

    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     * @param worker is the player's selected worker, not null
     * @param position is the position that player have inserted, not null
     * @param billboard is reference to the gameboard, not null
     */
    @Override
    public void moveWorker(Worker worker, Position position, Billboard billboard) {
        super.moveWorker(worker,position,billboard);
    }

    /**
     * Method that allows the specific building block action of Prometheus.
     * <p>
     * The player can build both before and after moving, just if he doesn't move up.
     * Check on the boolean hasMoved to understand if he's building before or after.
     *
     * @param worker     the player's selected worker, not null
     * @param position   the position that player have inserted, not null
     * @param billboard  the reference to the gameboard, not null
     */
    @Override
    public void build(Worker worker, Position position, Billboard billboard) {
        if (!hasMoved) {
            super.build(worker, position, billboard);
            hasBuiltBefore = true;
        }
        else super.build(worker,position,billboard);
    }

    /**
     * Return the spaces that are available after a check on billboard.
     * <p>
     * If the player builds before moving, he can't see higher level spaces.
     *
     * @param worker     the player's selected worker, not null
     * @param billboard  the reference to the gameboard, not null
     * @return           the spaces which are available
     */
    @Override
    public List<Position> getAvailableCells(Worker worker, Billboard billboard) {
        // switch(PlayerState):
        // case MOVE:
        // se costruisco prima, non posso salire di livello --> check hasBuiltBefore
        // se true, non mostro le caselle in cui getTowerHeights è > rispetto a quella della posizione attuale del worker
        // altrimenti faccio un semplice super.getAvailableCells

        return null;
    }
}
