package torgen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleJFXApp extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Parent view = FXMLLoader.load(getClass().getResource("/fxml/torgen/ui/UI.fxml"));
        final Scene scene = new Scene(view);

        primaryStage.setTitle("TextFX demo");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}
