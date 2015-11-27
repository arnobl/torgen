package torgen.utils;

import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import org.testfx.api.FxRobotInterface;

public interface FxRobotColourPicker extends FxRobotInterface {
    /**
     * Selects a colour of the palette of the colour picker. The same colour is always selected.
     * @param picker The picker to use.
     */
    default void pickColour(final ColorPicker picker) {
        clickOn(picker).type(KeyCode.TAB).type(KeyCode.TAB).type(KeyCode.ENTER);
    }
}
