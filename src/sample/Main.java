package sample;


import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {

    private final Group root = new Group();
    private PerspectiveCamera camera;
    private final double sceneWidth = 800;
    private final double sceneHeight = 600;

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);

    private volatile boolean isPicking = false;
    private Point3D vecIni, vecPos;
    private double distance;
    private Node s;


    private static final String AIRCRAFT_URL = "G:\\tec\\B6MG\\B6MG_L.3DS";
    private static final String RADAR_URL = "G:\\tec\\130212_antenna_C-band_satellite_antenna_S180-G.3DS";

    private final double cameraQuantity = 10.0;
    private final double cameraModifier = 50.0;

    @Override
    public void start(Stage stage) {
        //ПОДГРУЖАЕМ МОДЕЛЬ САМОЛЕТА, ЗАДАЕМ УМЕНЬШЕНИЕ В МАСШТАБЕ И ЕГО НАЧАЛЬНЫЕ КООРДИНАТЫ
        File file = new File(AIRCRAFT_URL);
        TdsModelImporter importer = new TdsModelImporter();
        importer.read(file);
        Node[] mesh = importer.getImport();
        for (int i = 0; i < mesh.length; i++) {
            mesh[i].setScaleX(0.3);
            mesh[i].setScaleZ(0.3);
            mesh[i].setScaleY(0.3);
            mesh[i].setRotationAxis(Rotate.Y_AXIS);
            mesh[i].setRotate(90);
            mesh[i].setTranslateZ(500);
            mesh[i].setTranslateY(100);
            mesh[i].setTranslateX(750);
        }
        //ПОДГРУЖАЕМ МОДЕЛЬ РАДАРА, ЗАДАЕМ УМЕНЬШЕНИЕ В МАСШТАБЕ И ЕГО НАЧАЛЬНЫЕ КООРДИНАТЫ
        File file1 = new File(RADAR_URL);
        TdsModelImporter importer1 = new TdsModelImporter();
        importer1.read(file1);
        Node[] mesh1 = importer1.getImport();
        for (int i = 0; i < mesh1.length; i++) {
            mesh1[i].setRotationAxis(Rotate.Y_AXIS);
            mesh1[i].setRotate(180);
            mesh1[i].setScaleX(0.3);
            mesh1[i].setScaleZ(0.3);
            mesh1[i].setScaleY(0.3);
            mesh1[i].setTranslateZ(-650);
            mesh1[i].setTranslateY(181);
            mesh1[i].setTranslateX(800);
        }
        //ДОБАВЛЯЕМ МОДЕЛЬКИ НА СЦЕНУ
        root.getChildren().addAll(mesh);
        root.getChildren().addAll(mesh1);

        //СОЗДАЕМ ОСНОВАНИЕ РАДАРА И ЕГО ПАРАМЕТРЫ
        Box floor = new Box(40, 40, 40);
        floor.setMaterial(new PhongMaterial(Color.GRAY));
        floor.setTranslateX(3);
        floor.setTranslateY(19);
        floor.setTranslateZ(0);
        floor.setTranslateZ(-650);
        floor.setTranslateY(200);
        floor.setTranslateX(803);
        root.getChildren().add(floor);

        //СОЗДАЕМ СЦЕНУ
        Scene scene = new Scene(root, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.LIGHTBLUE);


        //СОЗДАЕМ КАМЕРУ
        camera = new PerspectiveCamera(true);
        camera.setVerticalFieldOfView(false);
        camera.setNearClip(0.1);
        camera.setFarClip(100000.0);
        camera.getTransforms().addAll(rotateX, rotateY, new Translate(0, 0, -3000));

        //ДОБАВЛЯЕМ СВЕТ И КАМЕРУ НА СЦЕНУ
        PointLight light = new PointLight(Color.GAINSBORO);
        root.getChildren().add(light);
        root.getChildren().add(new AmbientLight(Color.WHITE));
        scene.setCamera(camera);

        //ОБРАБОТЧКИКИ НАЖАТИЙ КЛАВИШ И МЫШИ
//        scene.setOnMousePressed((MouseEvent me) -> {
//            for (int i = 0; i < mesh.length; i++) {
//                mousePosX = me.getSceneX();
//                mousePosY = me.getSceneY();
//                PickResult pr = me.getPickResult();
//                if (pr != null && pr.getIntersectedNode() != null && pr.getIntersectedNode() instanceof Node) {
//                    distance = pr.getIntersectedDistance();
//                    s = (Node) pr.getIntersectedNode();
//                    isPicking = true;
//                    vecIni = unProjectDirection(mousePosX, mousePosY, scene.getWidth(), scene.getHeight());
//                }
//            }
//        });
//        scene.setOnMouseDragged((MouseEvent me) -> {
//            mousePosX = me.getSceneX();
//            mousePosY = me.getSceneY();
//            if(isPicking){
//                vecPos = unProjectDirection(mousePosX, mousePosY, scene.getWidth(),scene.getHeight());
//                Point3D p=vecPos.subtract(vecIni).multiply(distance);
//                s.getTransforms().add(new Translate(p.getX(),p.getY(),p.getZ()));
//                vecIni=vecPos;
//                PickResult pr = me.getPickResult();
//                if(pr!=null && pr.getIntersectedNode() != null && pr.getIntersectedNode()==s){
//                    distance=pr.getIntersectedDistance();
//                } else {
//                    isPicking=false;
//                }
//            } else {
//                rotateX.setAngle(rotateX.getAngle()-(mousePosY - mouseOldY));
//                rotateY.setAngle(rotateY.getAngle()+(mousePosX - mouseOldX));
//                mouseOldX = mousePosX;
//                mouseOldY = mousePosY;
//            }
//        });
//        scene.setOnMouseReleased((MouseEvent me)->{
//            if(isPicking){
//                isPicking=false;
//            }
//        });


        Point3D pointX = new Point3D(1, 0, 0);
        Point3D pointY = new Point3D(0, 1, 0);
        //  Point3D pointZ = new Point3D(0, 0, 1);
        scene.setOnKeyPressed(event -> {
            double change = cameraQuantity;
            for (int i = 0; i < mesh.length; i++) {
                if (event.isShiftDown()) {
                    change = cameraModifier;
                }
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.W) {
                    mesh[i].setTranslateZ(mesh[i].getTranslateZ() + change);
                    if (mesh[i].getRotationAxis().equals(pointX)) {
                        mesh[i].setRotationAxis(Rotate.Y_AXIS);
                        mesh[i].setRotate(180);
                    } else if (mesh[i].getRotationAxis().equals(pointY)) {
                        mesh[i].setRotationAxis(Rotate.Y_AXIS);
                        mesh[i].setRotate(180);
                    }
                }
                if (keyCode == KeyCode.S) {
                    mesh[i].setTranslateZ(mesh[i].getTranslateZ() - change);
                    if (mesh[i].getRotationAxis().equals(pointY)) {
                        mesh[i].setRotationAxis(Rotate.X_AXIS);
                        mesh[i].setRotate(5);
                    }
                }
                if (keyCode == KeyCode.A) {
                    mesh[i].setTranslateX(mesh[i].getTranslateX() - change);
                    mesh1[i].setRotate(mesh1[i].getRotate()-1);
                    if (mesh[i].getRotationAxis().equals(pointY)) {
                        mesh[i].setRotate(90);
                        mesh1[i].setRotate(mesh1[i].getRotate()-1);
                    } else if (mesh[i].getRotationAxis().equals(pointX)) {
                        mesh[i].setRotationAxis(Rotate.Y_AXIS);
                        mesh[i].setRotate(90);
                        mesh1[i].setRotate(mesh1[i].getRotate()-1);
                    }
                    mesh1[i].setRotate(mesh1[i].getRotate()-1);
                }
                if (keyCode == KeyCode.D) {
                    mesh[i].setTranslateX(mesh[i].getTranslateX() + change);
                    if (mesh[i].getRotationAxis().equals(pointY)) {
                        mesh[i].setRotate(-90);
                    } else if (mesh[i].getRotationAxis().equals(pointX)) {
                        mesh[i].setRotationAxis(Rotate.Y_AXIS);
                        mesh[i].setRotate(-90);
                    } else {
                        mesh[i].setRotationAxis(Rotate.Y_AXIS);
                        mesh[i].setRotate(90);
                    }
                }
                if (keyCode == KeyCode.Y) {
                    System.out.println(mesh[i].getTranslateX() + " " + mesh[i].getTranslateY() + " " + mesh[i].getTranslateZ());
                }
                if (keyCode == KeyCode.T) {
                   mesh1[i].setRotate(mesh1[i].getRotate()-1);
                }

            }
        });


        stage.setTitle("3D Вид");
        stage.setScene(scene);
        stage.show();
    }

    public Point3D unProjectDirection(double sceneX, double sceneY, double sWidth, double sHeight) {
        double tanHFov = Math.tan(Math.toRadians(camera.getFieldOfView()) * 0.5f);
        Point3D vMouse = new Point3D(tanHFov * (2 * sceneX / sWidth - 1), tanHFov * (2 * sceneY / sWidth - sHeight / sWidth), 1);

        Point3D result = localToSceneDirection(vMouse);
        return result.normalize();
    }

    public Point3D localToScene(Point3D pt) {
        Point3D res = camera.localToParentTransformProperty().get().transform(pt);
        if (camera.getParent() != null) {
            res = camera.getParent().localToSceneTransformProperty().get().transform(res);
        }
        return res;
    }

    public Point3D localToSceneDirection(Point3D dir) {
        Point3D res = localToScene(dir);
        return res.subtract(localToScene(new Point3D(0, 0, 0)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
