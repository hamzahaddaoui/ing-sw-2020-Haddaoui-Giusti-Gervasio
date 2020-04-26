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
        try {
            System.out.println("Connected to " + client.getInetAddress());
            heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);

            try {
                    while (true) {
                        String inputObject = (String) input.readObject();
                        MessageEvent messageEvent = new Gson().fromJson(inputObject, MessageEvent.class);
                        //########DEBUG#######################
                        System.out.println(messageEvent);
                        //####################################
                        if (messageEvent.getInfo().equals("Heartbeat Message")) {
                        }
                        else if (this.playerID.equals(messageEvent.getPlayerID())) {
                            messageEvent.setClientHandler(this);
                            notify(messageEvent);
                        }
                    }
                    //client.close();
                }
            catch (ClassNotFoundException | ClassCastException exception) {
                    System.out.println("invalid stream from client");
                }
            catch (SocketTimeoutException e) {
                System.out.println("Timeout. Connection " + client.getInetAddress() + " closed");

            }
            finally {
                try {
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setExit(true);
                    messageEvent.setPlayerID(playerID);
                    messageEvent.setMatchID(matchID);
                    notify(messageEvent);
                    client.close();
                    heartbeatService.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException ex) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
            heartbeatService.shutdown();
        }
    }


    @Override
    public void update(MessageEvent message){
        if (!message.getPlayerID().equals(this.playerID)){
            //non è per me!!!
            return;
        }

        String json = new GsonBuilder().create().toJson(message);

        outputTaskQueue.submit(() -> {
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

    public void heartbeatAgent() {
        MessageEvent message =  new MessageEvent();
        message.setInfo("Heartbeat Message");

        String json = new GsonBuilder().create().toJson(message);

        try {
            output.reset();
            output.writeObject(json);
            output.flush();
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }

        heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT/2, TimeUnit.MILLISECONDS);
    }
}
