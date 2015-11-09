package torgen.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import torgen.view.DrawingArea;

import java.net.URL;
import java.util.ResourceBundle;

@Singleton
public class PickerController implements Initializable {
    @FXML ColorPicker picker1;
    @FXML ColorPicker picker2;

    @Inject DrawingArea drawingArea;

    public PickerController() {
        // Not mandatory. Just printing a message to see that this class is instantiated a single time since
        // we defined this class as a singleton in the Guice injection module.
        super();
        System.out.println("Creating a PickerController: " + this);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        picker1.setValue(Color.LIGHTCORAL);
        picker2.setValue(Color.LIGHTGRAY);

        drawingArea.getRec().fillProperty().bind(picker1.valueProperty());
        drawingArea.getEll().fillProperty().bind(picker2.valueProperty());
    }
}

