package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;



public class ClientHandler extends Observable<MessageEvent> implements Observer<MessageEvent>, Runnable {
    private final ExecutorService outputTaskQueue = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService heartbeatService = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService inputHandler = Executors.newSingleThreadExecutor();
    private final Socket client;

    private Integer playerID;
    private Integer matchID;

    private final ObjectOutputStream output;
    private final ObjectInputStream input;


  /*
  TODO
  TODO (done) Inserire timeout connessione (heartbeat messages)
  TODO Inserire AFK timeout. se non fai una mossa entro 1 minuto, hai perso
  TODO (done) chiusura connessione ondemand
  */

    public Integer getMatchID(){
        return matchID;
    }

    public void setMatchID(Integer matchID){
        this.matchID = matchID;
    }

    public Integer getPlayerID(){
        return playerID;
    }

    public void setPlayerID(Integer playerID){
        this.playerID = playerID;
    }

    ClientHandler(Socket client) throws IOException {
        this.client = client;
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }


    @Override
    public void run() {
        System.out.println("Connected to " + client.getInetAddress());
        Thread reader = new Thread(this::inputHandler);
        heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        reader.start();
    }

    @Override
    public void update(MessageEvent message){
        if (message.getPlayerID() != null && !message.getPlayerID().equals(this.playerID)){
            //non è per me!!!
            return;
        }
        String json = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create().toJson(message, MessageEvent.class);
        System.out.println("SENDING "+ json);
        outputTaskQueue.submit(() -> {
            try {
                output.reset();
                output.writeObject(json);
                output.flush();
            }
            catch(IOException e){
                System.out.println("errore nell'invio");
                System.err.println(e.getMessage());
            }
        });
    }

    public void heartbeatAgent() {
        MessageEvent message =  new MessageEvent();
        message.setInfo("Heartbeat Message");

        //update(message);

        heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT/2, TimeUnit.MILLISECONDS);
    }

    void inputHandler(){
        String inputObject;
        MessageEvent messageEvent;
        try {
            while (true) {
                inputObject = (String) input.readObject();
                messageEvent = new Gson().newBuilder().create().fromJson(inputObject, MessageEvent.class);

                if (messageEvent.getInfo()==null || !messageEvent.getInfo().equals("Heartbeat Message")) {
                    System.out.println(messageEvent);
                    messageEvent.setClientHandler(this);
                    messageEvent.setPlayerID(playerID);
                    messageEvent.setMatchID(matchID);
                    notify(messageEvent);
                }
            }
        } catch (ClassNotFoundException | ClassCastException | IOException exception) {
            System.out.println("invalid stream from client");
        }

        //finally con chiusura
    }
}
