package torgen.controller;

import com.google.inject.Singleton;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

@Singleton
public class SimpleController implements Initializable {
    @FXML ColorPicker picker;
    @FXML TextField text;
    @FXML Button button;
    @FXML Spinner<Double> spinner;
    @FXML ComboBox<Color> combobox;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        Bindings.bindBidirectional(text.textProperty(), picker.valueProperty(), new StringConverter<Color>() {
            @Override
            public String toString(final Color col) {
                return col.toString();
            }

            @Override
            public Color fromString(final String txt) {
                try {
                    return Color.valueOf(txt);
                }catch(IllegalArgumentException ex) {
                    return Color.WHITE;
                }
            }
        });
        text.backgroundProperty().bind(Bindings.createObjectBinding(() -> new Background(new BackgroundFill(picker.getValue(), null, null)), picker.valueProperty()));

        button.setOnAction(evt -> picker.setValue(picker.getValue().darker()));

        picker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue observable, Color oldValue, Color newValue) {
                spinner.getValueFactory().setValue(newValue.getOpacity() * 255d);
            }
        });

        spinner.getValueFactory().valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue observable, Double oldValue, Double newValue) {
                Color col = picker.valueProperty().getValue();
                Color col2 = new Color(col.getRed(), col.getGreen(), col.getBlue(), newValue/255d);
                picker.valueProperty().setValue(col2);
            }
        });

        picker.setValue(Color.LIGHTCORAL);

        combobox.setItems(FXCollections.observableArrayList(Color.ALICEBLUE, Color.BROWN, Color.BURLYWOOD, Color.AQUAMARINE));

        combobox.setOnAction(evt -> picker.setValue(combobox.getValue()));
    }
}

