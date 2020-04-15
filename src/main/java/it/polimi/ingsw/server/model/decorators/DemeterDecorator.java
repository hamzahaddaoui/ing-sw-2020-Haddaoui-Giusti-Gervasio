package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.model.TurnState.*;
import static it.polimi.ingsw.server.model.TurnState.IDLE;

public class DemeterDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Demeter;

    private Position firstBuildPosition;

    /**
     * decorate the object Command with Demeter's special power
     *
     * @param commands represent the player behaviour
     */
    public DemeterDecorator(Commands commands){
        this.commands=commands;
        this.firstBuildPosition=null;
    }

    @Override
    public TurnState nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                return MOVE;
            case MOVE:
                this.firstBuildPosition=null;
                return BUILD;
            case BUILD:
                if (this.firstBuildPosition!=null)
                    if(player.getSpecialFunction())
                        return BUILD;
            default:
                this.firstBuildPosition=null;
                player.setHasFinished();
                return IDLE;
        }
    }


    @Override
    public void build(Position position, Player player) {
        super.build(position, player);
        if (this.firstBuildPosition == null){
            this.firstBuildPosition = position;
        }
        else {
            if (player.getSpecialFunction() && this.firstBuildPosition != null) {
                this.firstBuildPosition=null;
            }
        }

    }

    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        Set<Position> result=super.computeAvailableBuildings(player, worker);
            if(this.firstBuildPosition==null)
                return result;
            else{
                return result
                        .stream()
                        .filter(position -> position!=firstBuildPosition)
                        .collect(Collectors.toSet());
            }
        }

}
