package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Commands;
import it.polimi.ingsw.model.CommandsDecorator;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.utilities.Position;

public class AthenaDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Athena;
    public AthenaDecorator(Commands commands){
        this.commands=commands;
    }

    @Override
    public void placeWorker(Position position) {

    }

    @Override
    public void moveWorker(Position position) {

    }

    @Override
    public void build(Position position) {

    }
}
