package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
    static ExecutorService executor = Executors.newCachedThreadPool();
    static Socket server;

    public static void main(String[] args) {
        NetworkHandler networkHandler;
        View view = new View();
        Controller controller = new Controller();
        Scanner scanner;

        //TODO COME GESTIRE LA CHIUSURA DEL SOCKET A FINE PARTITA

        try {
            scanner = new Scanner(System.in);
            String ip = scanner.next();
            networkHandler = new NetworkHandler(ip);
            //scanner.close();
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        networkHandler.addObserver(view);       //view osserva il networkHandler
        view.addObserver(controller);           //controller osserva la view
        controller.addObserver(networkHandler); //networkHandler osserva il controller

        executor.submit(networkHandler);        //si mette in ascolto di messaggi
        view.init();

    }

        /*while(view.isAlive()){

        }*/

        //per chiudere la connessione

        /*try {
            networkHandler.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

}

