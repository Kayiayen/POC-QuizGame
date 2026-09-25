package quiz;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;

public final class Navigation {

    private static final Duration FADE_OUT = Duration.millis(220);
    private static final Duration FADE_IN  = Duration.millis(220);

    private Navigation() {}

    /** Load an FXML by path and fade to it. */
    public static void goTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Navigation.class.getResource(fxmlPath));
            goTo(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fade to an already-loaded root. Use this when you need to configure
     * the controller (e.g. pass difficulty) before the scene shows it.
     */
    public static void goTo(Parent next) {
        Scene scene = Main.getStage().getScene();

        // First-ever navigation: no current root to fade from.
        if (scene == null || scene.getRoot() == null) {
            Main.getStage().setScene(new Scene(next));
            return;
        }

        Parent current = scene.getRoot();

        // If a previous transition is still running, don't stack fades.
        current.setOpacity(1.0);

        FadeTransition fadeOut = new FadeTransition(FADE_OUT, current);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setInterpolator(Interpolator.EASE_BOTH);
        fadeOut.setOnFinished(e -> {
            next.setOpacity(0.0);
            scene.setRoot(next);

            FadeTransition fadeIn = new FadeTransition(FADE_IN, next);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.setInterpolator(Interpolator.EASE_BOTH);
            fadeIn.play();
        });
        fadeOut.play();
    }
}