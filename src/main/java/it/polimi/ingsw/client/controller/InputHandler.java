package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;

import java.util.Scanner;

public class InputHandler extends Observable<String> {

    public void inputListener() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            notify(input);
        }
    }

}

/*Scanner scanner = new Scanner(System.in);
        while (true) {
            if (activeInput) {
                activeInput = false;
                String input = scanner.nextLine();
                synchronized (View.class){
                    executor.submit(()-> {
                       MessageEvent message = View.getPlayer().getControlState().computeInput(input);
                       if (messageReady) {
                           notify(message);
                           messageReady = false;
                       }
                });}
                notifyAll();
            }
            else{
                scanner.nextLine();
            }
        }*/