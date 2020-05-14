package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Observable<MessageEvent> {

    static ExecutorService executor = Executors.newSingleThreadExecutor();

    public synchronized void inputListener () {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (DataBase.isActiveInput() && (DataBase.getPlayerState() == PlayerState.ACTIVE || DataBase.getPlayerState() == null)) {
                DataBase.setActiveInput(false);
                executor.submit(() -> {
                    synchronized (DataBase.class) {
                        MessageEvent message = DataBase.getControlState().computeInput(input);
                        if (DataBase.isMessageReady()) {
                            DataBase.setMessageReady(false);
                            notify(message);
                        }
                        notifyAll();
                    }
                });
            } else {
                System.out.print("\nPlease wait\n");
                DataBase.setActiveInput(true);
            }
        }
    }

    /*public synchronized void update(String input) {
        if (activeInput && (DataBase.getPlayerState() == PlayerState.ACTIVE || DataBase.getPlayerState() == null)) {
            activeInput = false;
            executor.submit(() -> {
                synchronized (DataBase.class) {
                    MessageEvent message = DataBase.getControlState().computeInput(input);
                    if (messageReady) {
                        messageReady = false;
                        notify(message);
                    }
                    notifyAll();
                }
            });
        } else {
            System.out.print("\nPlease wait\n");
            activeInput = true;
        }
    }*/
}

