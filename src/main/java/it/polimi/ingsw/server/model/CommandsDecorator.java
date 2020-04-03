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
    public void build(Position position, Player player, boolean forceDome) {
        commands.build(position, player, forceDome);
    }

    /**
     * method that allows the standard building dome action
     * the player can build a dome on an unoccupied space neighbouring the worker
     *
     * @param player
     * @param position  is the position that player have inserted
     *
     */
    public void buildDome(Position position, Player player) {

    }

    /**
     * return the spaces that are available after a check on billboard
     *
     *
     * @param player
     * @return
     */
    public Set<Position> getAvailableCells(Player player) {
        return commands.getAvailableCells(player);
    }

    @Override
    public Set<Position> computeAvailablePlacing(Player player) {
        return commands.computeAvailablePlacing(player);
    }

    @Override
    public Set<Position> computeAvailableMovements(Player player) {
        return commands.computeAvailableMovements(player);
    }

    @Override
    public Set<Position> computeAvailableBuildings(Player player) {
        return commands.computeAvailableBuildings(player);
    }

    /**
     * Activates (deactivates) special function related to a certain player
     */
    public void specialFunctionSetUnset(){
        commands.specialFunctionSetUnset();
    }

    @Override
    public void reset(Player player) {
        commands.reset(player);
    }

    @Override
    public void winningCondition(Position startingPosition, Player player) { commands.winningCondition(startingPosition,player); }

    @Override
    public void losingCondition(Player player) { commands.losingCondition(player); }
}
