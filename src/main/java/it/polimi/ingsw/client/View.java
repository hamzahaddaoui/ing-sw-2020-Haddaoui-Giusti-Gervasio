package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.Message;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.Position;

import java.util.Scanner;

public class View extends Observable implements Runnable, Observer<String> {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void update(String message){
        //se ricevo un messaggio dal model/servercontroller
        message = message;

        Message input = new Gson().fromJson(message,Message.class);
        System.out.println("UserID: " + input.getUserID());
        System.out.println("Position: " + input.getPosition().getX() + " - " + input.getPosition().getY());
    }

    @Override
    public void run(){
        while(true) {
            int x, y;
            System.out.println("Insert position X");
            x = scanner.nextInt();
            System.out.println("Insert position Y");
            y = scanner.nextInt();

            notify(new Position(x, y));
        }
    }
}
