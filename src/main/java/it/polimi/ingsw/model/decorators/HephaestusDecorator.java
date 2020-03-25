package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.GameEntity;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.model.GodDecorator;

public class HephaestusDecorator extends GodDecorator {
    static final GodCards card = GodCards.Hephaestus;
    public HephaestusDecorator(GameEntity player){
        this.player=player;
    }
}
