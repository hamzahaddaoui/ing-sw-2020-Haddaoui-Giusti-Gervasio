package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.utilities.Message;
import it.polimi.ingsw.utilities.Observer;

public class GameController implements Observer<Message> {
    @Override
    public void update(Message message) {
        if (message.getMatchID() == null){
            if (GameModel.getCurrentMatch() != null){
                if(GameModel.isNickAvailable(message.getPayload())){
                    GameModel.addPlayer(message.getPayload());
                }
            }
            else{
                //GameModel.createMatch();
                GameModel.addPlayer(message.getPayload());
            }
        }

        /*switch(message.getMatchID().getCurrentState){

        }*/


        /*


        switch(match.state)
        case waiting_for_players:
        if(getCurrentMatch().getPlayersCurrentCount() == getCurrentMatch().getPlayersNum())
            getCurrentMatch().matchStart();
        match.state++;
        break;
        case selecting_god_cards
        if(message.matchID.currentPlayer.getID == message.id)
            if (message instanceof godCard) matchid.addRemoveCardToMatch(matchid, message)
            else (message instanceof String){
            if (matchid.getPlayerNum == matchID.getGodCards.size())
                model.confirmCards(match);
            match.state++;
            break;
            case selecting_special_command:
                if(message.matchID.currentPlayer.getID == message.id)
                    //verifico che la carta faccia parte del set di carte del match, poi la associo al player
                    if (message instanceof godCard) matchid.selectPlayerCard(match, message)
                if (!isDeckFull()) match.state++;*/
    }
}
