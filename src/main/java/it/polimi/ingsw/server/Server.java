package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
  public final static int SOCKET_PORT = 12345;
  public final static int SOCKET_TIMEOUT = 5000;
  static ExecutorService executor = Executors.newCachedThreadPool();
  static ServerSocket socket;
  static Controller controller = new Controller();

  static Map<Integer, ClientHandler> clientSocket = new HashMap<>(); //playerID - socket

  public static void addClientSocket(Integer playerID, ClientHandler clientHandler){
    clientSocket.put(playerID, clientHandler);
  }

  public static void removeClientSocket(Integer playerID){
    clientSocket.remove(playerID).setPlayerID(null);
  }

  public static ClientHandler getClientHandler(int playerID){
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
        client.setSoTimeout(SOCKET_TIMEOUT);
        clientHandler = new ClientHandler(client);

        clientHandler.addObserver(controller);  //controller osserva clientHandler (comunicazioni CLIENT_CONTROLLER-SERVER_CONTROLLER)
        controller.addObserver(clientHandler);  //clientHandler osserva controller (comunicazioni CONTROLLER-VIEW)

        executor.submit(clientHandler);

      } catch (IOException e) {
        System.out.println("connection dropped");
      }
    }
  }

}
