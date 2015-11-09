package torgen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DepInjectionApp extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        // Creation of the dependencies injector
        final Injector injector = Guice.createInjector(new DepInjectionModule());
//        final Callback<Class<?>, Object> guiceFactory = clazz -> injector.getInstance(clazz);

        final FXMLLoader loader = new FXMLControllersLoader(// Loading the view
                getClass().getResource("/torgen/ui/UI.fxml"),
                // The resource bundle, useful to internationalised apps. Null here.
                null,
                // The builder used to instantiate the view
                new DepInjectionBuilderFactory(injector),
                // The controller factory that will be a Guice factory:
                // this Guice factory will manage the instantiation of the controllers and their dependency injections.
                injector);
        final Parent view = loader.load();

//        final Parent view = FXMLLoader.load(
//                // Loading the view
//                getClass().getResource("/torgen/ui/UI.fxml"),
//                // The resource bundle, useful to internationalised apps. Null here.
//                null,
//                // The builder used to instantiate the view
//                new JavaFXBuilderFactory(),
//                // The controller factory that will be a Guice factory:
//                // this Guice factory will manage the instantiation of the controllers and their dependency injections.
//                guiceFactory);

        // Creation of a scene from the loaded view
        final Scene scene = new Scene(view);

        primaryStage.setTitle("Dependency injection demo");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}
