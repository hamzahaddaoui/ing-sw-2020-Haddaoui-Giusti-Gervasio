package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.Commands;
import it.polimi.ingsw.server.model.CommandsDecorator;

public class CharonDecorator  extends CommandsDecorator {

    public CharonDecorator(Commands commands){
        this.commands=commands;
    }
}
