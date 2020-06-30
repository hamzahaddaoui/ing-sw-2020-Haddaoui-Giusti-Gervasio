package it.polimi.ingsw.client.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Client extends Application {
    static private Stage stage;

    public static void main(String[] args){
        Application.launch(args);
    }

    /**
     * Starting application method.
     * Sets the windows size and initialize the elements on the screen.
     * @param stage The stage on which to launch the application
     */
    @Override
    public void start(Stage stage) {
        Database.setStage(stage);
        stage.setTitle("Santorini ONLINE");
        stage.setMaxWidth(1200);
        stage.setMaxHeight(800);
        stage.setResizable(false);

        View.updateView();
        Database.getCurrentState().showPane();


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
                Database.getNetworkHandler().shutdownAll();
            }
        });
    }


    //TODO Transizioni tra schermate
    //TODO Restrictions on nickname input (lenght and characters)

}