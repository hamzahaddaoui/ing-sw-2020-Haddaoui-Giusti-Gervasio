package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;

public class PanDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Pan;
    public PanDecorator(Commands commands){
        this.commands=commands;
    }

   //winningConditions to implement
}
