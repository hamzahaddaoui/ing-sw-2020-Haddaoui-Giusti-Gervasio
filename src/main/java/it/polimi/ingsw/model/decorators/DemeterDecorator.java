package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.GameEntity;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.model.GodDecorator;

public class DemeterDecorator extends GodDecorator {
    static final GodCards card = GodCards.Demeter;
    public DemeterDecorator(GameEntity player){
        this.player=player;
    }
}
