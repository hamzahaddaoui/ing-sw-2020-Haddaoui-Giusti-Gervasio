package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkHandler extends Observable implements Runnable, Observer {
    public final static int SOCKET_PORT = 12345;
    private Socket server;
    static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private boolean active;

    public NetworkHandler(String ip) throws IOException {
        server = new Socket(ip, SOCKET_PORT);
        active = true;
        output = new ObjectOutputStream(server.getOutputStream());
        input = new ObjectInputStream(server.getInputStream());
    }

    @Override
    public void run() {
        String inputObject;
        while (active) {
            try {
                inputObject = (String) input.readObject();
                notify(inputObject);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Object message){
        //messaggio da clientController a ServerController
        executor.submit(() -> {
            try {
                output.reset();
                output.writeObject(message);
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
}
