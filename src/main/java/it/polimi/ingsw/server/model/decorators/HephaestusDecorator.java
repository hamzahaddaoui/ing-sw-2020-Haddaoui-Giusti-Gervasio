package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

public class HephaestusDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Hephaestus;

    private Position firstBuildPosition;

    public HephaestusDecorator(Commands commands) {
        this.commands = commands;
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
            if (player.isSpecialFunction() && firstBuildPosition.getZ()<3)
                super.build(firstBuildPosition, player);
        }

    }


}