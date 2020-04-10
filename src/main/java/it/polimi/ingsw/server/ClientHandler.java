package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.VCEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHandler extends Observable<Object> implements Observer<Object>, Runnable {
  ExecutorService executor = Executors.newSingleThreadExecutor();
    private Integer matchID;
    private Integer playerID;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket client;
    private boolean active;

  /*
  TO - DO
  Inserire timeout connessione + heartbeat messages
   */


    ClientHandler(Socket client, int playerID) throws IOException {
        this.client = client;
        this.playerID = playerID;
        this.active = true;
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }


    @Override
    public void run() {
        String inputObject;
        VCEvent message;

        try {
            System.out.println("Connected to " + client.getInetAddress());
            try {
                while (active) {
                    matchID = Server.getUserMatch(playerID);
                    inputObject = (String) input.readObject();
                    message = new Gson().fromJson(inputObject,VCEvent.class);
                    notify(playerID, matchID, message);
                }
            }
            catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("invalid stream from client");
            }

            client.close();
        }
        catch (IOException ex) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }

    @Override
    public void update(Object message){
        return;
    }

    @Override
    public void update(Integer playerID, Integer matchID, Object message){
        if (this.matchID != matchID){
            return;
        }

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
}
