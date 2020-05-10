package it.polimi.ingsw.gui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
//import com.gluonhq.charm.glisten.animation.CachedTimelineTransition;


public class Controller implements Initializable{
    @FXML
    private ImageView santorini ;

    @FXML
    private TextField nickname;

    @FXML
    private TextField address;



    Stage stage;

    public void setStage (Stage stage){
        this.stage = stage;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ScaleTransition rt = new ScaleTransition(Duration.seconds(0.0005), santorini);
        rt.setCycleCount(1);
        rt.setToX(0);
        rt.setToY(0);
        rt.play();

        ScaleTransition rt2 = new ScaleTransition(Duration.seconds(16), santorini);
        rt2.setCycleCount(1);
        rt2.setToX(1.15);
        rt2.setToY(1.15);
        rt2.play();

        ScaleTransition rt3 = new ScaleTransition(Duration.seconds(16), santorini);
        rt3.setCycleCount(1);
        rt3.setToX(1);
        rt3.setToY(1);

        rt2.setOnFinished((e) -> rt3.play());
    }

    public void play(MouseEvent mouseEvent) throws Exception{
        String IP = address.getCharacters().toString();
        String nick =  nickname.getCharacters().toString();
        System.out.println("Connection to: "+ IP +  " - Nickname: "+nick );
        gotoSelectPlayersNum();
    }


    private void gotoSelectPlayersNum() throws Exception{
        replaceSceneContent("selectNum.fxml");
    }

    private void replaceSceneContent(String fxml) throws Exception {
        Parent page = (Parent) FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(fxml)));
        stage.getScene().setRoot(page);
        //stage.sizeToScene();
        stage.show();
        //return page;
    }

}