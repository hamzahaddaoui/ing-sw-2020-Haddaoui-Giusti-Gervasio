package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.Position;

public class Controller extends Observable implements Observer {


    @Override
    public void update(Object message){
        //messaggio ricevuto dalla view!
        //lo elaboro e se valido, lo invio al server controller

        //String output = new Gson().toJson(new Message("playerAction", (Position) message));
        //notify(output);

    }
}
