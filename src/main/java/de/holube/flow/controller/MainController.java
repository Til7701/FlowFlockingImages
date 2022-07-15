package de.holube.flow.controller;

import de.holube.flow.simulation.Simulation;
import de.holube.flow.util.fx.PlatformExt;
import de.holube.flow.view.MainView;

public class MainController {

    private final MainView mainView;

    private final CanvasController canvasController;

    public MainController(MainView mainView) {
        this.mainView = mainView;

        this.canvasController = mainView.getCanvasView().getCanvasController();
    }

    public void update(Simulation simulation) {
        PlatformExt.runAndWait(() -> canvasController.update(simulation));
    }

}