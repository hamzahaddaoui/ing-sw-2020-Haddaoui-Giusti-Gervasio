package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.Commands;
import it.polimi.ingsw.server.model.CommandsDecorator;

public class ErosDecorator  extends CommandsDecorator {
    public ErosDecorator(Commands commands){
        this.commands=commands;
    }
}
