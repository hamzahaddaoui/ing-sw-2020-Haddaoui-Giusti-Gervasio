package it.polimi.ingsw.GUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class IslandLoader extends Application {

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private final Map<Point2D, Integer> boardCells = new HashMap<>();

    private final Map<Integer, Double> cellHeight = Map.of(0, 0.3, 1, - 0.9, 2, - 2.2, 3, - 3.0);

    private final Map<Group, Point3D> workers = new HashMap<>();

    Group group;
    Stage stage;
    ToggleButton button;

    Group selectedWorker;

    AtomicBoolean drag = new AtomicBoolean(false);


    private final Map<Point2D, Point2D> Point2DMap = Collections.unmodifiableMap(new HashMap<Point2D, Point2D>(){{

        /*
        * coordinate primo punto: indice di riga, indice di colonna, altezza
        * coordinate secondo punto: riga, altezza, colonna
        *
        * */

        put(new Point2D(0,0), new Point2D(-5, -5.1));
        put(new Point2D(0,1), new Point2D(-5, -2.5));
        put(new Point2D(0,2), new Point2D(-5, 0));
        put(new Point2D(0,3), new Point2D(-5, 2.5));
        put(new Point2D(0,4), new Point2D(-5, 5));

        put(new Point2D(1,0), new Point2D(-2.5, -5.1));
        put(new Point2D(1,1), new Point2D(-2.5, -2.5));
        put(new Point2D(1,2), new Point2D(-2.5, 0));
        put(new Point2D(1,3), new Point2D(-2.5, 2.5));
        put(new Point2D(1,4), new Point2D(-2.5, 5));

        put(new Point2D(2,0), new Point2D(0, -5.1));
        put(new Point2D(2,1), new Point2D(0, -2.5));
        put(new Point2D(2,2), new Point2D(0, +0));
        put(new Point2D(2,3), new Point2D(0, +2.5));
        put(new Point2D(2,4), new Point2D(0, +5));

        put(new Point2D(3,0), new Point2D(2.5, -5.1));
        put(new Point2D(3,1), new Point2D(2.5, -2.5));
        put(new Point2D(3,2), new Point2D(2.5, 0));
        put(new Point2D(3,3), new Point2D(2.5, 2.5));
        put(new Point2D(3,4), new Point2D(2.5, 5));

        put(new Point2D(4,0), new Point2D(5.1, -5.1));
        put(new Point2D(4,1), new Point2D(5.1, -2.5));
        put(new Point2D(4,2), new Point2D(5.1, 0));
        put(new Point2D(4,3), new Point2D(5.1, 2.5));
        put(new Point2D(4,4), new Point2D(5.1,5));
    }});


    public static void main(String[] args) {
        launch(args);
    }

    private Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }


    @Override
    public void start(Stage stage) throws Exception {
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                boardCells.put(new Point2D(i,j), 0);
            }
        }

        this.stage = stage;

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setLightOn(true);
        ambientLight.setColor(Color.WHITE);
        ambientLight.setOpacity(90);
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.DARKSLATEGREY);



        pointLight.getTransforms().add(new Translate(65000,-250000,65000));
        pointLight.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));


        StackPane borderPane = new StackPane();
        group = new Group();
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(3000.0);
        camera.setTranslateZ(-40);
        angleX.set(90);
        angleY.set(90);

        Group cliff = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Cliff.obj"));
        Group board = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Board.obj"));
        Group innerWalls = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/InnerWalls.obj"));
        Group outerWalls = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/OuterWall.obj"));
        Group sea = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Sea.obj"));
        Group islands = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Islands.obj"));
        Group boat = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Boat.obj"));

        Group tree1 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree1.obj"));
        Group tree2 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree2.obj"));
        Group tree3 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree3.obj"));
        Group tree4 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree4.obj"));
        Group tree5 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree5.obj"));
        Group tree6 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree6.obj"));
        Group sheep1 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/sheep1.obj"));
        Group sheep2 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/sheep2.obj"));
        Group sheep3 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/sheep3.obj"));
        Group wolf1 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/wolf1.obj"));
        Group wolf2 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/wolf2.obj"));
        Group bird1 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/bird1.obj"));
        Group bird2 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/bird2.obj"));
        Group bird3 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/bird3.obj"));



        innerWalls.setMouseTransparent(true);
        outerWalls.setMouseTransparent(true);


        Timeline timelineBoat = new Timeline(
                new KeyFrame(
                        Duration.seconds(5),
                        new KeyValue(boat.translateXProperty(), 13)
                )
        );


        RotateTransition rotate1 = new RotateTransition();
        rotate1.setAxis(Rotate.Y_AXIS);
        // setting the angle of rotation
        rotate1.setByAngle(180);
        //Setting duration of the transition
        rotate1.setDuration(Duration.millis(5000));
        rotate1.setNode(boat);



        Timeline timelineBoat3 = new Timeline(
                new KeyFrame(
                        Duration.seconds(5),
                        new KeyValue(boat.translateXProperty(), 0)
                )
        );

        RotateTransition rotate2 = new RotateTransition();
        rotate2.setAxis(Rotate.Y_AXIS);
        // setting the angle of rotation
        rotate2.setByAngle(-180);
        //Setting duration of the transition
        rotate2.setDuration(Duration.millis(5000));
        rotate2.setNode(boat);

        SequentialTransition transition = new SequentialTransition(timelineBoat, rotate1, timelineBoat3, rotate2);
        //transition.setAutoReverse(true);
        transition.setCycleCount(-1);
        transition.play();

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(4),
                        new KeyValue(angleX, 30),
                        new KeyValue(angleY, 360)
                )
        );
        timeline.play();


        group.getChildren().addAll(cliff,board, islands, wolf1, wolf2, tree1, tree2, tree3, tree4, sheep1, sheep2, sheep3, bird1, bird2, bird3, tree5, tree6,
                innerWalls,outerWalls, sea, ambientLight, pointLight, boat);



        SubScene subScene = new SubScene(new Group(group),1280,720, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.TRANSPARENT);
        borderPane.getChildren().add(subScene);
        VBox vbox = new VBox();
        button  = new ToggleButton("add worker");
        vbox.getChildren().addAll(button, new Button("bottone1"),new Button("bottone2"),new Button("bottone3"),new Button("bottone4"),new Button("bottone5"),new Button("bottone6"));
        borderPane.getChildren().add(vbox);

        vbox.setPickOnBounds(false);


        board.setOnMouseEntered(e -> board.setCursor(Cursor.HAND));



        board.setOnMouseDragged(e -> drag.set(true));

        board.setOnMouseClicked(e -> {
            Point3D clickPoint = e.getPickResult().getIntersectedPoint();
            Point2D point = findCell(new Point2D(clickPoint.getX(), clickPoint.getZ()));
            //System.out.println(point.getX() + " " + point.getY());
            movingHandler(point);
        });





        Scene scene = new Scene(borderPane, 1280, 720);

        Image img = new Image("images/title_sky.png");
        BackgroundImage bgImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        Background background = new Background(bgImg);
        borderPane.setBackground(background);



        stage.setScene(scene);


        initMouseControl(group, scene, camera);
        subScene.addEventHandler(ZoomEvent.ZOOM, zoomEvent -> {
            camera.setTranslateZ(camera.getTranslateZ()/zoomEvent.getZoomFactor());
            if(camera.getTranslateZ()>-15)
                camera.setTranslateZ(-15);
            else if (camera.getTranslateZ() < -100)
                camera.setTranslateZ(-100);

            //System.out.println(camera.getTranslateZ() +" "+ zoomEvent.getZoomFactor());
        });

        stage.show();
    }


    private void initMouseControl(Group group, Scene scene, Camera camera) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
            //System.out.println(angleX.get()+" "+angleY.get());
            if (angleX.get() > 90.0)
                angleX.set(90.0);
            if (angleX.get() < 15.0)
                angleX.set(15.0);
        });
    }

    private Point2D findCell(Point2D clickPoint){
        final double limit = -6;
        final double offset = 2.4;
        Point2D upperPoint = new Point2D(limit, 0);
        Point2D lowerPoint = new Point2D(limit+offset, 0);

        int i, j;

        for(i=0;i<5;i++){
            if(upperPoint.subtract(lowerPoint).dotProduct(clickPoint.subtract(lowerPoint)) >= 0 && lowerPoint.subtract(upperPoint).dotProduct(clickPoint.subtract(upperPoint)) >= 0)
                break;
            upperPoint = new Point2D(upperPoint.getX()+offset,0);
            lowerPoint = new Point2D(lowerPoint.getX()+offset,0);
        }


        upperPoint = new Point2D(0, limit);
        lowerPoint = new Point2D(0, limit+offset);

        for (j=0; j<5;j++){
            if(upperPoint.subtract(lowerPoint).dotProduct(clickPoint.subtract(lowerPoint)) >= 0 && lowerPoint.subtract(upperPoint).dotProduct(clickPoint.subtract(upperPoint)) >= 0)
                break;
            upperPoint = new Point2D(0,upperPoint.getY()+offset);
            lowerPoint = new Point2D(0,lowerPoint.getY()+offset);
        }

        if (i>=4)
            i=4;

        if (j>=4)
            j=4;
        return new Point2D(i,j);
    }


    private void movingHandler(Point2D point){
        if (drag.get()){
            drag.set(false);
            return;
        }
        if (selectedWorker!=null){
            moveWorker(selectedWorker, point);
            selectedWorker = null;
        }

        else if (button.isSelected()){
            putWorker(point);
        }
        else {
            switch (boardCells.get(point)) {
                case 0:
                    buildBlockLevel1(point);
                    break;
                case 1:
                    buildBlockLevel2(point);
                    break;
                case 2:
                    buildBlockLevel3(point);
                    break;
                case 3:
                    buildDome(point);
                    break;
            }

            stage.show();
        }
    }


    private void putWorker(Point2D point){
        if (boardCells.get(point) == 4)
            return;
        Group worker = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/worker_blue_0.obj"));

        worker.setTranslateX(-worker.localToScene(worker.getBoundsInLocal()).getMaxX()+worker.localToScene(worker.getBoundsInLocal()).getWidth()/2);
        worker.setTranslateY(-worker.localToScene(worker.getBoundsInLocal()).getMaxY());
        worker.setTranslateZ(-worker.localToScene(worker.getBoundsInLocal()).getMaxZ()+worker.localToScene(worker.getBoundsInLocal()).getDepth()/2);

        worker.setTranslateX(Point2DMap.get(point).getX());
        //worker.setTranslateY(cellHeight.get(boardCells.get(point)));
        worker.setTranslateZ(Point2DMap.get(point).getY());
        //worker.setMouseTransparent(true);

        workers.put(worker, new Point3D(point.getX(), point.getY(), boardCells.get(point)));


        Translate translate = new Translate();
        double height = - 10;
        worker.setTranslateY(height);
        worker.getTransforms().add(translate);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.35),
                        new KeyValue(translate.yProperty(), - height + cellHeight.get(boardCells.get(point)))
                )
        );
        timeline.play();

        worker.setOnMouseEntered(e -> worker.setCursor(Cursor.HAND));

        worker.setOnMouseClicked(e -> {
            selectedWorker = worker;
        });

        group.getChildren().add(worker);
        stage.show();
    }

    private void buildBlockLevel1(Point2D point){
        Group block = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/BuildingBlock01.obj"));
        block.setTranslateX(-block.localToScene(block.getBoundsInLocal()).getMaxX()+block.localToScene(block.getBoundsInLocal()).getWidth()/2);
        //block.setTranslateY(-block.localToScene(block.getBoundsInLocal()).getMaxY());
        block.setTranslateZ(-block.localToScene(block.getBoundsInLocal()).getMaxZ()+block.localToScene(block.getBoundsInLocal()).getDepth()/2);
        //block.setMouseTransparent(true);
        block.setTranslateX(Point2DMap.get(new Point2D(point.getX(), point.getY())).getX());
        block.setTranslateZ(Point2DMap.get(new Point2D(point.getY(), point.getY())).getY());

        boardCells.replace(point, 1);
        Translate translate = new Translate();
        double height = -10;
        block.setTranslateY(height);
        block.getTransforms().add(translate);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.35),
                        new KeyValue(translate.yProperty(), -height)
                )
        );
        timeline.play();


        group.getChildren().add(block);

        block.setOnMouseEntered(e -> block.setCursor(Cursor.HAND));

        block.setOnMouseClicked(e -> {
            movingHandler(point);
        });
        stage.show();

    }

    private void buildBlockLevel2(Point2D point){
        Group block = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/BuildingBlock02.obj"));
        block.setTranslateX(-block.localToScene(block.getBoundsInLocal()).getMaxX()+block.localToScene(block.getBoundsInLocal()).getWidth()/2);
        //block.setTranslateY(-block.localToScene(block.getBoundsInLocal()).getMaxY());
        block.setTranslateZ(-block.localToScene(block.getBoundsInLocal()).getMaxZ()+block.localToScene(block.getBoundsInLocal()).getDepth()/2);
        //block.setMouseTransparent(true);
        block.setTranslateX(Point2DMap.get(new Point2D(point.getX(), point.getY())).getX());
        block.setTranslateZ(Point2DMap.get(new Point2D(point.getY(), point.getY())).getY());
        boardCells.replace(point, 2);

        Translate translate = new Translate();
        double height = -10;
        block.setTranslateY(height);
        block.getTransforms().add(translate);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.35),
                        new KeyValue(translate.yProperty(), -height)
                )
        );
        timeline.play();

        group.getChildren().add(block);
        block.setOnMouseEntered(e -> block.setCursor(Cursor.HAND));
        block.setOnMouseClicked(e -> {
            movingHandler(point);
        });
        stage.show();
    }

    private void buildBlockLevel3(Point2D point){
        Group block = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/BuildingBlock03.obj"));
        block.setTranslateX(-block.localToScene(block.getBoundsInLocal()).getMaxX()+block.localToScene(block.getBoundsInLocal()).getWidth()/2);
        //block.setTranslateY(-block.localToScene(block.getBoundsInLocal()).getMaxY());
        block.setTranslateZ(-block.localToScene(block.getBoundsInLocal()).getMaxZ()+block.localToScene(block.getBoundsInLocal()).getDepth()/2);
        //block.setMouseTransparent(true);
        block.setTranslateX(Point2DMap.get(new Point2D(point.getX(), point.getY())).getX());
        block.setTranslateZ(Point2DMap.get(new Point2D(point.getY(), point.getY())).getY());
        boardCells.replace(point, 3);

        Translate translate = new Translate();
        double height = -10;
        block.setTranslateY(height);
        block.getTransforms().add(translate);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.35),
                        new KeyValue(translate.yProperty(), -height)
                )
        );
        timeline.play();


        group.getChildren().add(block);
        block.setOnMouseEntered(e -> block.setCursor(Cursor.HAND));
        block.setOnMouseClicked(e -> {
            movingHandler(point);
        });
        stage.show();
    }

    private void buildDome(Point2D point){
        Group block = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Dome.obj"));
        block.setTranslateX(-block.localToScene(block.getBoundsInLocal()).getMaxX()+block.localToScene(block.getBoundsInLocal()).getWidth()/2);

        //block.setTranslateY(-block.localToScene(block.getBoundsInLocal()).getMaxY());
        block.setTranslateZ(-block.localToScene(block.getBoundsInLocal()).getMaxZ()+block.localToScene(block.getBoundsInLocal()).getDepth()/2);
        //block.setMouseTransparent(true);
        block.setTranslateX(Point2DMap.get(new Point2D(point.getX(), point.getY())).getX());
        block.setTranslateZ(Point2DMap.get(new Point2D(point.getY(), point.getY())).getY());
        boardCells.replace(point, 4);

        Translate translate = new Translate();
        double height = -10;
        block.setTranslateY(height);
        block.getTransforms().add(translate);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.35),
                        new KeyValue(translate.yProperty(), -height)
                )
        );
        timeline.play();

        group.getChildren().add(block);
        stage.show();
    }

    private void moveWorker(Group worker, Point2D point){
        if (boardCells.get(point) == 4)
            return;

        if (boardCells.get(point) - workers.get(worker).getZ() > 1)
            return;

        //Group worker = loadModel(IslandLoader.class.getClassLoader().getResource("worker_blue_0.obj"));

        //worker.setTranslateX(-worker.localToScene(worker.getBoundsInLocal()).getMaxX()+worker.localToScene(worker.getBoundsInLocal()).getWidth()/2);
        //worker.setTranslateY(-worker.localToScene(worker.getBoundsInLocal()).getMaxY());
        //worker.setTranslateZ(-worker.localToScene(worker.getBoundsInLocal()).getMaxZ()+worker.localToScene(worker.getBoundsInLocal()).getDepth()/2);

//        worker.setTranslateX(Point2DMap.get(point).getX());
  //      worker.setTranslateY(cellHeight.get(boardCells.get(point)));
    //    worker.setTranslateZ(Point2DMap.get(point).getY());
        //worker.setMouseTransparent(true);

        Point3D prev = new Point3D(Point2DMap.get(new Point2D(workers.get(worker).getX(),workers.get(worker).getY())).getX(), cellHeight.get((int) workers.get(worker).getZ()) , Point2DMap.get(new Point2D(workers.get(worker).getX(),workers.get(worker).getY())).getY());

        System.out.println(prev);

        Point3D next = new Point3D(Point2DMap.get(point).getX(), cellHeight.get(boardCells.get(point)), Point2DMap.get(point).getY());

        System.out.println(next);

        Point3D trasl = next.subtract(prev);


        System.out.println(trasl);


        /*worker.setTranslateX(point3D.getX());
        worker.setTranslateY(point3D.getY());
        worker.setTranslateZ(point3D.getZ());*/

        Translate translate = new Translate();
        worker.getTransforms().add(translate);

        /*if (boardCells.get(point) - workers.get(worker).getZ() == 1){
            timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0.25),
                            //new KeyValue(translate.xProperty(), trasl.getX()),
                            new KeyValue(translate.yProperty(), trasl.getY())
                            //new KeyValue(translate.zProperty(), trasl.getZ())
                    ),
                    new KeyFrame(
                            Duration.seconds(0.25),
                            new KeyValue(translate.xProperty(), trasl.getX()/2),
                            new KeyValue(translate.yProperty(), -3),
                            new KeyValue(translate.zProperty(), trasl.getZ()/2)
                    ),
                    new KeyFrame(
                            Duration.seconds(0.25),
                            new KeyValue(translate.xProperty(), trasl.getX()),
                            new KeyValue(translate.yProperty(), 3),
                            new KeyValue(translate.zProperty(), trasl.getZ())
                    )
            );
        }
        else if(boardCells.get(point) - workers.get(worker).getZ() == 0){
            timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(2),
                            new KeyValue(translate.xProperty(), trasl.getX()/2),
                            new KeyValue(translate.yProperty(), -3),
                            new KeyValue(translate.zProperty(), trasl.getZ()/2)
                    )
                    new KeyFrame(
                            Duration.seconds(2),
                            new KeyValue(translate.xProperty(), trasl.getX()),
                            new KeyValue(translate.yProperty(), 3),
                            new KeyValue(translate.zProperty(), trasl.getZ())
                    )
            );
        }

        else{
            timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0.25),
                            new KeyValue(translate.xProperty(), trasl.getX()/2),
                            new KeyValue(translate.yProperty(), 3),
                            new KeyValue(translate.zProperty(), trasl.getZ()/2)
                    ),
                    new KeyFrame(
                            Duration.seconds(0.25),
                            new KeyValue(translate.xProperty(), trasl.getX()/2),
                            new KeyValue(translate.yProperty(), -3),
                            new KeyValue(translate.zProperty(), trasl.getZ()/2)
                    ),
                    new KeyFrame(
                            Duration.seconds(0.25),
                            //new KeyValue(translate.xProperty(), trasl.getX()),
                            new KeyValue(translate.yProperty(), trasl.getY())
                            //new KeyValue(translate.zProperty(), trasl.getZ())
                    )
            );
        }*/

        //timeline.setCycleCount(2);
        //timeline.play();

        /*Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.25),
                        new KeyValue(translate.xProperty(), trasl.getX()/2),
                        new KeyValue(translate.yProperty(), -2),
                        new KeyValue(translate.zProperty(), trasl.getZ()/2)
                )
        );
        Timeline timeline2 = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.25),
                        new KeyValue(translate.xProperty(), trasl.getX()),
                        new KeyValue(translate.yProperty(), 0),
                        new KeyValue(translate.zProperty(), trasl.getZ())
                )
        );

        SequentialTransition sequence = new SequentialTransition(timeline, timeline2);
        sequence.play();*/



        SequentialTransition sequence = new SequentialTransition();

        if (boardCells.get(point) - workers.get(worker).getZ() == 1){
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(.15*(boardCells.get(point) - workers.get(worker).getZ())),
                            new KeyValue(translate.yProperty(), trasl.getY())
                    )
            );

            Timeline timeline2 = new Timeline();
            KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
            KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
            KeyValue yKV = new KeyValue(translate.yProperty(), trasl.getY()-1, new Interpolator() {
                @Override
                protected double curve(double t) {
                    return -4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence = new SequentialTransition(timeline1, timeline2);
        }


        else if (boardCells.get(point) - workers.get(worker).getZ() == 0){
            Timeline timeline = new Timeline();

            KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
            KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
            KeyValue yKV = new KeyValue(translate.yProperty(), trasl.getY()-1, new Interpolator() {
                @Override
                protected double curve(double t) {
                    return -4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence = new SequentialTransition(timeline);
        }

        else{
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(.15*-(boardCells.get(point) - workers.get(worker).getZ())),
                            new KeyValue(translate.yProperty(), trasl.getY())
                    )
            );

            Timeline timeline2 = new Timeline();
            KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
            KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
            KeyValue yKV = new KeyValue(translate.yProperty(), -1, new Interpolator() {
                @Override
                protected double curve(double t) {
                    return -4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence = new SequentialTransition(timeline2, timeline1);
        }

        sequence.play();






        workers.put(worker, new Point3D(point.getX(), point.getY(), boardCells.get(point)));
        stage.show();
    }


    class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }


}
