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

    @Override
    public void start(Stage stage) throws IOException{
        Database.setStage(stage);
        stage.setTitle("Santorini online");
        stage.setMaxWidth(1200);
        stage.setMaxHeight(800);
        stage.setResizable(false);


        //COMMENTATI PER DEBUG. NORMALMENTE SONO QUESTE LE ISTRUZIONI DA RUNNARE
        View.updateView();
        Database.getCurrentState().showPane();


        //setCurrentState(new Running());
        //getCurrentState().showPane();

        /*setPlayerID(3);
        setMatchPlayers(Map.of(1,"Hamza", 2, "Leo", 3, "Vasio"));
        setMatchColors(Map.of(1,"Blue", 2, "Orange", 3, "Purple"));
        setCurrentPlayer(1);
        setPlayerState(PlayerState.ACTIVE);
        setMatchCards(new ArrayList<>(Arrays.asList("Atlas", "Pan", "Athena")));
        setPlacingAvailableCells(Set.of(new Position(0,0),new Position(1,1),new Position(2,2),new Position(3,3),new Position(4,4)));
        setCurrentState(new PlacingWorkers());
        getCurrentState().showPane();*/

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