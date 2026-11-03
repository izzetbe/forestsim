package controller;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import view.Scene3D;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

/*
 Main entry point for the ForestSim project.
 This class starts the FXGL app, sets up basic settings,
 and initializes the 3D scene and player controls.
 */
public class Launcher extends GameApplication {

    private Scene3D scene3D;
    private Controller controller;

    @Override
    public void initSettings(GameSettings settings) {
        //Setup the game window
        settings.setTitle("Graphics Project");
        settings.setVersion("0.1");
        settings.setWidth(1300);
        settings.setHeight(750);

    }

    @Override
    public void initGame() {
        //Create the 3D scene (world, camera, lighting, etc.)
        scene3D = new Scene3D(getAppWidth(), getAppHeight());
        getGameScene().addUINode(scene3D.getSubScene());

        //Add controls for moving the player cube
        controller = new Controller(getInput(), scene3D.getCamera3D());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
