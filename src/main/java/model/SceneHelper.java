package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

/*
 A helper class for loading and placing a 3D object in the scene.
 Stores model path, color, size, and position.
 */
public class SceneHelper {

    private final String path;
    private final Color color;
    private final double scale;
    private final double x, y, z;

    public SceneHelper(String path, Color color, double scale, double x, double y, double z) {
        this.path = path;
        this.color = color;
        this.scale = scale;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //Builds the object and applies transforms
    public MeshView build() throws Exception {
        MeshView model = ModelLoader.createInstance(path, color, scale);
        model.setTranslateX(x);
        model.setTranslateY(y);
        model.setTranslateZ(z);
        model.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        return model;
    }

}
