package it.polimi.ingsw.client.CLI;

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
import java.util.concurrent.*;

/**
 * @author Vasio1298
 *
 * Network Handler is the class that handles the connection with server.
 */

public class NetworkHandler extends Observable<MessageEvent> implements Runnable, Observer<MessageEvent> {
    private final static int SOCKET_PORT = 12345;
    private final static int SOCKET_TIMEOUT = 10000;

    private final Socket server;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService heartbeatService = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService messageReader = Executors.newSingleThreadExecutor();

    private boolean active;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private String lastMessage;

    public NetworkHandler(String ip) throws IOException {
        active = true;
        server = new Socket(ip, SOCKET_PORT);
        server.setSoTimeout(SOCKET_TIMEOUT);
        output = new ObjectOutputStream(server.getOutputStream());
        input = new ObjectInputStream(server.getInputStream());
    }

    /**
     * Starting method for the Network Handler thread.
     * <p>
     * The method launches a thread dedicate to the heartbeat messages and a thread dedicate to analyze the messages received by the server.
     *
     */
    @Override
    public void run() {
        System.out.println("Connected to " + server.getInetAddress());
        heartbeatService.schedule(this::heartbeatRunnable, SOCKET_TIMEOUT / 2, TimeUnit.MILLISECONDS);
        messageReader.submit(this::inputHandler);
    }


    /**
     * Method that closes the Client Connection.
     * @throws IOException if an I/O error occurs when it closes this socket
     */
    public void stop() throws IOException{
        active = false;
        messageReader.shutdownNow();
        heartbeatService.shutdownNow();
        server.close();
    }

    /**
     * Method called after a notification from the Client Controller.
     * <p>
     * The method creates a string from the message received and creates a thread that
     * handles the writing of the message to the client.
     *
     * @param message the message passed by the Controller
     */

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


    /**
     * Method that handle the sending of heartbeat messages to the server.
     * <p>
     * After every half of the timeout it sends a message of type Message Event
     * useful to let the server knows that this is still connected.
     * When the socket is not active anymore, the method sends a last message to the server
     * to let it know that this is not connected anymore and then shuts down the thread dedicate to this function.
     */
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

    /**
     * Method that reads the messages sent by the server.
     * <p>
     * The method builds a message from the string received.
     * Then, if the message is an heartbeat one the method, it does nothing,
     * otherwise it notifies the View with the message.
     */

   private void inputHandler() {
       String inputObject;
       MessageEvent messageEvent;
       try {
           while (active) {
               inputObject = (String) input.readObject();
               if (!inputObject.equals(lastMessage)) {
                   lastMessage = inputObject;

               messageEvent = new Gson().newBuilder().enableComplexMapKeySerialization().create().fromJson(inputObject, MessageEvent.class);

               if (messageEvent.getInfo() == null || !messageEvent.getInfo().equals("Heartbeat Message"))
                       notify(messageEvent);

               }
           }
       } catch (SocketTimeoutException e) {
           Client.rec();
           System.out.println("socket connection closed: if you want to try to reconnect type 'REC', else press 'q'.");
       } catch (SocketException e) {
           System.out.println("DISCONNECTED FROM THE SERVER");
           Client.close();
       } catch (ClassNotFoundException | ClassCastException | IOException exception) {
           System.out.println("invalid stream from server");
           Client.close();
       }finally {
           try {
               stop();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }
}
