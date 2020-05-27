package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.Commands;
import it.polimi.ingsw.server.model.CommandsDecorator;

public class HestiaDecorator  extends CommandsDecorator {
    public HestiaDecorator(Commands commands){
        this.commands=commands;
    }
}
