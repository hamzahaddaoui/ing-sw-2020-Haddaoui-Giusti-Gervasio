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
import java.net.SocketTimeoutException;
import java.util.concurrent.*;



public class ClientHandler extends Observable<MessageEvent> implements Observer<MessageEvent>, Runnable {
    private final ExecutorService outputTaskQueue = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService heartbeatService = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService inputHandler = Executors.newSingleThreadExecutor();
    private final Socket client;
    private boolean active;
    private int playerID;
    private int matchID;

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
        active = true;
        this.client = client;
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }


    @Override
    public void run() {
        System.out.println("Connected to " + client.getInetAddress());
        heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        inputHandler.submit(this::inputHandler);
        while(active){
        }
        heartbeatService.shutdown();
        inputHandler.shutdown();
        System.out.println("Connection to " + client.getInetAddress()+" closed");
    }

    @Override
    public void update(MessageEvent message){
        if (!message.getPlayerID().equals(this.playerID) || !active){
            return;
        }
        String json = new GsonBuilder()
                .serializeNulls()
                .enableComplexMapKeySerialization()
                .create()
                .toJson(message, MessageEvent.class);

        if (message.getInfo() == null || !message.getInfo().equals("Heartbeat Message"))
            System.out.println("SENDING "+ json);

        outputTaskQueue.submit(() -> {
            try {
                output.reset();
                output.writeObject(json);
                output.flush();
            }
            catch(IOException e){
                System.out.println("errore nell'invio");

            }
        });
    }

    public void heartbeatAgent() {
        MessageEvent message =  new MessageEvent();
        message.setInfo("Heartbeat Message");

        update(message);
        if (active)
            heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT/2, TimeUnit.MILLISECONDS);
        else
            heartbeatService.shutdownNow();
    }

    void inputHandler(){
        String inputObject;
        MessageEvent messageEvent;
        try {
            while (true) {
                inputObject = (String) input.readObject();
                messageEvent = new Gson().newBuilder().create().fromJson(inputObject, MessageEvent.class);

                if ((messageEvent.getInfo() == null) || !messageEvent.getInfo().equals("Heartbeat Message")) {
                    System.out.println(messageEvent);
                    messageEvent.setClientHandler(this);
                    notify(messageEvent);
                }
            }
        } catch (ClassNotFoundException | ClassCastException exception) {
            System.out.println("invalid stream from client");
        } catch(SocketTimeoutException exception){
            System.out.println("Connection timeout");
        } catch(IOException exception){
            System.out.println("Connection error. Unexpected client connection close");
        }

        finally {
            try {
                messageEvent = new MessageEvent();
                messageEvent.setExit(true);
                messageEvent.setClientHandler(this);
                messageEvent.setExit(true);
                if(matchID !=0)
                    notify(messageEvent);
                active = false;
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
