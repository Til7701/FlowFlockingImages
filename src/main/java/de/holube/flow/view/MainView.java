package de.holube.flow.view;

import de.holube.flow.controller.MainController;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class MainView extends AnchorPane {

    @Getter
    private final MainController mainController;

    private final BorderPane borderPane;

    @Getter
    private final MenuBar menuBar;

    @Getter
    private final CanvasView canvasView;

    public MainView() {
        borderPane = new BorderPane();
        getChildren().add(borderPane);
        AnchorPane.setTopAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);
        AnchorPane.setBottomAnchor(borderPane, 0.0);

        menuBar = new MenuBar();
        borderPane.setTop(menuBar);

        canvasView = new CanvasView();
        borderPane.setCenter(canvasView);

        mainController = new MainController(this);
    }

}
