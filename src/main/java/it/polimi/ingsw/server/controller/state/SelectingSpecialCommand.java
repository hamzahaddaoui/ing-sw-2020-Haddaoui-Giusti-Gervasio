package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;

import java.util.Set;

import static it.polimi.ingsw.server.model.GameModel.*;
import static it.polimi.ingsw.server.model.GameModel.nextMatchState;

public class SelectingSpecialCommand extends State {
    @Override
    public void handleRequest(MessageEvent messageEvent){
        Integer matchID = messageEvent.getMatchID();
        String card = messageEvent.getGodCard();

        if (!getMatchCards(matchID).contains(card)){
            notify(basicErrorConfig(basicMatchConfig(basicPlayerConfig(new MessageEvent(), messageEvent.getPlayerID()),matchID)));
            return;
        }

        selectPlayerCard(matchID, messageEvent.getGodCard());
        nextMatchTurn(matchID);
        if (hasSelectedCard(matchID)) {
            nextMatchState(matchID);
        }
    }

    @Override
    public void viewNotify(Integer matchID){
        MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
        message.setMatchCards(getMatchCards(matchID));
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> notify(basicPlayerConfig(message, player)));
    }
}
