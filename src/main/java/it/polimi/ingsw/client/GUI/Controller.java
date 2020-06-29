package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.StartState;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class Controller {


    /**
     * Replace the current scene content with another one.
     * @param fxml the scene files to load
     */
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

    /**
     * Initialize the server connection.
     * @throws IOException If no connection have been established.
     */
    public static void setServer() throws IOException {
        Database.setNetworkHandler(new NetworkHandler(Database.getIP()));
        new Thread(Database.getNetworkHandler()).start();
    }

    /**
     * Close the application
     */
    public static void exit(){
        Platform.exit();
        System.exit(0);
    }

    /**
     * Start a new game, wiping out all the data.
     */
    public static void userNewGame(){
        //provare a chiudere conneessione server
        Database.wipeData();
        Database.setCurrentState(new StartState());
        Database.getCurrentState().showPane();
    }



}
