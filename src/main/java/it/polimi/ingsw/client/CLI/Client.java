package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.CLI.controller.Controller;
import it.polimi.ingsw.client.CLI.view.View;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *🟦    blu
 *🟧    arancio
 *🟪    viola
 *🟥    rosso
 *⬜️    bianco
 *🟩    verde
 *⏺    dome
 *
 */




public class Client {
    static ExecutorService inputListener = Executors.newSingleThreadExecutor();
    static ExecutorService networkListener = Executors.newSingleThreadExecutor();
    static NetworkHandler networkHandler;
    static View view;
    static Controller controller;
    static Scanner scanner = new Scanner(System.in);

    /**
     * Main method for the Client.
     * <p>
     * The method asks at the user the server's ip and then instantiate the Network Handler socket.
     * Then it assigns observer/observable roles and start a thread for the Network Handler,
     * a thread for the print method of the View and a thread for the Controller.
     *
     *
     * @param args   an array of command-line arguments for the application
     */
    public static void main(String[] args) {
        view = new View();
        controller = new Controller();

        try {
            System.out.println("Insert ip address: ('d' or 'default' for the default ip) ");
            String ip = scanner.next();
            if (ip.equals("d") || ip.equals("default"))
                ip = "127.0.0.1";
            networkHandler = new NetworkHandler(ip);
        } catch (IOException e) {
            System.out.println("SERVER UNREACHABLE");
            return;
        }
        System.out.println("CONNECTED");

        networkHandler.addObserver(view);       //view osserva il networkHandler
        controller.addObserver(networkHandler); //networkHandler osserva il controller

        networkListener.submit(networkHandler);
        new Thread(View::print).start();
        inputListener.submit(controller::inputListener);
    }


    /**
     * Method called by the current Control State when a player wants to quit from the game.
     */
    public static void close() {
         {
            try {
                networkHandler.stop();
                //scanner.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }


}

