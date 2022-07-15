package de.holube.flow.util.fx;

import javafx.application.Platform;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.CountDownLatch;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlatformExt {

    public static void runAndWait(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            try {
                action.run();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            final CountDownLatch doneLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    action.run();
                } finally {
                    doneLatch.countDown();
                }
            });

            try {
                doneLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

}
