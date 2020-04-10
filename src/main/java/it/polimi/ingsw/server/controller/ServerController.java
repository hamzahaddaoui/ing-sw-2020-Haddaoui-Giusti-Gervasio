package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utilities.Observer;

public class ServerController implements Observer {

    @Override
    public void update(Object message){

        return;
    }

    @Override
    public void update(Integer matchID, Integer playerID, Object message){
        //parsing dell'oggetto vcevent
        if (matchID == null && GameModel.getMatchID(playerID) != null) {
            Server.setUsersMatches(playerID, matchID);
        }

    }

        /*if (message.getMatchID() == null){
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
*/
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


/* selezione worker 0-1-2-3... (integer)
    selezione posizione (Position)
    selezione boolean
 */


