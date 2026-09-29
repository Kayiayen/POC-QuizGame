package quiz;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public final class Settings {

    private Settings() {}

    public static final BooleanProperty musicOn = new SimpleBooleanProperty(true);
}