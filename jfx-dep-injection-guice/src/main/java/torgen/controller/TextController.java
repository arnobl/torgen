package torgen.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import torgen.Configurable;

import java.net.URL;
import java.util.ResourceBundle;

@Singleton
public class TextController implements Initializable, Configurable {
    @FXML TextField text1;
    @FXML TextField text2;

    @Inject PickerController picker;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        // One may note that the FXML annotation is also a dependency injection:
        // the widgets instantiated during the loading of the FXML view are injected in the fields annotated with
        // @FXML in the controller of this view. The type and the name of these attributes must match the type and the
        // id of the FXML tag.
        System.out.println(text1);
        System.out.println(text2);
        // Similarly, the following controller has been injected.
        System.out.println("Injected controller: " + picker);

        // You should not access the @FXML attributes of the injected controller PickerController here.
        // Indeed, this last is instantiated here, but its FXML attributes may not being initialised if ButtonView is
        // loaded before PickerView by the FXML factory.
        System.out.println("PickerController widgets: " + picker.picker1 + " " + picker.picker2);
    }

    @Override
    public void configure() {
        System.out.println("PickerController widgets in configure: " + picker.picker1 + " " + picker.picker2);

        text1.textProperty().bind(StringBinding.stringExpression(picker.picker1.valueProperty()));
        text2.textProperty().bind(StringBinding.stringExpression(picker.picker2.valueProperty()));

        text1.backgroundProperty().bind(Bindings.createObjectBinding(() -> new Background(new BackgroundFill(picker.picker1.getValue(), null, null)), picker.picker1.valueProperty()));
        text2.backgroundProperty().bind(Bindings.createObjectBinding(() -> new Background(new BackgroundFill(picker.picker2.getValue(), null, null)), picker.picker2.valueProperty()));
    }
}

