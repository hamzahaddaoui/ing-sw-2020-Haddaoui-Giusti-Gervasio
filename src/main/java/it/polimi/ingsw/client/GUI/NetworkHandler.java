package it.polimi.ingsw.client.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.GUI.controller.StartState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkHandler extends Observable<MessageEvent> implements Runnable, Observer<MessageEvent> {
    public final static int SOCKET_PORT = 12345;
    public final static int SOCKET_TIMEOUT = 10000;

    private ScheduledExecutorService heartbeatService = Executors.newSingleThreadScheduledExecutor();
    private ExecutorService inputHandler  = Executors.newSingleThreadScheduledExecutor();;

    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean active;

    private String lastJson;

    public NetworkHandler(String ip) throws IOException{
        active = true;
        server = new Socket();
        server.connect(new InetSocketAddress(ip, SOCKET_PORT), 5000);
        //server = new Socket(ip, SOCKET_PORT);
        server.setSoTimeout(SOCKET_TIMEOUT);
        output = new ObjectOutputStream(server.getOutputStream());
        input = new ObjectInputStream(server.getInputStream());
        System.out.println("Connected to " + server.getInetAddress());
    }

    /**
     * Launching method for the network handler thread.
     * A heartbeat service is instanciated, as well as the input handler.
     */
    @Override
    public void run() {
        //heartbeatService = Executors.newSingleThreadScheduledExecutor();
        //inputHandler = Executors.newSingleThreadScheduledExecutor();
        heartbeatService.schedule(this::heartbeatRunnable, SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        inputHandler.submit(this::inputHandler);
        active = true;
    }

    /**
     * Manager of the output connections.
     * Sends a message to the server.
     * @param message the message to be sent to the server
     */
    @Override
    public void update(MessageEvent message){
        String json = new GsonBuilder().enableComplexMapKeySerialization().create().toJson(message);

        if (!active)
            return;

        new Thread (() -> {
            try {
                output.reset();
                output.writeObject(json);
                output.flush();
            } catch (SocketException e) {
                active = false;
                //connectionError();
                //shutdownAll();
                System.out.println("server connection closed");

            }
            catch(IOException e){
                System.out.println("si è verificato un problema nell'invio!");
                System.err.println(e.getMessage());
            }
        }).start();
    }

    /**
     * Manager of the connection. At a constant rate, a heartbeat message is sent to the server.
     * The rate at which the messages are sent is SOCKET_TIMEOUT/2
     */
    public void heartbeatRunnable() {
        MessageEvent msgEvent = new MessageEvent();
        if (active) {
            msgEvent.setInfo("Heartbeat Message");
            update(msgEvent);
            heartbeatService.schedule(this::heartbeatRunnable, SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Manager of the input connection, while the connection is active, keeps checking the input stream from the socket.
     * Whenever a new message is received, the controller is notified.
     */
    private void inputHandler() {
       String inputObject;
       MessageEvent messageEvent;
       try {
           while (active) {
               inputObject = (String) input.readObject();
               System.out.println(inputObject);
               if (! inputObject.equals(lastJson)){
                   lastJson = inputObject;
                   messageEvent = new Gson().newBuilder().create().fromJson(inputObject, MessageEvent.class);

                   if (messageEvent.getInfo()==null || !messageEvent.getInfo().equals("Heartbeat Message"))
                       notify(messageEvent);
               }
           }
       }
       catch (ClassNotFoundException | ClassCastException exception) {
           System.out.println("invalid stream from client");
       }
       catch(IOException exception) {
           if (active) {
               connectionError();
               shutdownAll();
           }
           active = false;
           System.out.println("Connection error - Check your internet connection.");
       }
   }

    /**
     * Shutdown of all the connection services.
     * Closes the input and output stream.
     */
   public void shutdownAll(){
        active = false;
        heartbeatService.shutdownNow();
        inputHandler.shutdownNow();
        if (server.isConnected()) {
            try {
                server.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
   }

    /**
     * Management of the connection errors from the server.
     * e.g. Server not sending heartbeat messages anymore, server not reachable.
     */
   public void connectionError(){
       Database.wipeData();
       Database.setCurrentState(new StartState());
       Database.getCurrentState().showPane();

       Platform.runLater(() -> {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Connection timeout");
           alert.setHeaderText("Check your internet connection");
           alert.setContentText("The server is no longer reachable");
           alert.showAndWait();
       });
   }

    /**
     * Manager of the network handler observer. Only one class at time can get updates from the net handler.
     * @param observer the new observer
     */
    @Override
    public void addObserver(Observer<MessageEvent> observer){
        List<Observer<MessageEvent>> observers = new ArrayList<>();

        for (Observer obs : getObservers())
            observers.add(obs);

        for (Observer obs : observers)
            super.removeObserver(obs);

        super.addObserver(observer);
    }
}
