package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.GameEntity;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.model.GodDecorator;

public class AtlasDecorator extends GodDecorator {
    static final GodCards card = GodCards.Atlas;
    public AtlasDecorator(GameEntity player){
        this.player=player;
    }
}
