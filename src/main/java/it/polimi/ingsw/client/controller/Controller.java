package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Observable<MessageEvent> implements Observer<String>  {

    static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static boolean activeInput = true;
    private static boolean messageReady;

    public static void setActiveInput(boolean activeInput) {
        Controller.activeInput = activeInput;
    }

    public static void setMessageReady(boolean messageReady) {
        Controller.messageReady = messageReady;
    }

    public synchronized void update(String input) {
        DataBase dataBase = View.getDataBase();
        if(activeInput && ( dataBase.getPlayerState() == PlayerState.ACTIVE || dataBase.getNickname() == null)) {
            activeInput = false;
            executor.submit(()-> {
                synchronized (dataBase){
                    MessageEvent message = dataBase.getControlState().computeInput(input);
                    if (messageReady){
                        messageReady = false;
                        notify(message);}
                    notifyAll();
                }
            });
        }
        else{
            System.out.print("\nPlease wait\n");
            activeInput = true;
        }
        notifyAll();
    }

    public static void updateStandardData(MessageEvent messageEvent){
        DataBase dataBase = View.getDataBase();
        if(messageEvent.getMatchState() != dataBase.getMatchState() && messageEvent.getMatchState() != null){
            dataBase.setMatchState(messageEvent.getMatchState());
        }
        if(messageEvent.getPlayerState() != dataBase.getPlayerState() && messageEvent.getPlayerState() != null){
            dataBase.setPlayerState(messageEvent.getPlayerState());
        }
        if(messageEvent.getTurnState() != dataBase.getTurnState() && messageEvent.getTurnState() != null){
            dataBase.setTurnState(messageEvent.getTurnState());
        }
        if(messageEvent.getMatchPlayers() != dataBase.getMatchPlayers() && messageEvent.getMatchPlayers() != null)
            dataBase.setMatchPlayers(messageEvent.getMatchPlayers());
        if((MatchState.SELECTING_SPECIAL_COMMAND != dataBase.getMatchState() || dataBase.getPlayer()==0) )
            dataBase.setPlayer(messageEvent.getCurrentPlayer());
    }

    public static void updateControllerState() {
        DataBase dataBase = View.getDataBase();
        if (dataBase.getNickname() == null && dataBase.getControlState().getClass() != NotInitialized.class){
            dataBase.setControlState(new NotInitialized());
        }
        switch (dataBase.getMatchState()){
            case GETTING_PLAYERS_NUM:
                if (dataBase.getControlState().getClass() != GettingPlayersNum.class)
                    dataBase.setControlState( new GettingPlayersNum());
                break;
            case WAITING_FOR_PLAYERS:
                if (dataBase.getControlState().getClass() != WaitingForPlayers.class)
                    dataBase.setControlState( new WaitingForPlayers());
                break;
            case SELECTING_GOD_CARDS:
                if (dataBase.getControlState().getClass() != SelectingGodCards.class)
                    dataBase.setControlState( new SelectingGodCards());
                break;
            case SELECTING_SPECIAL_COMMAND:
                if (dataBase.getControlState().getClass() != SelectingSpecialCommand.class)
                    dataBase.setControlState( new SelectingSpecialCommand());
                break;
            case PLACING_WORKERS:
                if (dataBase.getControlState().getClass() != PlacingWorkers.class)
                    dataBase.setControlState( new PlacingWorkers());
                break;
            case RUNNING:
                if (dataBase.getControlState().getClass() != Running.class)
                    dataBase.setControlState( new Running());
                break;
            case FINISHED:
                dataBase.setControlState(new NotInitialized());
                break;
            default:
                dataBase.setControlState( new WaitingList());
        }
    }


    /*private ControlState controlState;
    private static MessageEvent message;
    private boolean messageReady;
    Player player;

    public Controller() {
        controlState = new StartingStatus();
        message = new MessageEvent();
    }

    @Override
    public void update(String viewObject) {
        executor.submit(() -> execute(viewObject));
    }

    private synchronized void execute(String viewObject) {

        checkStatus();

        //System.out.println(viewObject + " -> " + controlState);

        messageReady = false;
        messageReady = controlState.processingMessage(viewObject);

        if (messageReady) {
            notify(message);
            reset();
        }
    }

    public void setState(ControlState ctrlState){
        this.controlState=ctrlState;
    }

    public static MessageEvent getMessage(){
        return message;
    }

    public ControlState getControlState() {return controlState;}

    public void reset (){
        message.setMatchID(null);
        message.setPlayerID(null);
        message.setNickname(null);
        message.setPlayersNum(null);
        message.setGodCards(null);
        message.setGodCard(null);
        message.setInitializedPositions(null);
        message.setStartPosition(null);
        message.setEndPosition(null);
        message.setEndTurn(null);
        message.setSpecialFunction(null);
    }

    private synchronized void checkStatus() {
        player = View.getPlayer();

        if (player.getPlayerState()==null && player.getMatchState()==null)
            return;
        else if (player.getPlayerState()==PlayerState.ACTIVE) {
            switch (player.getMatchState()) {
                case GETTING_PLAYERS_NUM:
                    controlState = new SelectionNumberStatus();
                    break;
                case SELECTING_GOD_CARDS:
                    if (controlState.getClass() == SelectingGodCardsStatus.class)
                        return;
                    controlState = new SelectingGodCardsStatus();
                    break;
                case SELECTING_SPECIAL_COMMAND:
                    controlState = new SelectingSpecialCommandStatus();
                    break;
                case PLACING_WORKERS:
                    if (controlState.getClass() == PlacingWorkersStatus.class)
                        return;
                    controlState = new PlacingWorkersStatus();
                    break;
                case RUNNING:
                    controlState = new RunningStatus();
                    break;
                default:
                    controlState = new WaitingStatus();
                    break; }
        } else controlState = new WaitingStatus();
    }

    public boolean isMessageReady(){
        return messageReady;
    }
    */

}
