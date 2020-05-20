package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;

import java.util.List;
import java.util.Set;

import static it.polimi.ingsw.server.model.GameModel.*;

public class SelectingGodCards extends State {
    @Override
    public boolean handleRequest(MessageEvent messageEvent){
        ClientHandler clientHandler = messageEvent.getClientHandler();
        int matchID = clientHandler.getMatchID();
        int playerID = clientHandler.getPlayerID();
        Set<String> cardList = messageEvent.getMatchCards();

        if (cardList.size()!=getMatchPlayers(matchID).size() || !getGameCards().containsAll(cardList) || (cardList.size() != cardList.stream().distinct().count())){
            notify(List.of(messageEvent.getClientHandler()), basicErrorConfig((basicPlayerConfig(basicMatchConfig(new MessageEvent(), matchID), playerID))));
            return false;
        }

        setMatchCards(matchID, cardList);
        nextMatchState(matchID);
        nextMatchTurn(matchID);
        return true;
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
        MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
        message.setMatchCards(getGameCards());
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> notify(observers, basicPlayerConfig(message, player)));
    }

}
