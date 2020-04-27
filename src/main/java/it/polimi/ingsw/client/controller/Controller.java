package it.polimi.ingsw.client.controller;


import com.google.gson.Gson;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.state.ControlState;
import it.polimi.ingsw.client.controller.state.StartingStatus;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

public class Controller extends Observable<MessageEvent> implements Observer {

    private ControlState controlState;
    private PlayerState playerState;
    private MatchState matchState;
    private static MessageEvent message;
    private boolean messageReady = false;

    public Controller() {
        controlState = new StartingStatus();
        playerState = null;
        matchState = null;
        message = new MessageEvent();
    }

    @Override
    public void update(Object viewObject) {

        messageReady = false;
        message = null;

        PlayerState newPlayerState = View.getPlayerState();
        MatchState newMatchState = View.getMatchState();

        // CASO if -> sono nel inserimento Nickname
        // Caso else -> altro caso

        if(newMatchState == null && newPlayerState == null){
            reset();
        }
        else if(newPlayerState != this.playerState || newMatchState != this.matchState){
            this.playerState = newPlayerState;
            this.matchState = newMatchState;
            nextState();
        }

        //agisco andando a processare il messaggio dopo aver effettuato il controllo
        messageReady = controlState.processingMessage(viewObject);

        if (messageReady) {
            if (playerState != null)
                message.setPlayerID(View.getPlayerID());
            if (matchState != null)
                message.setMatchID(View.getMatchID());
            //invio il messaggio precedentemente compilato
            notify(message);
            //dopo l'invio reset degli attributi del messaggio a null
            reset();
        }

    }

    public void nextState(){
        this.controlState.nextState(this);
    }

    public MatchState getMatchState() {
        return this.matchState;
    }

    public void setMatchState(MatchState matchState){
        this.matchState = matchState;
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    public void setPlayerState(PlayerState playerState){
        this.playerState = playerState;
    }

    public void setState(ControlState ctrlState){
        this.controlState=ctrlState;
    }

    public static MessageEvent getMessage(){
        return message;
    }

    public ControlState getControlState() {return this.controlState;}

    public void setPlayerAndMatchState(PlayerState plState,MatchState matState) {
        playerState = plState;
        matchState = matState;
    }


    public boolean isMessageReady(){
        return messageReady;
    }

    public void reset (){
        message.setMatchID(null);
        message.setPlayerID(null);
        message.setNickname(null);
        message.setPlayersNum(2);
        message.setGodCards(null);
        message.setGodCard(null);
        message.setInitializedPositions(null);
        message.setStartPosition(null);
        message.setEndPosition(null);
        message.setEndTurn(null);
        message.setSpecialFunction(null);
        message.setExit(false);
        message.setMatchState(null);
        message.setPlayerState(null);
        message.setTurnState(null);
        message.setError(false);
        message.setMatchCards(null);
        message.setAvailablePlacingCells(null);
        message.setBillboardStatus(null);
        message.setWorkersAvailableCells(null);
        message.setTerminateTurnAvailable(null);
        message.setSpecialFunctionAvailable(null);
        message.setMatchPlayers(null);
        message.setWinner(null);
        message.setFinished(false);
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
