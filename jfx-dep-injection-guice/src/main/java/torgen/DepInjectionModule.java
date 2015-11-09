package torgen;


import com.google.inject.AbstractModule;

public class DepInjectionModule extends AbstractModule {
    @Override
    protected void configure() {
        // Defining the controllers as singletons.
        // We do not want these controllers to be instantiated more than one time.
        // This is mandatory for the dependency injection: if you comment the following line, two PickerController
        // instances will be created (one while loading the PickerView FXML file, another one on the @inject instruction
        // in the TextController Java file.
        //bind(TextController.class).asEagerSingleton();
        //bind(PickerController.class).asEagerSingleton();
        // These bindings are useless when the annotation @Singleton is added on the
        // TextController and  PickerController classes
    }
}
