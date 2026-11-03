package view;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Camera {

    private final PerspectiveCamera cam;

    //Camera rotation values
    private double yaw = 0;
    private double pitch = 0;

    //Transforms applied to the camera
    private final Rotate rotX = new Rotate(0, Rotate.X_AXIS);//pitch
    private final Rotate rotY = new Rotate(0, Rotate.Y_AXIS);//yaw
    public Translate trans = new Translate(0, 0, 0);

    public Camera(PerspectiveCamera cam) {
        this.cam = cam;
        cam.getTransforms().addAll(trans, rotY, rotX); //STACK ORDER IMPORTANT (first trans, then rot - remember guys)
        cam.setFieldOfView(65);
        cam.setNearClip(0.1);
        cam.setFarClip(2000);
    }

    public PerspectiveCamera getCamera() {
        return cam;
    }

    //Rotation control
    public void addYaw(double amount) {
        yaw += amount;
        rotY.setAngle(yaw);
    }

    public void addPitch(double amount) {
        pitch = Math.max(-89, Math.min(89, pitch + amount)); // prevent flipping
        rotX.setAngle(pitch);
    }

    //vector helpers
    public Point3D getForwardVector() {
        //world forward (Z negative)
        Point3D forward = new Point3D(0, 0, -1);

        //rotate by yaw and pitch
        forward = rotY.transform(forward);
        forward = rotX.transform(forward);

        return forward.normalize();
    }

    public Point3D getSideVector() {
        Point3D side = new Point3D(1, 0, 0);

        side = rotY.transform(side);
        side = rotX.transform(side);

        return side.normalize();
    }

    public Point3D getUpVector() {
        Point3D up = new Point3D(0, 1, 0);

        up = rotY.transform(up);
        up = rotX.transform(up);

        return up.normalize();
    }

}
