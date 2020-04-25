package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class NetworkHandler extends Observable<MessageEvent> implements Runnable, Observer<MessageEvent> {
    public final static int SOCKET_PORT = 12345;
    public final static int SOCKET_TIMEOUT = 5000;                                                   //Vasio

    private Socket server;
    static ExecutorService executor = Executors.newSingleThreadExecutor();
    static ScheduledExecutorService heartbeatService = Executors.newSingleThreadScheduledExecutor();      //Vasio

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
        String inputObject;
        heartbeatService.schedule(this::heartbeatRunnable,SOCKET_TIMEOUT/2,TimeUnit.MILLISECONDS); //Vasio

        while (active) {
            try {
                inputObject = (String) input.readObject();
                MessageEvent messageEvent = new Gson().fromJson(inputObject,MessageEvent.class);          //Vasio
                if (messageEvent.getInfo().equals("Heartbeat Message"))                                   //Vasio
                    continue;                                                                             //Vasio
                notify(messageEvent);
            } catch (SocketTimeoutException e) {                                                          //Vasio
                System.out.println("Socket timed out!");                                                  //Vasio
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
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
