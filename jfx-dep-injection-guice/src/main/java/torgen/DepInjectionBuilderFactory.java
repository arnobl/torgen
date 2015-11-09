package torgen;

import com.google.inject.Injector;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import torgen.view.DrawingArea;

public class DepInjectionBuilderFactory implements BuilderFactory {
    private final Injector injector;
    private final BuilderFactory defaultFactory;


    public DepInjectionBuilderFactory(final Injector inj) {
        super();
        injector = inj;
        defaultFactory = new JavaFXBuilderFactory();
    }

    @Override
    public Builder<?> getBuilder(final Class<?> type) {
        if(type==DrawingArea.class)
            return () -> injector.getInstance(type);
        return defaultFactory.getBuilder(type);
    }
}
