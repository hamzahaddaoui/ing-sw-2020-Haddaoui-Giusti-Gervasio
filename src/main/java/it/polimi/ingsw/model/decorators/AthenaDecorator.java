package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.GameEntity;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.model.GodDecorator;

public class AthenaDecorator extends GodDecorator {
    static final GodCards card = GodCards.Athena;
    public AthenaDecorator(GameEntity player){
        this.player=player;
    }
}
