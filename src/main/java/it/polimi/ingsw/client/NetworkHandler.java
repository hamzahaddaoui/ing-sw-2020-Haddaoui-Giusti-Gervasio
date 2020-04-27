package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class NetworkHandler extends Observable<MessageEvent> implements Runnable, Observer<MessageEvent> {
    public final static int SOCKET_PORT = 12345;
    public final static int SOCKET_TIMEOUT = 5000;                                                        //Vasio

    private final Socket server;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService heartbeatService = Executors.newSingleThreadScheduledExecutor();      //Vasio

    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private boolean active;

    //TODO CAPIRE QUANDO CHIAMARE IL CLOSE

    public NetworkHandler(String ip) throws IOException {
        server = new Socket(ip, SOCKET_PORT);
        server.setSoTimeout(SOCKET_TIMEOUT);                                                              //Vasio
        active = true;
        output = new ObjectOutputStream(server.getOutputStream());
        input = new ObjectInputStream(server.getInputStream());
    }

    @Override
    public void run() {
        try {
            String inputObject;
            heartbeatService.schedule(this::heartbeatRunnable, SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS); //Vasio

            try {
                while (active) {
                    inputObject = (String) input.readObject();
                    MessageEvent messageEvent = new Gson().fromJson(inputObject, MessageEvent.class);       //Vasio
                    if (!messageEvent.getInfo().equals("Heartbeat Message"))                                 //Vasio
                        notify(messageEvent);
                }
            } catch (SocketTimeoutException e) {                                                          //Vasio
                System.out.println("Socket timed out!");                                                  //Vasio
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (EOFException e) {
             //
            }
            finally {
                try {
                    active = false;
                    server.close();
                    heartbeatService.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("server connection dropped");
            heartbeatService.shutdown();
        }
    }

    @Override
    public void update(MessageEvent message){
        //messaggio da clientController a ServerController

        String json = new Gson().toJson(message);                                                         //Vasio

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

    public void stop() throws IOException{
        executor.shutdown();
        active = false;
        server.close();
    }

    //Vasio
    public void heartbeatRunnable() {
        MessageEvent msgEvent = new MessageEvent();
        msgEvent.setInfo("Heartbeat Message");

        String json = new Gson().toJson(msgEvent);

        try {
            output.reset();
            output.writeObject(json);
            output.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        heartbeatService.schedule(this::heartbeatRunnable,SOCKET_TIMEOUT/2,TimeUnit.MILLISECONDS);
    }
}
