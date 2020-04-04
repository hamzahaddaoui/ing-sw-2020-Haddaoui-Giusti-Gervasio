package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import static it.polimi.ingsw.server.model.TurnState.*;

public class HephaestusDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Hephaestus;

    private Position firstBuildPosition;

    public HephaestusDecorator(Commands commands) {
        this.commands = commands;
    }


    @Override
    public TurnState nextState(Player player) {
        switch(player.getState()){
            case WAIT:
                return MOVE;
            case MOVE:
                return BUILD;
            case BUILD:
                if (player.getSpecialFunction() && firstBuildPosition == null)
                    return BUILD;
                else
                    return WAIT;
            default:
                return WAIT;
        }
    }

    @Override
    public void build(Position position, Player player) {
        super.build(position, player);
        if (firstBuildPosition == null){
            super.build(position, player);
            firstBuildPosition = position;
            position.setZ(player.getMatch().getBillboard().getTowerHeight(position));
        }
        else{
            if (player.getSpecialFunction() && firstBuildPosition.getZ() < 3)
                super.build(firstBuildPosition, player);
        }

    }


}