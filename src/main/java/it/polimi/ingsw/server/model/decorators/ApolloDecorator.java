package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApolloDecorator extends CommandsDecorator {
    private GodCards card = GodCards.Apollo;

    private int movesBeforeBuild = 2;
    private int numOfBuilds = 1;
    private int movesAfterBuild = 1;
    private boolean doneStandard = false;
    private boolean positionedWorkers = false;

    /**
     * decorate the object Command with Apollo's special power
     *
     * @param commands represent the player behaviour
     */
    public ApolloDecorator(Commands commands){
        this.commands=commands;
    }

    public GodCards getCard () {
        return card;
    }

    /**
     * method that allows the stardard placing movement
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void placeWorker(Position position, Player player) {
        super.placeWorker(position,player);
    }

    /**
     * worker may move into ah opponent Worker's space by forcing their worker to the space yours just vacated
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();
        Set<Position> availableMovements = computeAvailableMovements(player);
        if (!availableMovements.contains(position))
            return;

        position.setZ(billboard.getTowerHeight(position));
        billboard.resetPlayer(worker.getPosition());
        worker.setPosition(position);
        billboard.setPlayer(position, worker);
    }

    @Override
    public void build(Position position, Player player) {
        super.build(position,player);
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
        super.build(position,player,forceDome);
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
                    return computeAvailablePlacing(player);
                case MOVE:
                    return computeAvailableMovements(player);;
                case BUILD:
                    return computeAvailableBuildings(player);;
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
        return super.computeAvailablePlacing(player);
    }

    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> ComputeAvailableMovement(Player player) {
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

    public Set<Position> computeAvailableBuilding(Player player) {
      super.computeAvailableBuildings(player);
    }

    /**
     * method that allows the standard building dome action
     * the player can build a dome on an unoccupied space neighbouring the worker
     * @param worker is the player's selected worker
     * @param position is the position that player have inserted
     * @param billboard is the reference to the gameboard
     */


    public boolean hasWon() {
        return false;
    }


    public boolean hasLost() {
        return false;
    }


    public boolean hasMoved() {
        return false;
    }


    public boolean hasBuilt() {
        return false;
    }


    public boolean hasDoneStandard() {
        return false;
    }
}
