package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.Commands;
import it.polimi.ingsw.server.model.CommandsDecorator;

public class ZeusDecorator  extends CommandsDecorator {
    public ZeusDecorator(Commands commands){
        this.commands=commands;
    }
}
