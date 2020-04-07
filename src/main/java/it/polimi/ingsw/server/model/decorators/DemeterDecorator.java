package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.model.TurnState.*;
import static it.polimi.ingsw.server.model.TurnState.WAIT;

public class DemeterDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Demeter;

    private Position firstBuildPosition=null;

    /**
     * decorate the object Command with Demeter's special power
     *
     * @param commands represent the player behaviour
     */
    public DemeterDecorator(Commands commands){
        this.commands=commands;
    }

    @Override
    public TurnState nextState(Player player) {
        switch (player.getState()) {
            case WAIT:
                return MOVE;
            case MOVE:
                firstBuildPosition=null;
                return BUILD;
            case BUILD:
                if (player.getSpecialFunction() && firstBuildPosition != null){
                    return BUILD;}
            default:
                player.setHasFinished(true);
                return WAIT;
        }
    }


    @Override
    public void build(Position position, Player player) {
        if (firstBuildPosition == null){
            super.build(position, player);
            firstBuildPosition = position;
            position.setZ(player.getMatch().getBillboard().getTowerHeight(position));
        }
        else {
            if (player.getSpecialFunction()) {
                super.build(firstBuildPosition, player);
            }
        }

    }




    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        try{
            if(firstBuildPosition==null)
                return super.computeAvailableBuildings(player, worker);
            else{
                return computeAvailableSpecialBuildings(player, worker);
            }
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
    public Set<Position> computeAvailableSpecialBuildings(Player player, Worker worker) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            Set<Position> result=super.computeAvailableBuildings(player, worker);

            return result
                    .stream()
                    .filter(position -> position!=firstBuildPosition)
                    .collect(Collectors.toSet());
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

}
