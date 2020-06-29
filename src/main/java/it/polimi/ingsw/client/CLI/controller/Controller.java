package it.polimi.ingsw.client.CLI.controller;

import it.polimi.ingsw.client.CLI.Client;
import it.polimi.ingsw.client.CLI.controller.state.ControlState;
import it.polimi.ingsw.client.CLI.controller.state.NotInitialized;
import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.client.GUI.Database;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author giusti-leo , Vasio1298
 *
 * SelectingSpecialCommand is a state of the Controller and it handles the selection of Special GodCard for the Match
 *
 */

public class Controller extends Observable<MessageEvent> {

    static ExecutorService executor = Executors.newSingleThreadExecutor();
    //static ExecutorService reconnector = Executors.newSingleThreadExecutor();

    /**
     * Method handles the input from keyboard and launches the execution of ControllerState. Then, if the message is Ready
     * it notifies the MessageEvent to the Network Handler
     */
    public synchronized void inputListener () {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            //System.out.println("Init input iter");
            synchronized (View.class){
                synchronized (DataBase.class) {
                    //System.out.println("punto1");
                    if ((DataBase.isActiveInput() && DataBase.getControlState().getClass() == NotInitialized.class) || DataBase.isReconnection()) {
                        if(DataBase.isReconnection() && !input.toUpperCase().equals("REC") && !input.toUpperCase().equals("Q"))
                            System.out.println("Press Q to disconnect or Rec to reconnect");
                        if (DataBase.isReconnection() && input.toUpperCase().equals("REC")) {
                            DataBase.setReconnection(false);
                            DataBase.resetDataBase();
                            Client.reconnection();
                            return;
                        } else if (input.toUpperCase().equals("Q")) {
                            System.out.println("CLIENT INPUT CLOSED");
                            DataBase.setActiveInput(false);
                            Client.close();
                            return;
                        }
                    }

                    if (DataBase.isActiveInput() && (DataBase.getPlayerState() == PlayerState.ACTIVE || DataBase.getPlayerState() == null || DataBase.isViewer())) {
                        DataBase.setActiveInput(false);
                        //System.out.println("INPUT Compute");
                        executor.submit(() -> {
                            //System.out.println("Thread init");
                            synchronized (View.class){
                                synchronized (DataBase.class) {
                                    //System.out.println("Thread Start");
                                    MessageEvent message = DataBase.getControlState().computeInput(input);
                                    if (DataBase.isMessageReady()) {
                                        DataBase.setMessageReady(false);
                                        notify(message);
                                    }
                                    //System.out.println("Thread END");
                                }
                            }
                        });
                        //System.out.println("INPUT ->    end");
                    } else {
                        if (!DataBase.isActiveInput())
                            System.out.print("\nPlease wait\n");
                    }
                    //System.out.println("Input iter end");
                    //notifyAll();
                }
            }
            //System.out.println("End input iter");
        }

    }

}

