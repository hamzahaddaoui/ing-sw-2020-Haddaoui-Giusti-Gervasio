package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.GameModel;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
  public final static int SOCKET_PORT = 12345;
  static ExecutorService executor = Executors.newCachedThreadPool();
  static ServerSocket socket;
  static ServerController controller = new ServerController();
  static GameModel model = new GameModel();
  static int progressiveID = 1;

  static Map<Socket, Integer> socketUsers = new HashMap<>(); //socket - playerID
  static Map<Integer, Integer> usersMatches = new HashMap<>(); // playerID - matchID

  public static void setUsersMatches(int playerID, int matchID){
    usersMatches.put(playerID, matchID);
  }

  public static Integer getUserMatch(int playerID){
    return usersMatches.get(playerID);
  }

  public static void main(String[] args){
    ClientHandler clientHandler;

    try {
      socket = new ServerSocket(SOCKET_PORT);
    }
    catch (IOException e) {
      System.out.println("cannot open server socket");
      System.exit(1);
      return;
    }

    while (true) {
      try {
        Socket client = socket.accept();
        clientHandler = new ClientHandler(client, progressiveID);

        socketUsers.put(client, progressiveID);

        clientHandler.addObserver(controller);  //controller osserva clientHandler
        model.addObserver(clientHandler);       //clientHandler osserva Model

        executor.submit(clientHandler);

        progressiveID++;
      }
      catch (IOException e) {
        System.out.println("connection dropped");
      }
    }
  }
}
