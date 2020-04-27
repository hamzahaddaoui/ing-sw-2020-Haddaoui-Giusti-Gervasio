package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;

import java.util.List;
import java.util.Set;

import static it.polimi.ingsw.server.model.GameModel.*;

public class SelectingGodCards extends State {
    @Override
    public void handleRequest(MessageEvent messageEvent){
        Integer matchID = messageEvent.getMatchID();
        Set<String> cardList = messageEvent.getGodCards();

        if (cardList.size()==getMatchPlayers(matchID).size()
            &&!getGameCards().containsAll(cardList)
            && (cardList.size() != cardList.stream().distinct().count())){

            notify(basicErrorConfig(basicMatchConfig(basicPlayerConfig(new MessageEvent(), messageEvent.getPlayerID()),matchID)));
            return;
        }


        setMatchCards(matchID, cardList);
        nextMatchState(matchID);
        nextMatchTurn(matchID);
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
        MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
        message.setMatchCards(getGameCards());
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> notify(observers, basicPlayerConfig(message, player)));
    }

    @Override
    public void exit(Integer matchID){

    }
}
