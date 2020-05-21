package it.polimi.ingsw.GUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.GUI.controller.Running;
import it.polimi.ingsw.utilities.Position;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Lighting;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.GUI.Database.*;
import static javafx.application.Application.launch;

public class IslandLoader{

    private static double anchorX, anchorY;
    private static double anchorAngleX = 0;
    private static double anchorAngleY = 0;
    private static final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private static final DoubleProperty angleY = new SimpleDoubleProperty(0);

    //altezza ideale di ogni cella 0 1 2 3
    private static final Map<Point2D, Integer> boardCells = new HashMap<>();

    //altezza reale di ogni cella
    private static final Map<Integer, Double> cellHeight = Map.of(0, 0.3, 1, - 0.9, 2, - 2.2, 3, - 3.0);

    //posizione ideale dei worker nella mappa
    private static final Map<Group, Point3D> workers = new HashMap<>();

    static Group group;
    static Stage stage;
    static ToggleButton button;


    static AtomicBoolean drag = new AtomicBoolean(false);

    //posizione reale di ogni punto
    private static final Map<Point2D, Point2D> Point2DMap = Collections.unmodifiableMap(new HashMap<Point2D, Point2D>(){{

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

    private static Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }


    public static void start(StackPane stackPane) throws Exception{
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardCells.put(new Point2D(i, j), 0);
            }
        }

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setLightOn(true);
        ambientLight.setColor(Color.WHITE);
        ambientLight.setOpacity(90);
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.DARKSLATEGREY);

        pointLight.getTransforms().add(new Translate(65000, - 250000, 65000));
        pointLight.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));

        group = new Group();
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(3000.0);
        camera.setTranslateZ(- 40);
        angleX.set(90);
        angleY.set(90);

        Group cliff = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Cliff.obj"));
        Group board = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Board.obj"));
        Group innerWalls = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/InnerWalls.obj"));
        Group outerWalls = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/OuterWall.obj"));
        Group sea = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Sea.obj"));
        Group islands = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Islands.obj"));
        Group boat = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Boat.obj"));
        innerWalls.setMouseTransparent(true);
        outerWalls.setMouseTransparent(true);
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

        //BOAT ANIMATION
        {
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
            rotate2.setByAngle(- 180);
            //Setting duration of the transition
            rotate2.setDuration(Duration.millis(5000));
            rotate2.setNode(boat);

            SequentialTransition transition = new SequentialTransition(timelineBoat, rotate1, timelineBoat3, rotate2);
            //transition.setAutoReverse(true);
            transition.setCycleCount(- 1);
            transition.play();

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(4),
                            new KeyValue(angleX, 30),
                            new KeyValue(angleY, 450)
                    )
            );
            timeline.play();
        }


        group.getChildren().addAll(cliff,board, islands, wolf1, wolf2, tree1, tree2, tree3, tree4, sheep1, sheep2, sheep3, bird1, bird2, bird3, tree5, tree6,
                innerWalls,outerWalls, sea, ambientLight, pointLight, boat);


        SubScene subScene = new SubScene(new Group(group), getStage().getMaxWidth(), getStage().getMaxHeight(), true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.TRANSPARENT);
        stackPane.getChildren().add(subScene);
        subScene.toBack();
        stackPane.lookup("#background").toBack();

        board.setOnMouseEntered(e -> board.setCursor(Cursor.HAND));
        board.setOnMouseDragged(e -> drag.set(true));
        board.setOnMouseClicked(e -> {
            Point3D click = e.getPickResult().getIntersectedPoint();
            Point2D point = new Point2D(click.getX(), click.getZ());
            clickHandler(point);

        });

        //INIZIALIZZO MOVIMENTO E ZOOM TELECAMERA
        {
        initMouseControl(group, subScene, camera);
        subScene.addEventHandler(ZoomEvent.ZOOM, zoomEvent -> {
            camera.setTranslateZ(camera.getTranslateZ()/zoomEvent.getZoomFactor());
            if(camera.getTranslateZ()>-15)
                camera.setTranslateZ(-15);
            else if (camera.getTranslateZ() < -100)
                camera.setTranslateZ(-100);
        });}
    }


    private static void initMouseControl(Group group, SubScene scene, Camera camera) {
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

    private static Point2D findCell(Point2D clickPoint){
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

    private static void clickHandler(Point2D clickPoint){
        if (drag.get()){
            drag.set(false);
            return;
        }

        Point2D point = findCell(new Point2D(clickPoint.getX(), clickPoint.getY()));
        System.out.println("Click on board/building detected on "+ point);
        ((Running) getCurrentState()).boardClick(point);
    }

    public void putWorker(Point2D point, String color){
        if (boardCells.get(point) == 4)
            return;
        Group worker = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/worker_"+color.toLowerCase()+"_0.obj"));

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

        {
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(5),
                            new KeyValue(translate.yProperty(), - height + cellHeight.get(boardCells.get(point)))
                    )
            );
            timeline.play();
        }

        worker.setOnMouseEntered(e -> worker.setCursor(Cursor.HAND));

        worker.setOnMouseClicked(e -> {
            System.out.println("Mouse clicked in "+ point);
            if (((Running) getCurrentState()).workerClick(point)){
                //Lighting lighting = new Lighting();
                //worker.setEffect(lighting);
                System.out.println("");
                //creo oggetti illuminati che indicano dove può muoversi - oggetti mouse transparent
            }
        });

        group.getChildren().add(worker);
        //stage.show();
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
            clickHandler(point);
        });
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
            clickHandler(point);
        });
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
            clickHandler(point);
        });
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
    }


    public void build(Point2D point, boolean dome){
        if (dome){
            //correggere l'altezza a seconda
            buildDome(point);
            return;
        }

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
    }

    public void moveWorker(Point2D startPos, Point2D endPos){
        Optional<Group> optionalWorker =  workers.keySet().stream()
                .filter(w -> new Point2D(workers.get(w).getX(), workers.get(w).getY()).equals(startPos))
                .findAny();

        if (!optionalWorker.isPresent()){
            System.out.println("worker non trovato");
            return;
        }


        Group worker = optionalWorker.get();

        Point3D prev = new Point3D(Point2DMap.get(new Point2D(workers.get(worker).getX(),workers.get(worker).getY())).getX(), cellHeight.get((int) workers.get(worker).getZ()) , Point2DMap.get(new Point2D(workers.get(worker).getX(),workers.get(worker).getY())).getY());

        Point3D next = new Point3D(Point2DMap.get(endPos).getX(), cellHeight.get(boardCells.get(endPos)), Point2DMap.get(endPos).getY());

        System.out.println("worker trovato "+ prev+"  "+next);

        Point3D trasl = next.subtract(prev);

        Translate translate = new Translate();
        worker.getTransforms().add(translate);

        {
            SequentialTransition sequence;

            if (boardCells.get(endPos) - workers.get(worker).getZ() == 1) {
                Timeline timeline1 = new Timeline(
                        new KeyFrame(Duration.seconds(.15 * (boardCells.get(endPos) - workers.get(worker).getZ())),
                                new KeyValue(translate.yProperty(), trasl.getY())
                        )
                );

                Timeline timeline2 = new Timeline();
                KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
                KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
                KeyValue yKV = new KeyValue(translate.yProperty(), trasl.getY() - 1, new Interpolator() {
                    @Override
                    protected double curve(double t){
                        return - 4 * (t - .5) * (t - .5) + 1;
                    }
                });
                KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
                KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
                KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
                timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

                sequence = new SequentialTransition(timeline1, timeline2);
            } else if (boardCells.get(endPos) - workers.get(worker).getZ() == 0) {
                Timeline timeline = new Timeline();

                KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
                KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
                KeyValue yKV = new KeyValue(translate.yProperty(), trasl.getY() - 1, new Interpolator() {
                    @Override
                    protected double curve(double t){
                        return - 4 * (t - .5) * (t - .5) + 1;
                    }
                });
                KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
                KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
                KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
                timeline.getKeyFrames().addAll(xKF, yKF, zKF);

                sequence = new SequentialTransition(timeline);
            } else {
                Timeline timeline1 = new Timeline(
                        new KeyFrame(Duration.seconds(.15 * - (boardCells.get(endPos) - workers.get(worker).getZ())),
                                new KeyValue(translate.yProperty(), trasl.getY())
                        )
                );

                Timeline timeline2 = new Timeline();
                KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
                KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
                KeyValue yKV = new KeyValue(translate.yProperty(), - 1, new Interpolator() {
                    @Override
                    protected double curve(double t){
                        return - 4 * (t - .5) * (t - .5) + 1;
                    }
                });
                KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
                KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
                KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
                timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

                sequence = new SequentialTransition(timeline2, timeline1);
            }

            sequence.play();

        }

        workers.put(worker, new Point3D(endPos.getX(), endPos.getY(), boardCells.get(endPos)));
        stage.show();
    }






}
