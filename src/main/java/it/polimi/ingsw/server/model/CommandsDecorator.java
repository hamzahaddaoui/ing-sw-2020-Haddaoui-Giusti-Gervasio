package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

import java.util.Set;

public class CommandsDecorator implements Commands {
    protected Commands commands;

    @Override
    public void nextState(Player player) {
        commands.nextState(player);
    }

    /**
     * method that allows the stardard placing movement
     *  @param position  is the position that player have inserted
     */
    @Override
    public void placeWorker(Position position, Player player) {
        commands.placeWorker(position, player);
    }

    @Override
    public void moveWorker(Position position, Player player) {
        commands.moveWorker(position, player);
    }

    @Override
    public void build(Position position, Player player) {
        commands.build(position, player);
    }

    @Override
    public Set<Position> computeAvailablePlacing(Player player) {
        return commands.computeAvailablePlacing(player);
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
    public void notifySpecialFunction(Player player){
        commands.notifySpecialFunction(player);
    }

    @Override
    public boolean winningCondition(Player player) { return commands.winningCondition(player); }

    @Override
    public boolean losingCondition(Player player) { return commands.losingCondition(player); }
}
