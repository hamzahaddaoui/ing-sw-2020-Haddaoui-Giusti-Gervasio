package it.polimi.ingsw.GUI;

import java.util.Arrays;

public class Application {
    public static void main(String[] args){
        System.out.println(Arrays.toString(args));
        Client.main(args);
    }
}
