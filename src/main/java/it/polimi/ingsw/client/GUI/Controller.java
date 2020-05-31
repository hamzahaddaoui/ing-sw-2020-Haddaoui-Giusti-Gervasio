package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.StartState;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class Controller {


    public static void replaceSceneContent(String fxml) {
        Parent page;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Controller.class.getClassLoader().getResource(fxml)); //potrebbe dare problemi, ma non credo
        try {
            page = fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Failed to load fxml file.");
            exception.printStackTrace();
            return;
        }


        Scene scene = new Scene(page);
        Database.setScene(scene);
        Database.setCurrentState(fxmlLoader.getController());

        Database.getStage().setScene(scene);

        //MAGARI QUI VA LA TRANSIZIONE


        Database.getStage().show();
    }

    public static boolean setServer() throws IOException {
        Database.setNetworkHandler(new NetworkHandler(Database.getIP()));
        new Thread(Database.getNetworkHandler()).start();
        return true;
    }



    public static void exit(){
        Platform.exit();
        System.exit(0);
    }

    public static void userNewGame(){
        //provare a chiudere conneessione server
        Database.wipeData();
        Database.setCurrentState(new StartState());
        Database.getCurrentState().showPane();
    }



}
