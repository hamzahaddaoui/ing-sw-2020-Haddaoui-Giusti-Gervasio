package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.GameEntity;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.model.GodDecorator;

public class PanDecorator extends GodDecorator {
    static final GodCards card = GodCards.Pan;
    public PanDecorator(GameEntity player){
        this.player=player;
    }
}
