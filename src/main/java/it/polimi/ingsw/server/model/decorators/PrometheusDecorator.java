package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
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
     * @param position is the position that player have inserted, not null
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        super.moveWorker(position, player);
    }

    /**
     * Method that allows the specific building block action of Prometheus.
     * <p>
     * The player can build both before and after moving, just if he doesn't move up.
     * Check on the boolean hasMoved to understand if he's building before or after.
     *
     * @param player
     * @param position   the position that player have inserted, not null
     */
    @Override
    public void build(Player player, Position position) {
        if (!hasMoved) {
            super.build(player, position);
            hasBuiltBefore = true;
        }
        else super.build(player, position);
    }

    /**
     * Return the spaces that are available after a check on billboard.
     * <p>
     * If the player builds before moving, he can't see higher level spaces.
     *
     * @param player     the player's selected worker, not null
     * @return           the spaces which are available
     */
    @Override
    public List<Position> getAvailableCells(Player player) {
        // switch(PlayerState):
        // case MOVE:
        // se costruisco prima, non posso salire di livello --> check hasBuiltBefore
        // se true, non mostro le caselle in cui getTowerHeights è > rispetto a quella della posizione attuale del worker
        // altrimenti faccio un semplice super.getAvailableCells

        return null;
    }
}
