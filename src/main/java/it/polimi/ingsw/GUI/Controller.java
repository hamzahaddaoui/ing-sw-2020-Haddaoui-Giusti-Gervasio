package it.polimi.ingsw.GUI;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

import static it.polimi.ingsw.GUI.Database.*;
import static it.polimi.ingsw.GUI.Database.getNetworkHandler;

public class Controller {


    public static void replaceSceneContent(String fxml) {
        Parent page;



        /*URL location = Controller.class.getClassLoader().getResource("fxml_files/startScreen.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        System.out.println(location);
        try {
            page = fxmlLoader.load(location.openStream());
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }*/

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
        setCurrentState(fxmlLoader.getController());
        getStage().setScene(scene);
        getStage().show();
    }

    public static boolean setServer() throws IOException {
        setNetworkHandler(new NetworkHandler(Database.getIP()));
        new Thread(getNetworkHandler()).start();
        return true;
    }



    public static void exit(){
        Platform.exit();
        System.exit(0);
    }




}
