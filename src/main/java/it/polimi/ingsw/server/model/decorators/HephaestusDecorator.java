package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.*;

public class HephaestusDecorator extends CommandsDecorator {

    private Position firstBuildPosition;

    public HephaestusDecorator(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                firstBuildPosition = null;
                player.setTurnState(MOVE);
            case MOVE:
                player.setTurnState(BUILD);
            case BUILD:
                if (firstBuildPosition == null) {
                    player.setTerminateTurnAvailable();
                    player.setTurnState(BUILD);
                }
                else{
                    player.setHasFinished();
                    player.setTurnState(IDLE);
                }
            default:
                player.setTurnState(IDLE);
        }
    }

    @Override
    public void build(Position position, Player player) {
        super.build(firstBuildPosition, player);
        if (firstBuildPosition == null){
            position.setZ(player.getMatch().getBillboard().getTowerHeight(position));
            firstBuildPosition = position;
        }
    }


    /**
     * method that show the list of cells that are available for the standard building action of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can build on
     */
    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        try{
            Billboard billboard=player.getMatch().getBillboard();

            if (firstBuildPosition == null){
                return worker
                        .getPosition()
                        .neighbourPositions()
                        .stream()
                        .filter(position -> billboard.getPlayer(position) == -1)
                        .filter(position -> ! billboard.getDome(position))
                        .collect(Collectors.toSet());
            }
            else{
                if (firstBuildPosition.getZ() < 3)
                    return Collections.singleton(firstBuildPosition);
                else
                    return null;
            }

        }
        catch(Exception ex){
            throw new NullPointerException();
        }
    }

    @Override
    public boolean losingCondition(Player player){
        if (firstBuildPosition != null)
            return false;
        else
            return player
                    .getWorkers()
                    .stream()
                    .anyMatch(worker -> worker
                            .canDoSomething(player.getTurnState()));
    }


}