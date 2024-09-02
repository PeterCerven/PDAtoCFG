package com.example.bakalar.canvas.arrow;

import com.example.bakalar.logic.MainLogic;
import com.example.bakalar.logic.utility.ButtonState;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.example.bakalar.logic.MainLogic.NODE_RADIUS;

public class ArrowSymbolContainerHandler {
    private static final double SYMBOL_CONTAINER_SPACING = NODE_RADIUS / 5.0;
    private static final double VIEW_ORDER_SYMBOLS = 1;

    private final MainLogic mainLogic;
    @Getter
    private List<TransitionInputs> transitions;
    private VBox symbolsContainer;
    private Arrow arrow;

    public ArrowSymbolContainerHandler(MainLogic mainLogic, List<TransitionInputs> transitionInputs, VBox symbolsContainer, Arrow arrow) {
        this.symbolsContainer = symbolsContainer;
        this.arrow = arrow;
        this.mainLogic = mainLogic;
        this.transitions = new ArrayList<>();
        initializeSymbolContainers();
        for (TransitionInputs transitionInput : transitionInputs) {
            addSymbolContainer(transitionInput);
        }
    }

    private void initializeSymbolContainers() {
        VBox.setVgrow(symbolsContainer, Priority.ALWAYS);
        symbolsContainer.setAlignment(Pos.BOTTOM_CENTER);
        StackPane.setAlignment(symbolsContainer, Pos.BOTTOM_CENTER);
        symbolsContainer.setViewOrder(VIEW_ORDER_SYMBOLS);
    }

    public void addSymbolContainer(TransitionInputs transitionInputs) {
        HBox container = createSymbolContainer(transitionInputs);
        setupEventHandlers(container, transitionInputs);
        setupLayoutListener(container);
        this.transitions.add(transitionInputs);
        this.mainLogic.updateAllDescribePDA();
        symbolsContainer.getChildren().add(container);
    }

    private HBox createSymbolContainer(TransitionInputs transitionInputs) {
        HBox container = new HBox(SYMBOL_CONTAINER_SPACING);
        container.setAlignment(Pos.BOTTOM_CENTER);

        Text readSymbol = createSymbolText(transitionInputs.getRead());
        Text popSymbol = createSymbolText(transitionInputs.getPop());
        Text pushSymbol = createSymbolText(transitionInputs.getPush());

        container.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
        return container;
    }

    private Text createSymbolText(String text) {
        Text symbol = new Text(text);
        symbol.setFont(new Font("Arial", NODE_RADIUS / 2.0));
        return symbol;
    }

    private void setupEventHandlers(HBox container, TransitionInputs transitionInputs) {
        container.setOnMouseEntered(e -> {
            if (mainLogic.getBtnBeh().getCurrentState().equals(ButtonState.ERASE)) {
                container.setCursor(Cursor.HAND);
            }
        });

        container.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleMouseClick(event, container, transitionInputs));
    }

    private void handleMouseClick(MouseEvent event, HBox container, TransitionInputs transitionInputs) {
        if (event.getButton() == MouseButton.SECONDARY) {
            handleSecondaryClick(container, transitionInputs);
            event.consume();
        } else if (event.getButton() == MouseButton.PRIMARY && mainLogic.getBtnBeh().getCurrentState().equals(ButtonState.ERASE)) {
            handlePrimaryClick(container, transitionInputs);
        }
    }

    private void handleSecondaryClick(HBox container, TransitionInputs transitionInputs) {
        TransitionInputs newTransitionInputs = mainLogic.createArrowTransition(
                transitionInputs.getRead(),
                transitionInputs.getPop(),
                transitionInputs.getPush()
        );

        if (newTransitionInputs != null) {
            TransitionInputs transitionToChange = findTransition(transitionInputs);
            if (transitionToChange != null) {
                mainLogic.saveCurrentStateToHistory();
                transitionToChange.setRead(newTransitionInputs.getRead());
                transitionToChange.setPop(newTransitionInputs.getPop());
                transitionToChange.setPush(newTransitionInputs.getPush());
            }

            updateSymbolTexts(container, newTransitionInputs);
            mainLogic.updateAllDescribePDA();
        }
    }

    private void handlePrimaryClick(HBox container, TransitionInputs transitionInputs) {
        mainLogic.saveCurrentStateToHistory();
        transitions.remove(transitionInputs);
        symbolsContainer.getChildren().remove(container);

        if (symbolsContainer.getChildren().isEmpty()) {
            removeArrow();
        } else {
            arrow.updateStackPanePosition(symbolsContainer);
        }

        mainLogic.updateAllDescribePDA();
    }

    private void updateSymbolTexts(HBox container, TransitionInputs newTransitionInputs) {
        ((Text) container.getChildren().get(0)).setText(newTransitionInputs.getRead());
        ((Text) container.getChildren().get(1)).setText(newTransitionInputs.getPop());
        ((Text) container.getChildren().get(2)).setText(newTransitionInputs.getPush());
    }

    private void removeArrow() {
        mainLogic.getArrows().remove(arrow);
        arrow.getNodeFrom().removeArrow(arrow);
        arrow.getNodeTo().removeArrow(arrow);
        mainLogic.getMainPane().getChildren().remove(arrow);
    }

    private void setupLayoutListener(HBox container) {
        container.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            container.setLayoutX(newValue.getWidth());
            container.setLayoutY(newValue.getHeight());
            arrow.updateStackPanePosition(symbolsContainer);
        });
    }

    private TransitionInputs findTransition(TransitionInputs transitionInputs) {
        return transitions.stream()
                .filter(transition -> transition.equals(transitionInputs))
                .findFirst()
                .orElse(null);
    }
}
