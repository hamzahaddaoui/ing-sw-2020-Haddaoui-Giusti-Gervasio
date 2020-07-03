package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;

import java.util.List;

import static it.polimi.ingsw.server.model.GameModel.*;
import static it.polimi.ingsw.server.model.GameModel.nextMatchState;

/**
 * @author: hamzahaddaoui
 *
 * Controller class that manages the selecting command inputs by the users.
 * Checks if the card selected is available.
 */

public class SelectingSpecialCommand extends State {
    @Override
    public boolean handleRequest(MessageEvent messageEvent){
        ClientHandler clientHandler = messageEvent.getClientHandler();
        int matchID = clientHandler.getMatchID();
        int playerID = clientHandler.getPlayerID();
        String card = messageEvent.getGodCard();

        if (!getMatchCards(matchID).contains(card)){
            notify(List.of(messageEvent.getClientHandler()), basicErrorConfig((basicPlayerConfig(basicMatchConfig(new MessageEvent(), matchID), playerID))));
            return false;
        }

        selectPlayerCard(matchID, card);
        nextMatchTurn(matchID);
        if (hasSelectedCard(matchID)) {
            nextMatchState(matchID);
        }
        return true;
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
        MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
        message.setMatchCards(getMatchCards(matchID));
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> notify(observers, basicPlayerConfig(message, player)));
    }
}
