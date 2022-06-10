package de.holube.flow.fx.util;

import javafx.application.Platform;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.Semaphore;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlatformExt {

    public static void runAndWait(Runnable action) {
        if (action == null) {
            throw new NullPointerException("action");
        }

        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        final Semaphore lock = new Semaphore(0);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                lock.release();
            }
        });
        lock.acquireUninterruptibly();
    }

}
