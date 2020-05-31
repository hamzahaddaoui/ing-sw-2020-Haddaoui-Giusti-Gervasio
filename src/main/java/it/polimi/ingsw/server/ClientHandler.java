package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.controller.state.State;
import it.polimi.ingsw.utilities.*;

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

    private MessageEvent lastMessage;

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


    /**
     * Starting method for the Client Handler thread.
     * <p>
     * The method launches a thread dedicate to the heartbeat messages and a thread dedicate to analyze the messages
     * received by the client. When this socket is not active anymore, it shuts down both threads.
     *
     */
    @Override
    public void run() {
        System.out.println("Connected to " + client.getInetAddress());
        lastMessage = null;
        heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        inputHandler.submit(this::inputHandler);
    }

    /**
     * Method called after a notification from the Server Controller.
     * <p>
     * If the player specified in the message is different from the client's player, it does nothing.
     * Otherwise, the method creates a string from the message received and creates a thread that
     * handles the writing of the message to the client.
     *
     *
     * @param message the message passed by the Controller
     */
    @Override
    public void update(MessageEvent message){

        if (! active || (message.getPlayerID() != this.playerID))
            return;

        String json = new GsonBuilder()
                .serializeNulls()
                .enableComplexMapKeySerialization()
                .create()
                .toJson(message, MessageEvent.class);

        if (message.getInfo() == null || !message.getInfo().equals("Heartbeat Message")) {
            System.out.println("SENT:  " + message);
        }

        lastMessage = message;

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

    /**
     * Method that handle the sending of heartbeat messages to the client.
     * <p>
     * After every half of the timeout it sends a message of type Message Event
     * useful to let the client knows that this is still connected.
     * When the socket is not active anymore, the method shuts down the thread dedicate to this function.
     */
    public void heartbeatAgent() {
        MessageEvent messageEvent;
        if (active){
            if (lastMessage != null) {
                messageEvent = lastMessage;
                update(messageEvent);
                lastMessage = null;
            }
            else {
                messageEvent = new MessageEvent();
                messageEvent.setPlayerID(this.playerID);
                messageEvent.setInfo("Heartbeat Message");
                //messageEvent = State.basicPlayerConfig(State.basicMatchConfig(new MessageEvent(), this.matchID), this.playerID);
                update(messageEvent);
            }

            if (messageEvent.isFinished())
                return;


            heartbeatService.schedule(this::heartbeatAgent, Server.SOCKET_TIMEOUT/2, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Method that reads the messages sent by the client.
     * <p>
     * The method builds a message from the string received.
     * Then, if the message is an heartbeat one the method, it does nothing,
     * otherwise it notifies the Server Controller with the message.
     * If the client is no longer connected the method notifies the Server Controller
     * with a last message with a reference to the client and the boolean exit set, then it closes.
     */
    void inputHandler(){
        String inputObject;
        MessageEvent messageEvent;
        try {
            while (true) {
                inputObject = (String) input.readObject();
                messageEvent = new Gson().newBuilder().create().fromJson(inputObject, MessageEvent.class);

                if ((messageEvent.getInfo() == null) || !messageEvent.getInfo().equals("Heartbeat Message")) {
                    System.out.println("RECEIVED:  " + messageEvent);
                    messageEvent.setClientHandler(this);
                    notify(messageEvent);
                }
            }
        } catch (ClassNotFoundException | ClassCastException exception) {
            System.out.println("invalid stream from client");
        } catch(SocketTimeoutException exception){
            System.out.println("Connection timeout");
        } catch(IOException exception){
            System.out.println("Connection error - Unexpected client disconnection.");
        }

        finally {
            try {
                System.out.println("Unsubscribing client...");
                messageEvent = new MessageEvent();
                messageEvent.setExit(true);
                messageEvent.setClientHandler(this);
                notify(messageEvent);
                active = false;
                client.close();
                heartbeatService.shutdownNow();
                System.out.println("Done!");
                System.out.println("Connection to " + client.getInetAddress()+" closed");
                inputHandler.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
