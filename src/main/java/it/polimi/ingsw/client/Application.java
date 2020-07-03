package it.polimi.ingsw.client;

/**
 * @author: hamzahaddaoui
 *
 * Entry class of the Client application.
 * Decides, based on the input to launch the cli version or the gui version
 */

public class Application {
    public static void main(String[] args){
        if (args.length != 0 && args[0].equals("-cli")){
            it.polimi.ingsw.client.CLI.Client.main(args);
        }
        else {
            it.polimi.ingsw.client.GUI.Client.main(args);
        }
    }
}
