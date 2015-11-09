package torgen.view;

import com.google.inject.Singleton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

@Singleton
public class DrawingArea extends Pane {
    final private Rectangle rec;
    final private Ellipse ell;

    public DrawingArea() {
        super();
        System.out.println("DrawingArea created: " + this);

        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        rec = new Rectangle(2, 5, 15, 10);
        ell = new Ellipse(100, 150, 15, 10);

        getChildren().add(rec);
        getChildren().add(ell);
    }

    public Rectangle getRec() {
        return rec;
    }

    public Ellipse getEll() {
        return ell;
    }
}
