package it.polimi.ingsw.client.controller;


import it.polimi.ingsw.client.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Controller extends Observable<MessageEvent> implements Observer<MessageEvent> {
    MessageEvent message;
    Set<String> selectedCards;
    Position position = null;
    String card;

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

    @Override
    public void update(MessageEvent viewString){
        //messaggio ricevuto dalla view!
        //lo elaboro e se valido, lo invio al server controller
        int num;
        String Subject = null;
        String Argument = null;
        position=null;
        analyzeMessage(Subject,Argument);

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
                if (position!=null  &&
                        !View.getPlayerState().equals("IDLE")) {
                    if (View.getPlayerState().equals("CHOOSING_WORKER")) {
                        message.setStartPosition(position);
                    }
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
                message.setNickname(Subject);
                notify(message);
        }

    }

    private boolean createPosition(String viewString) {
        return true;
    }

    private void analyzeMessage(String messageSubject, String messageArgument){
        messageArgument.replaceAll(" ","");
        switch(messageSubject.toString()){
            case "POSITION":
                checkValue(messageArgument);
                return;
            case "ENDTURN":
                message.setEndTurn(true);
                break;
            case "GODCARD":
                //controlla che il valore sia corretto
                //analizza lo stato del player ed in base a quello sceglie dove mettere il valore della carta
                break;
            case "NICKNAME":
                message.setNickname(messageSubject);
                break;
            case "NUM_OF_PLAYERS":
                checkPlayersNum(messageArgument);
                break;
            case "SPECIAL_FUNCTION" :
                //CONTROLLARE SE é UNA DELLE GIà SCELTE OPPURE SE é UNA TRA QUELLE POSSIBILI
            case "ENABLE_FUNCTION" :
                message.setSpecialFunction(true);
            default:
            }

    }

    private boolean checkValue(String messageArgument){
       int valueX,valueY;
       char[] messageParts;

       messageParts = messageArgument.replace(" ","").toCharArray();
       valueX= ((int) messageParts[0]) -48;
       valueY= ((int) messageParts[1]) -48;
       if((valueX >=0) && (valueY>=0) && (valueX<=4) && (valueY<=4)){
           this.position.setX(valueX);
           this.position.setY(valueY);
           return true;
        }
       else
           return false;
    }

    private boolean checkCard(String messageArgument){
        if(totalCards.contains(messageArgument))
                return true;
        else return false;
    }

    private boolean checkPlayersNum(String messageArgument){
        int value;
        value= (int)( messageArgument.toString().charAt(0))-48;
        if(messageArgument.length()==1 && value==2 || value==3){
            return true;
        }
        else
            return false;
    }
}
