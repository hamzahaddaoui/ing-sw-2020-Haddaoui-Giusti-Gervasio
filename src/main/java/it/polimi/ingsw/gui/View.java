package it.polimi.ingsw.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class View extends Application {
    private Stage stage;

    public static void main(String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException{
        this.stage = stage;
        URL location = getClass().getClassLoader().getResource("fxml_files/startScreen.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);

        Parent root = (Parent) fxmlLoader.load(location.openStream());

        stage.setTitle("Santorini online");
        stage.setScene(new Scene(root, 1200, 800));
        stage.setResizable(false);
        Controller mainController = fxmlLoader.getController();
        mainController.setStage(stage);


        stage.show();
    }


}