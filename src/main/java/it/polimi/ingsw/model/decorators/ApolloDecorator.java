package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.GameEntity;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.model.GodDecorator;
import it.polimi.ingsw.model.Player;

public class ApolloDecorator extends GodDecorator {
    static final GodCards card = GodCards.Apollo;
    public ApolloDecorator(GameEntity player){
        this.player=player;
    }
}
