package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
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
  static Controller controller = new Controller();
  static GameModel model = new GameModel();
  static int progressiveID = 1;

  static Map<Integer, ClientHandler> clientSocket = new HashMap<>(); //playerID - socket

  public static ClientHandler getSocket(int playerID){
    return clientSocket.get(playerID);
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

        clientSocket.put(progressiveID, clientHandler);

        clientHandler.addObserver(controller);  //controller osserva clientHandler (comunicazioni CLIENT_CONTROLLER-SERVER_CONTROLLER)
        model.addObserver(clientHandler);       //clientHandler osserva Model (comunicazioni MODEL-VIEW)
        controller.addObserver(clientHandler);  //clientHandler osserva controller (comunicazioni CONTROLLER-VIEW)

        executor.submit(clientHandler);

        progressiveID++;
      }
      catch (IOException e) {
        System.out.println("connection dropped");
      }
    }
  }
}
