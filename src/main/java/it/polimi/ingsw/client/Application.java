package it.polimi.ingsw.client;

import it.polimi.ingsw.client.GUI.Client;

public class Application {
    public static void main(String[] args){
        if (args.length != 0 && args[0].equals("-cli")){
            it.polimi.ingsw.client.CLI.Client.main(args);
        }
        else {
            Client.main(args);
        }
    }
}