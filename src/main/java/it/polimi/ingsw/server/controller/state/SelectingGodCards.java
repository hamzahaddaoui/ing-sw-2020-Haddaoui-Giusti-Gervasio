package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import static it.polimi.ingsw.server.model.GameModel.*;

public class SelectingGodCards extends State {
    @Override
    public void handleRequest(Integer matchID, MessageEvent messageEvent){
        setMatchCards(matchID, messageEvent.getGodCards());
        nextMatchState(matchID);
        nextMatchTurn(matchID);
    }

    @Override
    public void viewNotify(Integer matchID){
        MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
        message.setMatchCards(getGameCards());
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> notify(basicPlayerConfig(message, player)));
    }
}
