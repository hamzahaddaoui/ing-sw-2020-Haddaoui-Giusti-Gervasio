package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DemeterDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Demeter;

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
        player.getMatch().getBillboard().incrementTowerHeight(position);

        /*if(player.getTotalBuilds()==1) {
            positionBuilt=null;}*/
    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param player
     * @param position   the position that player have inserted, not null
     */
    public void build(Position position, Player player, boolean forceDome) {
       /* if(player.getTotalBuilds()==2)
            positionBuilt=position;
        if (forceDome){
            player.getMatch().getBillboard().setDome(position);}
        else
            build(position, player);*/
    }


    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        try{
            /*if(player.getTotalBuilds()==2){
                return computeAvailableFirstBuildings(player,worker);}
            else if(player.getTotalBuilds()==1){
                return computeAvailableSecondBuildings(player,worker);}
            else*/ return null;
        }
        catch(Exception ex){
            throw new NullPointerException();}
    }

    /**
     * method that show the list of cells that are available for the standard building action of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can build on
     */
    public Set<Position> computeAvailableFirstBuildings(Player player, Worker worker) {
        try{
            Billboard billboard=player.getMatch().getBillboard();

            return worker
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) == -1)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
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
    public Set<Position> computeAvailableSecondBuildings(Player player, Worker worker) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            return worker
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> position!=positionBuilt)
                    .filter(position -> billboard.getPlayer(position) == -1)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

}
