package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHandler extends Observable<MessageEvent> implements Observer<MessageEvent>, Runnable {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Socket client;

    private Integer matchID;
    private Integer playerID;

    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    private boolean active;

  /*
  TO - DO
  Inserire timeout connessione (heartbeat messages)
  Inserire AFK timeout. se non fai una mossa entro 1 minuto, hai perso
  Eventualmente si può far scegliere all'utente

  Stati pubblici
  mandare god card all'inizio
  */

    public Integer getMatchID(){
        return matchID;
    }

    public Integer getPlayerID(){
        return playerID;
    }

    public void setMatchID(Integer matchID){
        this.matchID = matchID;
    }

    public void setPlayerID(Integer playerID){
        this.playerID = playerID;
    }

    ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.active = true;
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }


    @Override
    public void run() {
        try {
            System.out.println("Connected to " + client.getInetAddress());
            try {
                while (active) {
                    String inputObject = (String) input.readObject();
                    MessageEvent messageEvent = new Gson().fromJson(inputObject, MessageEvent.class);
                    if(!(this.matchID.equals(messageEvent.getMatchID())
                         && this.playerID.equals(messageEvent.getPlayerID()))){
                        //ignoro il messaggio perchè non è corretto
                        continue;
                    }
                    messageEvent.setClientHandler(this);
                    notify(messageEvent);
                }
            }
            catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("invalid stream from client");
            }

            client.close();
        }
        catch (IOException ex) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }


    @Override
    public void update(MessageEvent message){
        if (((message.getPlayerID() != null) && !message.getPlayerID().equals(this.playerID))
            || ((message.getMatchID() != null) && !message.getMatchID().equals(this.matchID))) {
            //non è per me!!!
            return;
        }

        String json = new GsonBuilder().create().toJson(message);

        executor.submit(() -> {
            try {
                output.reset();
                output.writeObject(json);
                output.flush();
            }
            catch(IOException e){
                System.err.println(e.getMessage());
            }
        });
    }
}
