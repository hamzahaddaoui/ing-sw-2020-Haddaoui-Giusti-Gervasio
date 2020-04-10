package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.MVEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.VCEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHandler extends Observable<VCEvent> implements Observer<MVEvent>, Runnable {
  ExecutorService executor = Executors.newSingleThreadExecutor();
  private int matchID;
  private int playerID;
  private ObjectOutputStream output;
  private ObjectInputStream input;
  private Socket client;
  private boolean active;

  /*
  TO - DO
  Inserire timeout connessione + heartbeat messages
   */


  ClientHandler(Socket client) throws IOException {
    this.client = client;
    active = true;
    output = new ObjectOutputStream(client.getOutputStream());
    input = new ObjectInputStream(client.getInputStream());
  }

    public void setMatchID(int matchID){
        this.matchID = matchID;
    }

    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }

    @Override
  public void run() {
    String inputObject;
    VCEvent message;

    try {
      System.out.println("Connected to " + client.getInetAddress());
      try {
        while (active) {
          inputObject = (String) input.readObject();
          message = new Gson().fromJson(inputObject,VCEvent.class);
          notify(matchID, playerID, message);
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
    public void update(MVEvent message){
        return;
    }

    @Override
  public void update(int matchID, int playerID, MVEvent message){
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
