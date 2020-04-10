package it.polimi.ingsw.client;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
    static ExecutorService executor = Executors.newCachedThreadPool();
    static Socket server;

    public static void main(String[] args){
        NetworkHandler networkHandler;
        View view = new View();
        Controller controller = new Controller();

        //default view
        //richiedo all'utente qual'è l'ip del server al quale vuole collegarsi
        String ip = "127.0.0.1";

        try {
            networkHandler = new NetworkHandler(ip);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        networkHandler.addObserver(view);       //view osserva il networkHandler
        view.addObserver(controller);           //controller osserva la view
        controller.addObserver(networkHandler); //networkHandler osserva il controller

        executor.submit(networkHandler);        //si mette in ascolto di messaggi
        executor.submit(view);


        //per chiudere la connessione
        /*try {
            networkHandler.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}
