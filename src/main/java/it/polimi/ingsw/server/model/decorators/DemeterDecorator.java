package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DemeterDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Demeter;


    private int movesBeforeBuild = 1;
    private int numOfBuilds = 2;
    private int movesAfterBuild = 0;
    private boolean doneStandard = false;
    private boolean positionedWorkers = false;
    private Position positionBuilt=null;

    /**
     * decorate the object Command with Demeter's special power
     *
     * @param commands represent the player behaviour
     */
    public DemeterDecorator(Commands commands){
        this.commands=commands;
    }

    @Override
    public void build(Position position, Player player) {
        Set<Position> availableBuildings=null;
        if(numOfBuilds==2){
            availableBuildings=computeAvailableBuildings(player);}
        if(numOfBuilds==1){
            availableBuildings=computeAvailableSecondBuildings(player);}
        if (!availableBuildings.contains(position))
            return;

        player.getMatch().getBillboard().incrementTowerHeight(position);
        numOfBuilds--;
        if(numOfBuilds==0) {
            player.setState(TurnState.END);}
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
        Set<Position> availableBuildings=null;
        if(numOfBuilds==2)
            positionBuilt=position;
        if (forceDome){
            if(numOfBuilds==2){
            availableBuildings=computeAvailableBuildings(player);}
            if(numOfBuilds==1){
            availableBuildings=computeAvailableSecondBuildings(player);}
            if (!availableBuildings.contains(position))
                return;
            player.getMatch().getBillboard().setDome(position);
            numOfBuilds--;}
        else
            build(position, player);
    }

    /**
     * method that divide the different implementation of available cells: for building action and for movement action
     *
     * @param player  is the current player
     * @return  the list of Position that are available for that specific action
     */
    public Set<Position> getAvailableCells(Player player) {
        try{
            switch (player.getState()){
                case PLACING:
                    return computeAvailablePlacing(player);
                case MOVE:
                    return computeAvailableMovements(player);
                case BUILD:
                    return computeAvailableBuildings(player);
                default:
                    return null;
            }
        } catch(NullPointerException ex){
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
            Set<Position> availableBuildings;
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

    /**
     * method that show the list of cells that are available for the standard building action of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can build on
     */
    public Set<Position> computeAvailableSecondBuildings(Player player) {
        try{
            Set<Position> availableBuildings;
            Billboard billboard=player.getMatch().getBillboard();
            availableBuildings = player
                    .getCurrentWorker()
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> position!=positionBuilt)
                    .filter(position -> billboard.getPlayer(position) == null)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
            return availableBuildings;
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

}
