package it.polimi.ingsw.server.controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.MessageType;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

public class Controller extends Observable<MessageEvent> implements Observer<MessageEvent> {
    Integer matchID, playerID;

    /*
    *  - RAGGRUPPARE PARTI SIMILI IN FUNZIONI
    *  - AGGIUNGERE AGGIORNAMENTI MATCHSTATE DOPO OGNI FUNZIONE
    *  - SE C'è DA AGGIORNARE LA VIEW, CREARE UN MESSAGGIO, E FARE NOTIFY
    *
    * */



    @Override
    public void update(MessageEvent message){
        playerID = message.getPlayerID();
        matchID = message.getMatchID();

        if (matchID == null) {
            matchIDUnavailable(message);
        }

        switch (GameModel.getMatchState(matchID)){
            case "GETTING_PLAYERS_NUM":
                Integer opponentID;
                int playersNum = message.getPlayersNum();

                if(GameModel.getMatchPlayers(matchID).get(0) == playerID){
                    GameModel.setMatchPlayersNum(matchID, playersNum);

                    while(!GameModel.isPlayersWaitingListEmpty()  && GameModel.isNumReached(matchID)){
                        opponentID = GameModel.unstackPlayerToMatch(matchID);
                        Server.getSocket(opponentID).setMatchID(matchID);
                    }

                    if (GameModel.isNumReached(matchID)) {
                        GameModel.startMatch(matchID);
                        //CAMBIO LO STATO, CAMBIO LA VIEW
                        //SELECTING_GOD_CARDS
                    }
                    else{
                        //CAMBIO STATO IN WAITING_FOR_PLAYERS
                        //schermata di attesa per tutti i giocatori
                    }
                }
                else {
                    //OPERAZIONE NON CONSENTITA
                    return;
                }

                /*
                * Verifico che playerID è il player che si trova nel match
                * Setto il numero di player
                * Aggiungo i player dalla lista d'attesa
                *
                * */
                break;
        }


    }

    private void matchIDUnavailable(MessageEvent message){
        String nickname = message.getNickname();

        if (GameModel.isPlayersWaitingListEmpty()) {
            matchID = GameModel.getAvailableMatchID();
            if (matchID != null) {  //addPlayer
                GameModel.isNickAvailable(nickname);
                GameModel.addPlayer(matchID, playerID, nickname);
                Server.getSocket(playerID).setMatchID(matchID);
                if (GameModel.isNumReached(matchID)) {
                    GameModel.startMatch(matchID);
                }
            } else { //aggiungi alla lista
                if (GameModel.isNotInitializedMatchPresent()) {
                    GameModel.playerAddToWaitingList(playerID, nickname);
                } else { //crea match
                    matchID = GameModel.createMatch();
                    GameModel.addPlayer(matchID, playerID, nickname);
                    Server.getSocket(playerID).setMatchID(matchID);
                }
            }
        } else {  //se la lista è popolata
            if (GameModel.isNewMatchinstantiable()) { //crea match
                matchID = GameModel.createMatch();
                GameModel.addPlayer(matchID, playerID, nickname);
                Server.getSocket(playerID).setMatchID(matchID);
            } else { //aggiungi alla lista
                if (GameModel.isNickAvailable(nickname)) {
                    GameModel.playerAddToWaitingList(playerID, nickname);
                } else { //errore - return
                    //notify(new MessageEvent(matchID, playerID, "Nickname_Not_Available"));
                    return; //NICKNAME ERROR
                }


            }
        }
    }



        /*AGGIORNO LA VIEW
        MessageEvent = new MessageEvent()
        notify();
*/
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


/* selezione worker 0-1-2-3... (integer)
    selezione posizione (Position)
    selezione boolean
 */


