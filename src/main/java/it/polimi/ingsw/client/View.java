package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.*;


import java.util.Scanner;

public class View extends Observable implements Runnable, Observer<MVEvent> {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void update(MVEvent message){
        //se ricevo un messaggio dal model
        //aggiorna la scacchiera a video
    }

    @Override
    public void update(int matchID, int playerID, MVEvent message){
        return;
    }

    @Override
    public void run(){
        VCEvent vcEvent = new VCEvent("nick", new Position(2,2));
        while(true) {

            int x, y;
            System.out.println("Insert position X");
            x = scanner.nextInt();
            System.out.println("Insert position Y");
            y = scanner.nextInt();

            notify(vcEvent);
        }
    }
}
