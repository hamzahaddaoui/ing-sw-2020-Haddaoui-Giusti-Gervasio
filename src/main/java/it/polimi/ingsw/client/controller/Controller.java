package it.polimi.ingsw.client.controller;


import com.google.gson.Gson;
import it.polimi.ingsw.client.Position;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.GodCards;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.TurnState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Controller extends Observable<MessageEvent> implements Observer<String> {
MessageEvent message;
private ArrayList<String> totalCards = new ArrayList<String>(Arrays.asList(
        "APOLLO",
        "ARTEMIS",
        "ATHENA",
        "ATLAS",
        "DEMETER",
        "HEPHAESTUS",
        "MINOTAUR",
        "PAN",
        "PROMETHEUS"));
Set<String> selectedCards;
Position position;

    @Override
    public void update(String viewString){
        //messaggio ricevuto dalla view!
        //lo elaboro e se valido, lo invio al server controller

        switch (View.getMatchState()) {
            case "GETTING_PLAYERS_NUM":
                //check TurnState = SETTING_MATCH
                //ci aspettiamo un int, con valore o 2 o 3
                //altrimenti lancio errore
                    if (View.getPlayerState().equals("SETTING_MATCH") &&
                            (message.getPlayersNum() == 2 ||
                                    message.getPlayersNum() == 3))
                        notify(message);
                    else
                    //lancio errore 1 : "Numero di player inacettabile"
                    //lancio errore 2 : "Non sei te il player designato"
                return;
            case "WAITING_FOR_PLAYERS" :
                //non faccio nulla
                //(cambio view tempo residuo)
                return;
            case "SELECTING_GOD_CARDS":
                //check TurnState = SETTING_MATCH
                //check stringhe disponibili
                //controllo che numero carte = players_Num
                String messageCard = message.getGodCard().toUpperCase();
                if (View.getPlayerState().equals("SETTING_MATCH") &&
                        totalCards.contains(messageCard)) {
                    selectedCards.add(messageCard);
                    if (selectedCards.size()==View.getPlayersNum()) {
                        message.setGodCards(selectedCards);
                        notify(message);
                    }
                }
                else
                    //Errore 1: "Non sei te il player designato"
                    //Errore 2: "Divinità non esistente"
                return;
            case "SELECTING_SPECIAL_COMMANDS":
                //check TurnState = SELECTING_CARDS
                if (View.getPlayerState().equals("SELECTING_CARDS") &&
                        View.getGodCards().contains(message.getGodCard()))
                    notify(message);
                else
                    //Errore 1: "Carta non disponibile"
                    //Errore 2: "Non è il tuo turno"
                return;
            case "PLACING WORKER":
                //check TurnState = PLACING
                //costruito posizione
                if (View.getPlayerState().equals("PLACING") &&
                        View.getWorkersAvailableCells(message.getStartPosition()).contains(message.getEndPosition())) {
                   notify(message);
                }
                return;
            case "RUNNING":
                //check TurnState != IDLE
                //trasformo in position la x e la y che mi arrivano
                //check su availablePosition
                //metodo per costruire posizione con check su estremi billboard
                if (createPosition(viewString) &&
                        !View.getPlayerState().equals("IDLE")) {
                    if (View.getPlayerState().equals("CHOOSING_WORKER"))
                        message.setStartPosition(position);
                    else {
                        //View.getWorkersAvailableCells(message.getStartPosition()).contains(message.getEndPosition())
                        message.setEndPosition(position);
                        notify(message);
                    }
                }
                return;
            case "FINISHED":
                //non faccio nulla
                //(cambio view tempo residuo)
                return;
            default:
                //caso nickname
                message.setNickname(viewString);
                notify(message);
        }

    }

    private boolean createPosition(String viewString) {
        return true;
    }
}
