package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.GameModel;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
  public final static int SOCKET_PORT = 12345;
  static ExecutorService executor = Executors.newCachedThreadPool();
  static ServerSocket socket;
  static ServerController controller = new ServerController();
  static GameModel model = new GameModel();
  static ClientHandler clientHandler;

  public static void main(String[] args){


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
        clientHandler = new ClientHandler(client);
        clientHandler.addObserver(controller);
        model.addObserver(clientHandler);
        executor.submit(clientHandler);
      }
      catch (IOException e) {
        System.out.println("connection dropped");
      }
    }
  }
}
