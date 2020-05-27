package it.polimi.ingsw.client.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.GUI.controller.StartState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public final static int SOCKET_TIMEOUT = 5000;

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
        //server.setSoTimeout(SOCKET_TIMEOUT);
        output = new ObjectOutputStream(server.getOutputStream());
        input = new ObjectInputStream(server.getInputStream());
        System.out.println("Connected to " + server.getInetAddress());
    }

    @Override
    public void run() {
        //heartbeatService = Executors.newSingleThreadScheduledExecutor();
        //inputHandler = Executors.newSingleThreadScheduledExecutor();
        heartbeatService.schedule(this::heartbeatRunnable, SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        inputHandler.submit(this::inputHandler);
        active = true;
    }

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

    public void heartbeatRunnable() {
        MessageEvent msgEvent = new MessageEvent();
        if (active) {
            msgEvent.setInfo("Heartbeat Message");
            update(msgEvent);
            heartbeatService.schedule(this::heartbeatRunnable, SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        }
    }

   private void inputHandler() {
       String inputObject;
       MessageEvent messageEvent;
       try {
           while (active) {
               inputObject = (String) input.readObject();
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
           active = false;
           connectionError();
           shutdownAll();
           System.out.println("Connection error - Check your internet connection.");
       }
   }

   public void shutdownAll() {
        heartbeatService.shutdown();
        inputHandler.shutdown();
   }

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
