package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.*;

/**
 * @author: hamzahaddaoui
 *
 * Redefinition of methods related to the Hephaestus power.
 */

public class HephaestusDecorator extends CommandsDecorator {

    static final private GodCards card = GodCards.Hephaestus;

    private Position firstBuildPosition;
    private boolean secondBuildDone;

    public HephaestusDecorator(Commands commands) {
        this.commands = commands;
    }

    /**
     * method that changes the current state of the player
     * If the state is IDLE then the player can MOVE
     * If the state is MOVE then the player can BUILD
     * If the state is BUILD then the player goes IDLE or BUILD again
     * @param player the match current player
     */
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
                if (losingCondition(player) || secondBuildDone || firstBuildPosition.getZ() >= 3){
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

    /**
     * method that allows the standard player build
     * the player can build with the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard if the position that is selected is free
     * If the player has already built, it allows a second build in the same position
     *  @param position   the position that player have inserted, not null
     * @param player the match current player
     */
    @Override
    public void build(Position position, Player player) {
        if (firstBuildPosition == null){
            firstBuildPosition = position;
        }
        else{
            secondBuildDone = true;
        }
        super.build(firstBuildPosition, player);
        position.setZ(player.getMatch().getBillboard().getTowerHeight(position));
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
            return Collections.singleton(firstBuildPosition);
        }


    }


}