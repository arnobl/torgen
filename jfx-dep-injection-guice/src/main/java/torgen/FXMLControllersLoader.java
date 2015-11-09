package torgen;

import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.util.BuilderFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class FXMLControllersLoader extends FXMLLoader {
    final Set<Object> controllers;

    public FXMLControllersLoader(final URL location, final ResourceBundle resources,
                                 final BuilderFactory builderFactory, final Injector injector) {
        super(location, resources, builderFactory);

        controllers = new HashSet<>();

        setControllerFactory(clazz ->  {
            final Object instance = injector.getInstance(clazz);
            if(instance!=null)
                controllers.add(instance);
            return instance;
        });
    }

    @Override
    public <T> T load() throws IOException {
        final T loaded = super.load();
        controllers.stream().filter(c -> c instanceof Configurable).forEach(c -> ((Configurable)c).configure());
        return loaded;
    }
}
