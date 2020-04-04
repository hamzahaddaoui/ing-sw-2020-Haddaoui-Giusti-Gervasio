package it.polimi.ingsw.server;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<ClientConnection, Integer> playerConnection = new HashMap<>(); //idconnessione, idutente
    private Map<Integer, Integer> playerMatch = new HashMap<>(); //idutente, idmatch

    public void setPlayerConnection(ClientConnection client, Integer playerID) {
        this.playerConnection.put(client, playerID);
    }

    public void setPlayerMatch(Integer playerID, Integer matchID) {
        this.playerMatch.put(playerID, matchID);
    }

    public void removePlayerConnection(ClientConnection client, Integer playerID) {
        this.playerConnection.remove(client);
    }

    public void removePlayerMatch(Integer playerID, Integer matchID) {
        this.playerMatch.remove(playerID);
    }

    public synchronized void deregisterConnection(ClientConnection connection) {
        /*ClientConnection opponent = playingConnection.get(c);
        if(opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);
        Iterator<String> iterator = waitingConnection.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingConnection.get(iterator.next())==c){
                iterator.remove();
            }
        }*/
    }

    public Map<ClientConnection, Integer> getPlayerConnection() {
        return playerConnection;
    }

    public Map<Integer, Integer> getPlayerMatch() {
        return playerMatch;
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();

                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

}
