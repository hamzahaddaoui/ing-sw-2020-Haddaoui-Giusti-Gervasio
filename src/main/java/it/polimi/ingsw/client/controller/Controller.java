package it.polimi.ingsw.client.controller;


import com.google.gson.Gson;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.ViewControllerEvent;
import it.polimi.ingsw.server.model.GodCards;
import it.polimi.ingsw.server.model.MatchState;
import it.polimi.ingsw.server.model.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.Position;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Controller extends Observable<String> implements Observer<String> {

    private ControlState controlState= new StartingStatus();
    private String playerState;
    private String matchState;
    private MessageEvent message;
    private boolean messageReady;

    @Override
    public void update(String viewMessage){
        messageReady=false;
        message = null;
        String messageString=viewMessage;
        String newPlayerState = View.getPlayerState();
        String newMatchState =View.getMatchState();
        if(newPlayerState==playerState && newMatchState==matchState) {
            //non aggiorno il playerState e matchState lato controller -> resto nello stesso Stato
        }
        else {
            playerState=newPlayerState;
            matchState=newMatchState;
            nextState();
        }

        /*if(controlState.getMessageReady()!=null){
            mgEvent=controlState.getMessageReady();
        }*/

        message.setMatchID(View.getMatchID());
        message.setPlayerID(View.getPlayerID());
        String json = new Gson().toJson(message);
        notify(json);
    }

    public void nextState(){
        controlState.nextState(this);
    }

    public String getMatchState() {
        return matchState;
    }

    public String getPlayerState() {
        return playerState;
    }

    public void setState(ControlState ctrlState){
        this.controlState=ctrlState;
    }


    /*MessageEvent message; //
    Set<String> selectedCards;
    Position startPosition;
    Position endPosition;
    String cardChecked;
    int playersNum;
    boolean endTurn;
    boolean specialFunction;
    boolean readyToSend;

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
    public void update(String viewMessage){
        String Subject = viewMessage.getMessageSubject();
        String Argument = viewMessage.getMessageArgument();
        endTurn=false;
        specialFunction=false;
        endPosition=null;
        playersNum=0;
        readyToSend=false;
        analyzeMessage(Subject,Argument);

        switch (View.getMatchState()) {
            case "GETTING_PLAYERS_NUM":
                    if (View.getPlayerState().equals("SETTING_MATCH")) {
                        this.message.setPlayersNum(this.playersNum);
                        readyToSend=true;
                    }
                    else
                    //cambio view: "Non sei te il player designato"
                break;
            case "WAITING_FOR_PLAYERS" :
                //non faccio nulla
                //(cambio view tempo residuo)
                break;
            case "SELECTING_GOD_CARDS":
                if (View.getPlayerState().equals("SETTING_MATCH")) {
                        if (selectedCards.contains(cardChecked)) {
                            //cambio view: carta già presente nel mazzo
                        }
                        selectedCards.add(cardChecked);
                        if (selectedCards.size()==View.getPlayersNum()) {
                            message.setGodCards(selectedCards);
                            readyToSend=true;
                        }
                }
                else
                    //cambio view: "Non sei te il player designato"
                break;
            case "SELECTING_SPECIAL_COMMANDS":
                if (View.getPlayerState().equals("SELECTING_CARDS") &&
                        View.getGodCards().contains(cardChecked)) {
                    message.setGodCard(cardChecked);
                    readyToSend=true;
                }
                else
                    //cambio view 1: "Carta non disponibile"
                    //cambio view 2: "Non è il tuo turno"
                break;
            case "PLACING WORKER":
                if (View.getPlayerState().equals("PLACING") &&
                        View.getPlacingAvailableCells().contains(startPosition)) {
                    message.setEndPosition(startPosition);
                    startPosition=null;
                    readyToSend=true;
                }
                else
                    //cambio view 1: "Non è il tuo turno"
                    //cambio view 2: "Posizione non valida"
                break;
            case "RUNNING":
                if (endTurn)
                    message.setEndTurn(true);
                else if (specialFunction)
                    message.setSpecialFunction(true);
                else if (!View.getPlayerState().equals("IDLE")) {
                        if (View.isWorkerPresent(startPosition) &&
                                endPosition != null &&
                                View.getWorkersAvailableCells(startPosition).contains(endPosition)) {
                                    message.setStartPosition(startPosition);
                                    message.setEndPosition(endPosition);
                                    startPosition = null;
                                    readyToSend=true;
                            }
                        else
                            //cambio view 1: "Worker non esistente"
                            //cambio view 2: "Posizione non accessibile"
                            //cambio view 3: "Inserisci posizione di destinazione"
                        break;
                        }
                else
                    //cambio view: "Non è il tuo turno!"
                break;
            case "FINISHED":
                //non faccio nulla
                //(cambio view tempo residuo)
                break;
            default:
                //caso nickname
                message.setNickname(Subject);
                readyToSend=true;
        }

        if (readyToSend) {
            message.setMatchID(View.getMatchID());
            message.setPlayerID(View.getPlayerID());
            String json = new Gson().toJson(message);
            notify(json);
        }
    }

    private void analyzeMessage(String messageSubject, String messageArgument){
        messageArgument=messageArgument.replaceAll(" ","");

        switch(messageSubject.toString()){
            case "POSITION":
                if (!checkValue(messageArgument))
                    //cambio view: posizione errata
                break;
            case "ENDTURN":
                endTurn=true;
                break;
            case "GODCARD":
                if (!checkGodCard(messageArgument))
                    //cambio view: carta non esistente
                break;
            case "NICKNAME":
                break;
            case "NUM_OF_PLAYERS":
                if(!checkPlayersNum(messageArgument))
                    //cambio view: numero non corretto
                break;
            case "SPECIAL_FUNCTION" :
                specialFunction=true;
                break;
            }

    }

    private boolean checkValue(String messageArgument){
       int valueX,valueY;
       char[] messageParts;

       messageParts = messageArgument.replace(",","").toCharArray();
       if(messageParts.length!=2) return false;
       valueX= ((int) messageParts[0]) -48;
       valueY= ((int) messageParts[1]) -48;
       if((valueX >=0) && (valueY>=0) && (valueX<=4) && (valueY<=4)){
            if (startPosition==null) {
                this.startPosition=new Position(valueX,valueY);
            }
            else {
                this.endPosition=new Position(valueX,valueY);
            }
             return true;}
       return false;
    }

    private boolean checkPlayersNum(String messageArgument){
        int value;
        value= (int)( messageArgument.toString().charAt(0))-48;
        if(value==2 || value==3){
            playersNum=value;
            return true;
        }
        else
            return false;
    }

    private boolean checkGodCard(String messageArgument){
        this.cardChecked = totalCards
                .stream()
                .filter(card -> card.equals(messageArgument.toUpperCase()))
                .findAny()
                .get();

        return !this.cardChecked.equals("");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP){

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }*/

}
