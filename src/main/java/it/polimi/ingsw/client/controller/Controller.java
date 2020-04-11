package it.polimi.ingsw.client.controller;


import com.google.gson.Gson;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

public class Controller extends Observable implements Observer {
    @Override
    public void update(Object message){
        //messaggio ricevuto dalla view!
        //lo elaboro e se valido, lo invio al server controller

        String output = new Gson().toJson(message);
        notify(output);

    }
}
