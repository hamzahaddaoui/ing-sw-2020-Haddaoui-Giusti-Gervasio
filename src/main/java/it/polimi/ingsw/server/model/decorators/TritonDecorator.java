package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.Commands;
import it.polimi.ingsw.server.model.CommandsDecorator;

public class TritonDecorator  extends CommandsDecorator {
    public TritonDecorator(Commands commands){
        this.commands=commands;
    }
}
