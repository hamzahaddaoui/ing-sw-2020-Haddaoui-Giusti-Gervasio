package it.polimi.ingsw.client;


import com.google.gson.Gson;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.VCEvent;

public class Controller extends Observable implements Observer<VCEvent> {
    @Override
    public void update(VCEvent message){
        //messaggio ricevuto dalla view!
        //lo elaboro e se valido, lo invio al server controller

        String output = new Gson().toJson(message);
        notify(output);

    }

    @Override
    public void update(int matchID, int playerID, VCEvent message){

    }
}
