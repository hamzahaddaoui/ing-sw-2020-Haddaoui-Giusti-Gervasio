package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class PanDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Pan;
    public PanDecorator(Commands commands){
        this.commands=commands;
    }

   //winningConditions to implement
}
