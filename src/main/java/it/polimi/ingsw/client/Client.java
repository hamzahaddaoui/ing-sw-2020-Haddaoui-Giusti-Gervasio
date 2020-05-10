package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
    static ExecutorService inputListener = Executors.newSingleThreadExecutor();
    static ExecutorService networkListener = Executors.newSingleThreadExecutor();
    //static ExecutorService viewManager = Executors.newSingleThreadExecutor();
    static NetworkHandler networkHandler;
    static View view;
    static Controller controller;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        view = new View();
        controller = new Controller();

        //TODO COME GESTIRE LA CHIUSURA DEL SOCKET A FINE PARTITA

        try {
            System.out.println("Insert ip address: ('d' or 'default' for the default ip) ");
            String ip = scanner.next();
            if (ip.equals("d") || ip.equals("default"))
                ip = "127.0.0.1";
            networkHandler = new NetworkHandler(ip);
            //scanner.close();
        } catch (IOException e) {
            System.out.println("SERVER UNREACHABLE");
            return;
        }
        System.out.println("CONNECTED");

        networkHandler.addObserver(view);       //view osserva il networkHandler
        controller.addObserver(networkHandler); //networkHandler osserva il controller

        networkListener.submit(networkHandler);
        new Thread(View::print).start();
        inputListener.submit(controller);
    }

    public static void close() {
        if (!View.isActive()) {
            try {
                networkHandler.stop();
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }


}

