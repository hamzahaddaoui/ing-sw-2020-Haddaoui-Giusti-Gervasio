package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicCommands implements Commands {
    protected Set<Position> availablePlacing = new HashSet<>();
    protected Set<Position> availableMovements = new HashSet<>();
    protected Set<Position> availableBuildings = new HashSet<>();

    /**
     * method that allows the stardard placing movement
     *
     * @param position  the position that player have inserted, not null
     * @param player  that does the placing action
     */
    @Override
    public void placeWorker(Position position, Player player) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            position.setZ(0);
            player.getCurrentWorker().setPosition(position);
            billboard.setPlayer(position, player.getCurrentWorker());
        }
        catch(NullPointerException ex){
            throw new NullPointerException();
        }
    }

    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     *  @param position   the position that player have inserted, not null
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();

        if (!availableMovements.contains(position))
            return;

        position.setZ(billboard.getTowerHeight(position));
        billboard.resetPlayer(worker.getPosition());
        worker.setPosition(position);
        billboard.setPlayer(position, worker);

        player.setState(TurnState.BUILD);
    }

    @Override
    public void build(Position position, Player player) {
        if (!availableBuildings.contains(position))
            return;
        player.getMatch().getBillboard().incrementTowerHeight(position);

        player.setState(TurnState.END);
    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param player
     * @param position   the position that player have inserted, not null
     */
    @Override
    public void build(Position position, Player player, boolean forceDome) {
        if (forceDome){
            this.availableBuildings=computeAvailableBuildings(player);
            if (!availableBuildings.contains(position))
                return;
            player.getMatch().getBillboard().setDome(position);}
        else
            build(position, player);
    }

    /**
     * method that divide the different implementation of available cells: for building action and for movement action
     *
     * @param player  is the current player
     * @return  the list of Position that are available for that specific action
     */
    //@Override
    public Set<Position> getAvailableCells(Player player) {
        try{
            switch (player.getState()){
                case PLACING:
                    computeAvailablePlacing(player);
                    return availablePlacing;
                case MOVE:
                    computeAvailableMovements(player);
                    return availableMovements;
                case BUILD:
                    computeAvailableBuildings(player);
                    return availableBuildings;
                default:
                    return null;
            }
        } catch(NullPointerException ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> computeAvailablePlacing(Player player) {
        try{
            availablePlacing = player
                    .getCurrentWorker()
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> player.getMatch().getBillboard().getPlayer(position) == null)
                    .collect(Collectors.toSet());
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
        return availablePlacing;
    }

    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> computeAvailableMovements(Player player) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            Position currentPosition=player.getCurrentWorker().getPosition();

            availableMovements = player
                    .getCurrentWorker()
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) == null)
                    .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition))
                    .filter(position ->
                            player.getMatch().isMoveUpActive()
                            && billboard.getTowerHeight(position) == billboard.getTowerHeight(currentPosition)+1)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
            return availableMovements;
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }

    }

    /**
     * method that show the list of cells that are available for the standard building action of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can build on
     */
    public Set<Position> computeAvailableBuildings(Player player) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            availableBuildings = player
                    .getCurrentWorker()
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) == null)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
            return availableBuildings;
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

    @Override
    public void specialFunctionSetUnset() {

    }

    @Override
    public void reset(Player player) {
        availableBuildings = null;
        availablePlacing = null;
        availableMovements = null;
    }
}
