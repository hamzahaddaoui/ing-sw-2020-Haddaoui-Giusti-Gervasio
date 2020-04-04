package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;
import java.util.Set;

public class CommandsDecorator implements Commands {
    protected Commands commands;

    /**
     * method that allows the stardard placing movement
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void placeWorker(Position position, Player player) {
        commands.placeWorker(position, player);
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
        commands.moveWorker(position, player);
    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param player
     * @param position  is the position that player have inserted
     */
    @Override
    public void build(Position position, Player player) {
        commands.build(position, player);
    }

    @Override
    public Set<Position> computeAvailablePlacing(Player player, Worker worker) {
        return commands.computeAvailablePlacing(player, worker);
    }

    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
        return commands.computeAvailableMovements(player, worker);
    }

    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        return commands.computeAvailableBuildings(player, worker);
    }

    @Override
    public boolean winningCondition(Player player) { return commands.winningCondition(player); }

    @Override
    public boolean losingCondition(Player player) { return commands.losingCondition(player); }
}
