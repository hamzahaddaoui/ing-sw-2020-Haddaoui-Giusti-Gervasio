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
        //String inputObject;
        //MessageEvent messageEvent;
        //try {
            System.out.println("Connected to " + client.getInetAddress());
            heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
            Thread reader = new Thread(this::inputHandler);
            reader.start();
            while(reader.isAlive());
            System.out.println("Closed");

            //inputHandler.submit(this::inputHandler);
            /*try {
                try{
                    while (true) {

                        System.out.println("Getting input");
                        inputObject = (String) input.readObject();
                        messageEvent = new Gson().fromJson(inputObject, MessageEvent.class);
                        System.out.println(messageEvent);
                        if (! messageEvent.getInfo().equals("Heartbeat Message")) {
                            messageEvent.setClientHandler(this);
                            //notify(messageEvent);
                        }
                    }
                }
                catch (ClassNotFoundException | ClassCastException exception) {
                    System.out.println("invalid stream from client");
                }
            }
            catch (SocketTimeoutException e) {
                System.out.println("Timeout. Connection " + client.getInetAddress() + " closed");
            }
        }
        catch (IOException ex) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
        System.out.println("exit");
        try {
            messageEvent = new MessageEvent();
            messageEvent.setExit(true);
            messageEvent.setInfo("CLOSING COMMUNICATION");
            messageEvent.setPlayerID(playerID);
            messageEvent.setMatchID(matchID);
            notify(messageEvent);
            client.close();
            heartbeatService.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void update(MessageEvent message){
        /*if (!message.getPlayerID().equals(this.playerID)){
            //non è per me!!!
            return;
        }*/

        String json = new GsonBuilder().create().toJson(message, MessageEvent.class);
        System.out.println("SENDING "+ json);
        outputTaskQueue.submit(() -> {
            try {
                output.reset();
                output.writeObject(json);
                System.out.println("inviato!");
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

        update(message);

        heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT/2, TimeUnit.MILLISECONDS);
    }

    void inputHandler(){
        String inputObject;
        MessageEvent messageEvent;
        try {
            while (true) {
                inputObject = (String) input.readObject();
                messageEvent = new Gson().fromJson(inputObject, MessageEvent.class);
                System.out.println(messageEvent);
                if (messageEvent.getInfo()==null || !messageEvent.getInfo().equals("Heartbeat Message")) {
                    messageEvent.setClientHandler(this);
                    notify(messageEvent);
                    System.out.println("CICLO CONCLUSO");
                    TimeUnit.SECONDS.sleep(6);
                }
            }
        } catch (ClassNotFoundException | ClassCastException | IOException | InterruptedException exception) {
            System.out.println("invalid stream from client");
        }
    }
}
