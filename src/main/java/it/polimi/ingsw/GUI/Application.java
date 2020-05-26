package it.polimi.ingsw.GUI;

import java.util.Arrays;

public class Application {
    public static void main(String[] args){
        if (args[0].equals("-cli")){
            it.polimi.ingsw.client.Client.main(args);
        }
        else {
            Client.main(args);
        }
    }
}
