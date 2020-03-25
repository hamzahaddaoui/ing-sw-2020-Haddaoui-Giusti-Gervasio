package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.GameEntity;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.model.GodDecorator;

public class MinotaurDecorator extends GodDecorator {
    static final GodCards card = GodCards.Minotaur;
    public MinotaurDecorator(GameEntity player){
        this.player=player;
    }
}
