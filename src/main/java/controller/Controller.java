package controller;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;
import view.Camera;

public class Controller {

    private final Camera camera;
    private double lastX;
    private double lastY;
    private boolean rotating = false;
    private final double MOUSE_SENSITIVITY = 0.125;

    private Robot robot;

    public Controller(Input input, Camera camera) {
        this.camera = camera;

        Platform.runLater(() -> {
            robot = new Robot();
            initMouseLook();
        });

        initControls(input);
    }

    //WASD controls
    private void initControls(Input input) {
        input.addAction(new UserAction("Move Forward") {
            @Override protected void onAction() { moveCameraZ(-1); }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Backward") {
            @Override protected void onAction() { moveCameraZ(1); }
        }, KeyCode.S);

        input.addAction(new UserAction("Move Left") {
            @Override protected void onAction() { moveCameraX(-1); }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Right") {
            @Override protected void onAction() { moveCameraX(1); }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Up") {
            @Override protected void onAction() { moveCameraY(-1); }
        }, KeyCode.SPACE);

        input.addAction(new UserAction("Move Down") {
            @Override protected void onAction() { moveCameraY(1); }
        }, KeyCode.C);
    }

    //Mouse look
    private void initMouseLook() {
        var stage = FXGL.getPrimaryStage();
        var scene = stage.getScene();

        scene.setCursor(Cursor.DEFAULT);

        //Initalize rotation when the right mouse button is held down
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {

                rotating = true;
                lastX = event.getSceneX();
                lastY = event.getSceneY();
            }
        });

        //Rotate only while the right button is held down
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

            if (!event.isSecondaryButtonDown())
                return; //not holding right mouse

            if (!rotating)
                return;

            double currentX = event.getSceneX();
            double currentY = event.getSceneY();

            double dx = currentX - lastX;
            double dy = currentY - lastY;

            lastX = currentX;
            lastY = currentY;

            camera.addYaw(dx * MOUSE_SENSITIVITY);
            camera.addPitch(-dy * MOUSE_SENSITIVITY);
        });

        //When the right mouse is released, stop rotating and recenter cursor
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (event.getButton() != MouseButton.SECONDARY)
                return;

            rotating = false;

            double centerX = stage.getX() + scene.getWidth() / 2;
            double centerY = stage.getY() + scene.getHeight() / 2;

            robot.mouseMove(centerX, centerY);
        });
    }

    //axis camera movement
    private void moveCameraZ(double distance) {
        Point3D fwd = camera.getForwardVector();
        camera.trans.setX(camera.trans.getX() + fwd.getX() * distance);
        camera.trans.setZ(camera.trans.getZ() + fwd.getZ() * distance);
    }

    private void moveCameraX(double distance) {
        Point3D right = camera.getSideVector();
        camera.trans.setX(camera.trans.getX() + right.getX() * distance);
        camera.trans.setZ(camera.trans.getZ() + right.getZ() * distance);
    }

    private void moveCameraY(double distance) {
        Point3D up = camera.getUpVector();
        camera.trans.setY(camera.trans.getY() + up.getY() * distance);
    }
}

