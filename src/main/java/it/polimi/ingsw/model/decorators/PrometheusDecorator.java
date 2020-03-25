package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.GameEntity;
import it.polimi.ingsw.model.GodCards;
import it.polimi.ingsw.model.GodDecorator;

public class PrometheusDecorator extends GodDecorator {
    static final GodCards card = GodCards.Prometheus;
    public PrometheusDecorator(GameEntity player){
        this.player=player;
    }
}
