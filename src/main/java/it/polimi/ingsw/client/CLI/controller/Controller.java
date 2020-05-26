package it.polimi.ingsw.client.CLI.controller;

import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author giusti-leo , Vasio1298
 *
 * SelectingSpecialCommand is a state of the Controller and it handles the selection of Special GodCard for the Match
 *
 */

public class Controller extends Observable<MessageEvent> {

    static ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Method handles the input from keyboard and launches the execution of ControllerState. Then, if the message is Ready
     * it notifies the MessageEvent to the Network Handler
     */
    public synchronized void inputListener () {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            synchronized (DataBase.class){
            if (DataBase.isActiveInput() && (DataBase.getPlayerState() == PlayerState.ACTIVE || DataBase.getPlayerState() == null || DataBase.isViewer())) {
                DataBase.setActiveInput(false);
                executor.submit(() -> {
                        MessageEvent message = DataBase.getControlState().computeInput(input);
                        if (DataBase.isMessageReady()) {
                            DataBase.setMessageReady(false);
                            notify(message);
                        }
                });
            } else {
                System.out.print("\nPlease wait\n");
                DataBase.setActiveInput(true);
            }
                notifyAll();
            }
        }
    }

}

