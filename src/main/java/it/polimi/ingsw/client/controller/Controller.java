package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.ControlState;
import it.polimi.ingsw.client.controller.state.StartingStatus;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Observable<MessageEvent> implements Observer<Object> {

    static ExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private ControlState controlState;
    private MatchState matchState;
    private PlayerState playerState;
    private static MessageEvent message;
    private boolean messageReady = false;

    public Controller() {
        controlState = new StartingStatus();
        playerState = null;
        matchState = null;
        message = new MessageEvent();

    }

    @Override
    public void update(Object viewObject){
        executor.submit(() -> execute(viewObject));
    }

    private synchronized void execute(Object viewObject) {
        messageReady = false;
                PlayerState newPlayerState = View.getPlayer().getPlayerState();
                MatchState newMatchState = View.getPlayer().getMatchState();
                System.out.print("\nview MATCHSTATE ->"+newMatchState);
                System.out.print("  /  view PLAYERSTATE ->"+newPlayerState);
                System.out.println(" /   ctrl ControlSTATE ->"+controlState+"  \n ");
                if(newMatchState == null && newPlayerState == null){
                    reset();
                }
                else if(newPlayerState != playerState || newMatchState != matchState){
                    playerState = newPlayerState;
                    matchState = newMatchState;
                    nextState();
                }

                System.out.print("\nctrl MATCHSTATE ->"+matchState);
                System.out.print("/   ctrl PLAYERSTATE ->"+playerState);
                System.out.println("  / ctrl ControlSTATE ->"+controlState+"  \n ");
                messageReady = controlState.processingMessage(viewObject);

                if (messageReady) {
                    if (playerState != null)
                        message.setPlayerID(View.getPlayer().getPlayerID());
                    if (matchState != null)
                        message.setMatchID(View.getPlayer().getMatchID());
                    notify(message);
                    reset();
                }
        System.out.print("\nctrl MATCHSTATE ->"+matchState);
        System.out.print("/   ctrl PLAYERSTATE ->"+playerState);
        System.out.println("  / ctrl ControlSTATE ->"+controlState+"  \n ");

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
        //message.setExit(false);
        //message.setMatchState(null);
        //message.setPlayerState(null);
        //message.setTurnState(null);
        //message.setError(false);
        //message.setMatchCards(null);
        //message.setAvailablePlacingCells(null);
        //message.setBillboardStatus(null);
        //message.setWorkersAvailableCells(null);
        //message.setTerminateTurnAvailable(null);
        //message.setSpecialFunctionAvailable(null);
        //message.setMatchPlayers(null);
        //message.setWinner(null);
        //message.setFinished(false);
    }

    /*
    ------------------- UTILE PER I TEST ------------------
     */

    public boolean isMessageReady(){
        return messageReady;
    }
}
