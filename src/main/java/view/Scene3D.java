package view;

import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import model.SceneHelper;

import java.util.ArrayList;
import java.util.List;

public class Scene3D {

    private final SubScene subScene;
    private final Group root3D;
    private final view.Camera camWrapper;

    public Scene3D(double width, double height) {
        root3D = new Group();
        subScene = new SubScene(root3D, width, height, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTBLUE);

        //Setup perspective camera
        PerspectiveCamera cam = new PerspectiveCamera(true);
        camWrapper = new view.Camera(cam);
        subScene.setCamera(cam);

        createWorld();
        loadModels();
    }

    //Creates floor and world objects
    private void createWorld() {

        //lighting
        AmbientLight ambient = new AmbientLight(Color.color(0.40, 0.40, 0.40));
        DirectionalLight light = new DirectionalLight(Color.color(0.85, 0.78, 0.72));
        light.setDirection(new Point3D(0, 1, 0).normalize());

        //floor
        Box floor = new Box(1500, 1, 1500);
        floor.setTranslateY(20);
        floor.setMaterial(new PhongMaterial(Color.rgb(113,200,19)));

        root3D.getChildren().addAll(floor, ambient, light);
    }

    //load models from obj files
    private void loadModels() {
        try {
            List<SceneHelper> objects = new ArrayList<>();

            //rocks
            for (int i = 0; i < 89; i++) {
                objects.add(new SceneHelper("/assets/models/Rock1.obj", Color.GREY, 20, Math.random() * 1500 - 750, 15, Math.random() * 1500 - 750));
                objects.add(new SceneHelper("/assets/models/Rock2.obj", Color.DARKGREY, 15, Math.random() * 1500 - 750, 16, Math.random() * 1500 - 750));
            }
            //tree
            for (int i = 0; i < 159; i++) {
                objects.add(new SceneHelper("/assets/models/Tree2.obj", Color.DARKGREEN, 20, Math.random() * 1500 - 750, -30, Math.random() * 1500 - 750));
            }
            //grass
            for (int i = 0; i < 2000; i++) {
                objects.add(new SceneHelper("/assets/models/Grass.obj", Color.rgb(113,200,19), 15, Math.random() * 1500 - 750, 17, Math.random() * 1500 - 750));
            }
            //clouds
            for (int i = 0; i < 29; i++) {
                objects.add(new SceneHelper("/assets/models/Cloud.obj", Color.WHITE, 15, Math.random() * 1500 - 750, -150, Math.random() * 1500 - 750));
            }
            //mountains
            objects.add(new SceneHelper("/assets/models/Mountain.obj", Color.GREY, 40, 800, -60, 800));
            objects.add(new SceneHelper("/assets/models/Mountain.obj", Color.GREY, 40, -800, -60, -800));
            objects.add(new SceneHelper("/assets/models/Mountain.obj", Color.GREY, 40, 800, -60, -800));
            objects.add(new SceneHelper("/assets/models/Mountain.obj", Color.GREY, 40, -800, -60, 800));

            //loop through all objects and add them to the scene
            for (SceneHelper obj : objects) {
                MeshView mesh = obj.build();
                mesh.setScaleY(-mesh.getScaleY()); //fix flipping
                root3D.getChildren().add(mesh);
            }

            System.out.println("Loaded " + objects.size() + " scene objects.");

        } catch (Exception e) {
            System.err.println("Failed to load scene models: " + e.getMessage());
        }
    }

    //Getters
    public SubScene getSubScene() {
        return subScene; }
    public Camera getCamera3D() {
        return camWrapper; }
}
