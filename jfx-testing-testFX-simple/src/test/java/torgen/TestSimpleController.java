package torgen;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import torgen.utils.FxRobotColourPicker;
import torgen.utils.FxRobotListSelection;
import torgen.utils.FxRobotSpinner;

import static org.junit.Assert.*;

import java.util.concurrent.TimeoutException;

public class TestSimpleController extends ApplicationTest implements FxRobotColourPicker, FxRobotSpinner, FxRobotListSelection {
    ColorPicker picker;
    TextField text;
    Button button;
    Spinner<Double> spinner;
    ComboBox<Color> combobox;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(SimpleJFXApp.class.getResource("/torgen/ui/UI.fxml"))));
        stage.show();
        stage.toFront();
    }

    public <T extends Node> T find(final String query) {
        return lookup(query).queryFirst();
    }

    @Before
    public void setUp() {
        picker = find("#picker");
        text = find("#text");
        button = find("#button");
        spinner = find("#spinner");
        combobox = find("#combobox");
    }

    @After
    public void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Test
    public void testWidgetsExist() {
        final String errMsg = "One of the widget cannot be retrieved anymore";
        assertNotNull(errMsg, picker);
        assertNotNull(errMsg, text);
        assertNotNull(errMsg, button);
        assertNotNull(errMsg, spinner);
        assertNotNull(errMsg, combobox);
    }

    @Test
    public void testTextofTextFieldBoundToColourPicker() {
        pickColour(picker);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The binding between the colour picker and the text of the text field does not work",
                picker.getValue().toString(), text.getText());
    }

    @Test
    public void testSetTextOfSpinnerChangesColourPicker() {
        clickOn(spinner);
        clickOn(spinner).type(KeyCode.END).type(KeyCode.BACK_SPACE, 3).type(KeyCode.NUMPAD1).type(KeyCode.NUMPAD2).
                type(KeyCode.NUMPAD7).type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(127d, picker.getValue().getOpacity()*255d, 0.001);
    }

    @Test
    public void testBackgroundofTextFieldBoundToColourPicker() {
        pickColour(picker);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The binding between the colour picker and the background colour of the text field does not work",
                picker.getValue(), text.getBackground().getFills().get(0).getFill());
    }

    @Test
    public void testColourPickerBoundToTextFieldText() {
        clickOn(text).type(KeyCode.END).type(KeyCode.BACK_SPACE, 10).type(KeyCode.NUMPAD0).type(KeyCode.X).
                type(KeyCode.NUMPAD9).type(KeyCode.NUMPAD8).type(KeyCode.F).type(KeyCode.B).type(KeyCode.NUMPAD9).
                type(KeyCode.NUMPAD9).type(KeyCode.F).type(KeyCode.F).type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The binding between the text of the text field and the selected colour of the color picker does not work",
                text.getText(), picker.getValue().toString());
    }

    @Test
    public void testClickOnButtonSetDarkerColour() {
        Color darker = picker.getValue().darker();
        clickOn(button);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(darker, picker.getValue());
    }

    @Test
    public void testUseSpinnerToChangeTheOpacity() {
        final double opacity = picker.getValue().getOpacity()*255d;
        decrementSpinner(spinner);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(opacity-1d, picker.getValue().getOpacity()*255d, 0.000001);
    }

    @Test
    public void testChangeColourSetSpinnersValue() {
        Platform.runLater(() -> picker.setValue(new Color(0.1, 0.1, 0.1, 0.5)));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(0.5*255d, spinner.getValue(), 0.001);
    }

    @Test
    public void testSelectComboBoxItemChangesColour() {
        selectNextComboBoxItem(combobox);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(combobox.getValue(), picker.getValue());
    }
}
