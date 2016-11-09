package torgen;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import torgen.utils.FxImageComparison;
import torgen.utils.FxRobotColourPicker;
import torgen.utils.FxRobotListSelection;
import torgen.utils.FxRobotSpinner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/* A TestFX test class must extends ApplicationTest. The interfaces used by the test class are robots that
* can interact with some widgets not supported by TestFX yet. */
public class TestSimpleController extends ApplicationTest implements FxRobotColourPicker, FxRobotSpinner,
        FxRobotListSelection, FxImageComparison {
    /* The widgets of the GUI used for the tests. */
    ColorPicker picker;
    TextField text;
    Button button;
    Spinner<Integer> spinner;
    ComboBox<Color> combobox;
    Parent mainNode;

    /* This operation comes from ApplicationTest and loads the GUI to test. */
    @Override
    public void start(Stage stage) throws Exception {
        mainNode = FXMLLoader.load(SimpleJFXApp.class.getResource("/fxml/torgen/ui/UI.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        /* Do not forget to put the GUI in front of windows. Otherwise, the robots may interact with another
        window, the one in front of all the windows... */
        stage.toFront();
    }

    /* Just a shortcut to retrieve widgets in the GUI. */
    public <T extends Node> T find(final String query) {
        /* TestFX provides many operations to retrieve elements from the loaded GUI. */
        return lookup(query).queryFirst();
    }

    @Before
    public void setUp() {
        /* Just retrieving the tested widgets from the GUI. */
        picker = find("#picker");
        text = find("#text");
        button = find("#button");
        spinner = find("#spinner");
        combobox = find("#combobox");
    }

    /* IMO, it is quite recommended to clear the ongoing events, in case of. */
    @After
    public void tearDown() throws TimeoutException {
        /* Close the window. It will be re-opened at the next test. */
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
        /* pickColour is not supported by TestFX yet (as far as I know). The trait FxRobotColourPicker implements
        such a robot. */
        pickColour(picker);
        /* The following instruction is mandatory to wait for the end of the user interactions before running
        the assertions. */
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The binding between the colour picker and the text of the text field does not work",
                picker.getValue().toString(), text.getText());
    }

    @Test
    public void testSetTextOfSpinnerChangesColourPicker() {
        clickOn(spinner).type(KeyCode.END).type(KeyCode.BACK_SPACE, 3).write("127").type(KeyCode.ENTER);
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
        clickOn(text).type(KeyCode.END).type(KeyCode.BACK_SPACE, 10).write("0x98fb99ff").type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The binding between the text of the text field and the selected colour of the color picker does not work",
                text.getText(), picker.getValue().toString());
    }


    @Test
    public void testSetIncorrectTextOfSpinnerChangesColourPickerToWhite() {
        clickOn(text).type(KeyCode.END).type(KeyCode.BACK_SPACE, 10).type(KeyCode.NUMPAD0).type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(Color.WHITE, picker.getValue());
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
        /* Changing a value of a widget may modifies its rendering, so this instruction must be executed in the
        JavaFX thread. */
        Platform.runLater(() -> picker.setValue(new Color(0.1, 0.1, 0.1, 0.5)));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(127, spinner.getValue().intValue());
    }

    @Test
    public void testSelectComboBoxItemChangesColour() {
        selectNextComboBoxItem(combobox);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(combobox.getValue(), picker.getValue());
    }

    @Test
    public void testGUIRendering() throws IOException {
        assertSnapshotsEqual(getClass().getResource("/test/snapshots/snapshotGUI.png").getFile(), mainNode, 0d);
    }
}
