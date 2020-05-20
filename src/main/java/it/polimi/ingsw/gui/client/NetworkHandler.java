package it.polimi.ingsw.gui.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkHandler extends Observable<MessageEvent> implements Runnable, Observer<MessageEvent> {
    public final static int SOCKET_PORT = 12345;
    public final static int SOCKET_TIMEOUT = 5000;

    private final Socket server;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService heartbeatService = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService messageReader = Executors.newSingleThreadExecutor();

    private boolean active;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;

    public NetworkHandler(String ip) throws IOException {
        active = true;
        server = new Socket(ip, SOCKET_PORT);
        server.setSoTimeout(SOCKET_TIMEOUT);
        output = new ObjectOutputStream(server.getOutputStream());
        input = new ObjectInputStream(server.getInputStream());
    }

    @Override
    public void run() {
        System.out.println("Connected to " + server.getInetAddress());
        heartbeatService.schedule(this::heartbeatRunnable, SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        messageReader.submit(this::inputHandler);
    }

    public void stop() throws IOException{
        active = false;
        server.close();
    }

    @Override
    public void update(MessageEvent message){

        String json = new GsonBuilder().enableComplexMapKeySerialization().create().toJson(message);

        executor.submit(() -> {
            try {
                output.reset();
                output.writeObject(json);
                output.flush();
            } catch (SocketException e) {
                try {
                    System.out.println("server connection closed");
                    stop();
                }
                catch (IOException ex) {
                    ex.getMessage();
                }
            }
            catch(IOException e){
                System.out.println("si è verificato un problema nell'invio!");
                System.err.println(e.getMessage());
            }
        });
    }

    public void heartbeatRunnable() {
        MessageEvent msgEvent = new MessageEvent();
        if (active) {
            msgEvent.setInfo("Heartbeat Message");
            update(msgEvent);
            heartbeatService.schedule(this::heartbeatRunnable, SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        }
        else {
            MessageEvent message = new MessageEvent();
            message.setExit(true);
            update(message);
            heartbeatService.shutdownNow();
        }
    }

   private void inputHandler() {
       String inputObject;
       MessageEvent messageEvent;
       try {
           while (active) {
               inputObject = (String) input.readObject();
               messageEvent = new Gson().newBuilder().create().fromJson(inputObject, MessageEvent.class);

               if (messageEvent.getInfo()==null || !messageEvent.getInfo().equals("Heartbeat Message")) {
                   notify(messageEvent);
               }
           }
       } catch (SocketTimeoutException e) {
           System.out.println("socket timed out");
       } catch (ClassNotFoundException | ClassCastException | IOException exception) {
           System.out.println("invalid stream from server");
       }finally {
           try {
               stop();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }
}
