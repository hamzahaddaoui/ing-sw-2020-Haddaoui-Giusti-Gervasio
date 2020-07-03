package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import javafx.geometry.Pos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.utilities.TurnState.*;

/**
 * @author: hamzahaddaoui
 *
 * Redefinition of methods related to the Atlas power.
 */

public class AtlasDecorator extends CommandsDecorator {

    static final private GodCards card = GodCards.Atlas;

    /**
     * decorate the object Command with Atlas's special power
     *
     * @param commands  represent the player behaviour
     */
    public AtlasDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * method that changes the current state of the player
     * If the state is IDLE then the player can MOVE
     * If the state is MOVE then the player can BUILD
     * If the state is BUILD then the player goes IDLE
     * @param player the match current player
     */
    @Override
    public void nextState(Player player) {
        switch(player.getTurnState()){
            case IDLE:
                player.setTurnState(MOVE);
                break;
            case MOVE:
                player.setUnsetSpecialFunctionAvailable(Collections.unmodifiableMap(new HashMap<Position, Boolean>(){{
                    player.getWorkers().forEach(worker -> put(worker.getPosition(), true));
                }
                }));
                player.setTurnState(BUILD);
                break;
            case BUILD:
                player.setHasFinished();
                break;
        }
    }


    /**
     * method that allows the standard building block action
     * the Atlas player can build a block or a dome, depending on the special function activation
     *
     * @param player
     * @param position  is the position that player have inserted
     */
    @Override
    public void build(Position position, Player player) {
        if (player.hasSpecialFunction())
            player.getMatch().getBillboard().setDome(position);
        else
            super.build(position, player);
    }

}
