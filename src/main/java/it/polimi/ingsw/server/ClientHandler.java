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


public class ClientHandler extends Observable implements Observer<MessageEvent>, Runnable {
  ExecutorService executor = Executors.newSingleThreadExecutor();
    private Integer matchID;
    private Integer playerID;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket client;
    private boolean active;

  /*
  TO - DO
  Inserire timeout connessione + heartbeat messages
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

    ClientHandler(Socket client, int playerID) throws IOException {
        this.client = client;
        this.playerID = playerID;
        this.active = true;
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }


    @Override
    public void run() {
        String inputObject;
        try {
            System.out.println("Connected to " + client.getInetAddress());
            try {
                while (active) {
                    inputObject = (String) input.readObject();
                    MessageEvent messageEvent = new Gson().fromJson(inputObject, MessageEvent.class);
                    messageEvent.setMatchID(this.matchID);
                    messageEvent.setPlayerID(this.playerID);
                    //l'oggetto può essere indirizzato al serverController
                    notify(inputObject);
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
        if (message.getPlayerID() != 0 && message.getPlayerID()!=this.playerID){
            //non è per me!!!
            return;
        }
        if (message.getMatchID() != this.matchID){
            return;
        }

        String json = new GsonBuilder().serializeNulls().create().toJson(message);

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
