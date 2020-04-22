package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.*;

public class HephaestusDecorator extends CommandsDecorator {

    private Position firstBuildPosition;
    private boolean secondBuildDone;

    public HephaestusDecorator(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                secondBuildDone = false;
                firstBuildPosition = null;
                player.setTurnState(MOVE);
                break;
            case MOVE:
                player.setTurnState(BUILD);
                break;
            case BUILD:
                if (losingCondition(player) || secondBuildDone){
                    player.setHasFinished();
                }
                else{
                    player.setTerminateTurnAvailable();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void build(Position position, Player player) {
        if (firstBuildPosition == null){
            position.setZ(player.getMatch().getBillboard().getTowerHeight(position));
            firstBuildPosition = position;
        }
        else{
            secondBuildDone = true;
        }
        super.build(firstBuildPosition, player);
    }


    /**
     * method that show the list of cells that are available for the standard building action of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can build on
     */
    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        if (firstBuildPosition == null){
            return super.computeAvailableBuildings(player, worker);
        }
        else{
            if (firstBuildPosition.getZ() < 3)
                return Collections.singleton(firstBuildPosition);
            else
                return null;
        }


    }

}