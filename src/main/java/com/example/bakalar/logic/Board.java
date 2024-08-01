package com.example.bakalar.logic;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.arrow.TransitionInputs;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.StartNodeArrow;
import com.example.bakalar.exceptions.MyCustomException;
import com.example.bakalar.files.FileLogic;
import com.example.bakalar.logic.conversion.ConversionLogic;
import com.example.bakalar.logic.history.AppState;
import com.example.bakalar.logic.history.ArrowModel;
import com.example.bakalar.logic.history.HistoryLogic;
import com.example.bakalar.logic.history.NodeModel;
import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.ButtonBehaviour;
import com.example.bakalar.logic.utility.ButtonState;
import com.example.bakalar.logic.utility.DescribePDA;
import com.example.bakalar.logic.utility.LimitedTextField;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.bakalar.logic.utility.ButtonBehaviour.NODE_ICON_PATH;
import static com.example.bakalar.logic.utility.ErrorPopUp.showErrorDialog;
import static com.example.bakalar.logic.utility.StageUtils.setStageIcon;

@Getter
@Setter
public class Board implements Serializable {
    public static final String EPSILON = "ε";
    public static final String GAMMA_CAPITAL = "Γ";
    public static final String SIGMA = "Σ";
    public static final String DELTA_LOWER = "δ";
    public static final String STARTING_Z = "Z";
    public static final String STARTING_S = "S";
    private static final double DRAG_THRESHOLD = 20;
    public static int NODE_RADIUS = 35;
    private List<MyNode> nodes;
    private List<Arrow> arrows;
    private MyNode startNode;
    private AnchorPane mainPane;
    private StartNodeArrow startNodeArrow;
    private int nodeCounter;
    private CheckBox startingCheckBox;
    private CheckBox endingCheckBox;
    private double startX, startY;
    private DescribePDA describePDA;
    private HistoryLogic historyLogic;
    private FileLogic fileLogic;
    private Stage stage;
    private boolean dragging;
    private Long idCounter;
    private ButtonBehaviour btnBeh;
    private ConversionLogic conversionLogic;
    private boolean wasButtonPressed;
    private boolean canClick;
    private Slider slider;
    private TextField sliderInput;
    private boolean preventNodeCreation;


    public Board(AnchorPane mainPane, DescribePDA describePDA, HistoryLogic historyLogic,
                 Stage stage, ButtonBehaviour btnBeh, Slider slider, TextField sliderInput) {
        this.nodes = new ArrayList<>();
        this.describePDA = describePDA;
        this.arrows = new ArrayList<>();
        this.idCounter = 0L;
        this.mainPane = mainPane;
        this.startNodeArrow = new StartNodeArrow(0, 0, 0);
        this.addObject(startNodeArrow);
        this.startNodeArrow.setVisible(false);
        this.nodeCounter = 0;
        this.startingCheckBox = new CheckBox("Začiatočný stav");
        this.endingCheckBox = new CheckBox("Koncový stav");
        this.historyLogic = historyLogic;
        this.fileLogic = new FileLogic();
        this.stage = stage;
        this.dragging = false;
        this.btnBeh = btnBeh;
        this.conversionLogic = new ConversionLogic(fileLogic);
        this.canClick = true;
        this.slider = slider;
        this.sliderInput = sliderInput;
    }

    // file operations

    public void saveCurrentStateToFile() {
        AppState appState = createAppState();
        fileLogic.saveToJson(appState, stage);
    }

    public void loadStateFromFile() {
        AppState appState = fileLogic.loadFromJson(this.stage);
        if (appState != null) {
            createBoardFromAppState(appState);
        }
    }


    public void updateAllDescribePDA() {
        this.describePDA.updateAllDescribePDA(this.nodes, this.arrows, this.getNodesTransitions());
    }


    public List<Transition> getNodesTransitions() {
        List<Transition> transitions = new ArrayList<>();
        for (MyNode node : nodes) {
            for (Arrow arrow : node.getArrowsFrom()) {
                for (TransitionInputs transitionInputs : arrow.getTransitions()) {
                    transitions.add(new Transition(arrow.getFrom().getName(), transitionInputs.getRead(),
                            transitionInputs.getPop(), arrow.getTo().getName(), transitionInputs.getPush()));
                }
            }
        }
        return transitions;
    }


    // adding objects

    public void createArrow(MyNode from, MyNode to, TransitionInputs transitionInputs) throws MyCustomException {
        if (transitionInputs == null) {
            transitionInputs = createArrowTransition("", "", "");
        }
        if (transitionInputs == null) {
            return;
        }
        if (checkArrowForDuplicate(from, to, transitionInputs)) return;
        Arrow arrow = sameArrowExists(from, to);
        if (arrow != null) {
            boolean sameTransition = arrow.sameTransitionExists(transitionInputs);
            if (sameTransition) {
                throw new MyCustomException("Prechodová funkcia už existuje");
            }
            arrow.addSymbolContainer(transitionInputs);
            return;
        }
        List<TransitionInputs> transitions = List.of(transitionInputs);
        if (from == to) {
            arrow = new SelfLoopArrow(from, to, this, transitions);
        } else {
            if (oppositeExists(from, to)) {
                arrow = new LineArrow(from, to, 30, this, transitions);
            } else {
                arrow = new LineArrow(from, to, this, transitions);
            }
        }
        addArrowToBoard(from, to, arrow);
    }


    public void addObject(Node node) {
        if (node instanceof Arrow arrow) {
            arrows.add(arrow);
        } else if (node instanceof MyNode myNode) {
            char character = (char) (nodeCounter + '₀');
            String name = "q" + character;

            myNode.setName(name);
            nodeCounter++;
            nodes.add(myNode);

        }
        idCounter++;
        mainPane.getChildren().add(node);
        updateAllDescribePDA();
    }

    public MyNode createMyNode(double x, double y) {
        MyNode myNode = new MyNode(x, y, NODE_RADIUS);
        addNodeToBoard(myNode);
        return myNode;
    }

    private void createArrow(MyNode node) {
        if (btnBeh.getSelectedNode() != null) {
            saveCurrentStateToHistory();
            createMyArrow(btnBeh.getSelectedNode().getID(), node.getID(), null);
            this.selectNode(btnBeh.getSelectedNode(), false);
            btnBeh.setSelectedNode(null);

        } else {
            btnBeh.setSelectedNode(node);
            this.selectNode(node, true);
        }
    }

    public void createMyArrow(Long fromId, Long toId, TransitionInputs transitionInputs) {
        try {
            MyNode fromNode = findNodeById(fromId);
            MyNode toNode = findNodeById(toId);
            this.createArrow(fromNode, toNode, transitionInputs);
        } catch (MyCustomException e) {
            showErrorDialog(e.getMessage());
        }
    }

    // utility functions

    private void addArrowToBoard(MyNode from, MyNode to, Arrow arrow) {
        to.addArrow(arrow, "to");
        from.addArrow(arrow, "from");
        if (arrow instanceof LineArrow) this.makeCurveDraggable((LineArrow) arrow);
        this.addObject(arrow);
        this.makeArrowErasable(arrow);
    }

    private void addNodeToBoard(MyNode myNode) {
        this.makeDraggable(myNode);
        this.enableArrowCreation(myNode);
        this.addObject(myNode);
    }

    private Arrow sameArrowExists(MyNode from, MyNode to) {
        for (Arrow arrow : arrows) {
            if (arrow.getFrom() == from && arrow.getTo() == to) {
                return arrow;
            }
        }
        return null;
    }

    private boolean oppositeExists(MyNode from, MyNode to) {
        for (Arrow arrow : arrows) {
            if (arrow.getFrom() == to && arrow.getTo() == from) {
                return true;
            }
        }
        return false;
    }

    public void clearBoard(boolean resetButtons) {
        mainPane.getChildren().clear();
        nodes.clear();
        arrows.clear();
        nodeCounter = 0;
        startNode = null;
        updateAllDescribePDA();
        if (resetButtons) btnBeh.resetToSelect();
    }

    // starting and ending nodes

    public void setStarting(MyNode node, boolean starting) {
        if (startNode != null) {
            this.startNode.setStarting(false);
        }
        node.setStarting(starting);
        this.startNode = starting ? node : null;
    }

    private MyNode findNodeById(Long nodeId) throws MyCustomException {
        for (MyNode node : nodes) {
            if (node.getID().equals(nodeId)) {
                return node;
            }
        }
        throw new MyCustomException("Node not found");
    }

    public void setEnding(MyNode node, boolean ending) {
        node.setEnding(ending);

    }

    public void selectNode(MyNode node, boolean select) {
        node.setSelected(select);
    }

    // dialog windows

    public TransitionInputs createArrowTransition(String read, String pop, String push) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");

        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        setStageIcon(dialogStage);

        dialog.setTitle("Vytvorte prechodovú funkciu");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        LimitedTextField input1 = new LimitedTextField(read == null || read.isBlank() ? "" : read, 1);
        input1.setPromptText("(Read)");
        Label label1 = new Label("Čítaj symbol");
        label1.setLabelFor(input1);
        LimitedTextField input2 = new LimitedTextField(pop == null || pop.isBlank() ? "" : pop, 1);
        input2.setPromptText("(Pop)");
        Label label2 = new Label("Odstráň zo zásobníka");
        label2.setLabelFor(input2);
        LimitedTextField input3 = new LimitedTextField(push == null || push.isBlank() ? "" : push, 5);
        input3.setPromptText("(Push)");
        Label label3 = new Label("Daj na zásobník");
        label3.setLabelFor(input3);

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.add(input1, 0, 0);
        grid.add(label1, 1, 0);
        grid.add(input2, 0, 1);
        grid.add(label2, 1, 1);
        grid.add(input3, 0, 2);
        grid.add(label3, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(input1::requestFocus);

        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                String newRead = input1.getText().isBlank() ? EPSILON : input1.getText().trim();
                String newPush = input3.getText().isBlank() ? EPSILON : input3.getText().trim();
                String newPop = input2.getText();
                if (newPop.isBlank()) {
                    showErrorDialog("Políčko Pop nesmie byť prázdne");
                } else {
                    return new TransitionInputs(newRead, newPop, newPush);
                }
            } else {
                return null;
            }
        }
    }

    public void showDialog(MyNode node) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Zmeň stav");
        dialog.setHeaderText(null);

        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");

        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        setStageIcon(dialogStage);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(node.getName());

        gridPane.add(startingCheckBox, 0, 0);
        gridPane.add(endingCheckBox, 0, 1);
        gridPane.add(nameField, 0, 2);

        startingCheckBox.setSelected(node.isStarting());
        endingCheckBox.setSelected(node.isEnding());

        dialog.getDialogPane().setContent(gridPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                saveCurrentStateToHistory();
                node.setName(nameField.getText());
                if (node.isStarting() != startingCheckBox.isSelected()) {
                    setStarting(node, startingCheckBox.isSelected());
                }
                setEnding(node, endingCheckBox.isSelected());
            }
            updateAllDescribePDA();
        });
    }

    // event handlers

    public void makeCurveDraggable(LineArrow arrow) {
        arrow.getControlIndicator().setOnMousePressed(event -> {
            if (!btnBeh.getCurrentState().equals(ButtonState.SELECT) && !btnBeh.getCurrentState().equals(ButtonState.ERASE)
                    && event.getButton() == MouseButton.PRIMARY) {
                btnBeh.setPreviousState(btnBeh.getCurrentState());
                btnBeh.resetToSelect();
                wasButtonPressed = true;
            }
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - arrow.getControlIndicator().getTranslateX();
                    startY = event.getSceneY() - arrow.getControlIndicator().getTranslateY();
                }
            }
        });

        arrow.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                arrow.resetControlPoint();
                event.consume();
            }
        });

        arrow.getControlIndicator().setOnMouseDragged(e -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                if (!dragging) {
                    dragging = true;
                    arrow.getControlIndicator().cursorProperty().setValue(Cursor.MOVE);
                    this.saveCurrentStateToHistory();
                }
                arrow.moveControlPoint(e.getSceneX() - startX, e.getSceneY() - startY);
            }
        });

        arrow.getControlIndicator().setOnMouseReleased(e -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                arrow.getControlIndicator().cursorProperty().setValue(Cursor.HAND);
                dragging = false;
                if (wasButtonPressed) {
                    btnBeh.toggleButtonState(btnBeh.getPreviousState());
                    wasButtonPressed = false;
                }
            }
        });


    }

    public void createNode(MouseEvent event) {
        if (btnBeh.getCurrentState().equals(ButtonState.NODE) && event.getButton() == MouseButton.PRIMARY && !preventNodeCreation) {
            saveCurrentStateToHistory();
            createMyNode(event.getX(), event.getY());
        }
        preventNodeCreation = false;
    }

    public void makeDraggable(MyNode node) {
        node.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                this.showDialog(node);
            } else if (event.getButton() == MouseButton.PRIMARY) {
                startX = event.getSceneX() - node.getTranslateX();
                startY = event.getSceneY() - node.getTranslateY();
                dragging = false;
            }
        });

        node.setOnMouseDragged(e -> {
            if (!btnBeh.getCurrentState().equals(ButtonState.SELECT) && !btnBeh.getCurrentState().equals(ButtonState.ERASE)) {
                double diffX = Math.abs((e.getSceneX() - node.getTranslateX()) - startX);
                double diffY = Math.abs((e.getSceneY() - node.getTranslateY()) - startY);
                if (!dragging && (diffX > DRAG_THRESHOLD || diffY > DRAG_THRESHOLD)) {
                    btnBeh.setPreviousState(btnBeh.getCurrentState());
                    btnBeh.resetToSelect();
                    wasButtonPressed = true;
                }
            }
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                if (!dragging) {
                    dragging = true;
                    node.cursorProperty().setValue(Cursor.MOVE);
                    this.saveCurrentStateToHistory();
                }
                node.move(e.getSceneX() - startX, e.getSceneY() - startY);
                node.updateArrows(false);
            }
        });

        node.setOnMouseReleased(event -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                if (dragging) {
                    preventNodeCreation = true;
                }
                node.updateArrows(true);
                dragging = false;
                node.cursorProperty().setValue(Cursor.HAND);
                if (wasButtonPressed) {
                    btnBeh.toggleButtonState(btnBeh.getPreviousState());
                    wasButtonPressed = false;
                    canClick = false;
                }
            }
        });
    }

    public void enableArrowCreation(MyNode node) {
        node.setOnMouseClicked(event -> {
            if (btnBeh.getCurrentState().equals(ButtonState.ARROW) && event.getButton() == MouseButton.PRIMARY) {
                if (canClick) {
                    createArrow(node);
                }
                canClick = true;
            } else if (btnBeh.getCurrentState().equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                saveCurrentStateToHistory();
                node.erase(this);
                mainPane.getChildren().remove(node);
                updateAllDescribePDA();
            }

        });
    }

    public void makeArrowErasable(Arrow arrow) {
        if (arrow instanceof LineArrow lineArrow) {
            lineArrow.getControlIndicator().setOnMouseClicked(event -> {
                if (btnBeh.getCurrentState().equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                    saveCurrentStateToHistory();
                    lineArrow.erase(this);
                    mainPane.getChildren().remove(lineArrow);
                    updateAllDescribePDA();
                }
            });
        } else if (arrow instanceof SelfLoopArrow selfLoopArrow) {
            selfLoopArrow.getArc().setOnMouseClicked(event -> {
                if (btnBeh.getCurrentState().equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                    saveCurrentStateToHistory();
                    arrow.erase(this);
                    mainPane.getChildren().remove(arrow);
                    updateAllDescribePDA();
                }
            });
        }

    }


    // history

    public AppState createAppState() {
        AppState appState = new AppState();
        List<NodeModel> myNodeModels = nodes.stream().map(node -> {
            NodeModel myNodeModel = new NodeModel();
            myNodeModel.setNodeId(node.getID());
            myNodeModel.setX(node.getAbsoluteCentrePosX());
            myNodeModel.setY(node.getAbsoluteCentrePosY());
            myNodeModel.setName(node.getName());
            myNodeModel.setStarting(node.isStarting());
            myNodeModel.setEnding(node.isEnding());
            return myNodeModel;
        }).toList();

        List<ArrowModel> arrowModels = new ArrayList<>();
        for (Arrow arrow : arrows) {
            for (TransitionInputs inputs : arrow.getTransitions()) {
                ArrowModel arrowModel = new ArrowModel();
                arrowModel.setArrowId(arrow.getID());
                arrowModel.setFromNodeId(arrow.getFrom().getID());
                arrowModel.setToNodeId(arrow.getTo().getID());
                arrowModel.setTransition(inputs);
                arrowModels.add(arrowModel);
                if (arrow instanceof LineArrow lineArrow) {
                    arrowModel.setLineArrow(true);
                    arrowModel.setControlPointChangeX(lineArrow.getControlPointChangeX());
                    arrowModel.setControlPointChangeY(lineArrow.getControlPointChangeY());
                }
            }
        }

        appState.setNodeModels(myNodeModels);
        appState.setArrowModels(arrowModels);
        appState.setNodeRadius(NODE_RADIUS);
        return appState;
    }


    public void updateBoardSize(Integer value) {
        NODE_RADIUS = value;
        AppState appState = createAppState();
        createBoardFromAppState(appState);
    }

    public void saveCurrentStateToHistory() {
        historyLogic.saveCurrentState(arrows, nodes);
    }

    public void redo() {
        AppState appState = historyLogic.redo(arrows, nodes);
        createBoardFromAppState(appState);
    }

    public void undo() {
        AppState appState = historyLogic.undo(arrows, nodes);
        createBoardFromAppState(appState);
    }

    public void createBoardFromAppState(AppState appState) {
        try {
            clearBoard(false);
            NODE_RADIUS = appState.getNodeRadius();
            sliderInput.setText(NODE_RADIUS + "");
            slider.setValue(NODE_RADIUS);
            for (NodeModel myNodeModel : appState.getNodeModels()) {
                createMyNodeFromHistory(myNodeModel.getName(), myNodeModel.getX(), myNodeModel.getY(),
                        myNodeModel.isStarting(), myNodeModel.isEnding(), myNodeModel.getNodeId());
            }
            for (ArrowModel arrowModel : appState.getArrowModels()) {
                MyNode from = findNodeById(arrowModel.getFromNodeId());
                MyNode to = findNodeById(arrowModel.getToNodeId());
                if (arrowModel.isLineArrow()) {
                    createLineArrowFromHistory(from, to, arrowModel.getTransition(), arrowModel.getControlPointChangeX(), arrowModel.getControlPointChangeY());
                } else {
                    createSelfLoopArrowFromHistory(from, to, arrowModel.getTransition());
                }
            }
        } catch (MyCustomException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void createSelfLoopArrowFromHistory(MyNode from, MyNode to, TransitionInputs transitionInputs) throws MyCustomException {
        if (checkArrowForDuplicate(from, to, transitionInputs)) return;
        Arrow arrow = new SelfLoopArrow(from, to, this, List.of(transitionInputs));
        addArrowToBoard(from, to, arrow);
    }

    private void createLineArrowFromHistory(MyNode from, MyNode to, TransitionInputs transitionInputs, Double controlPointChangeX, Double controlPointChangeY) throws MyCustomException {
        if (checkArrowForDuplicate(from, to, transitionInputs)) return;
        Arrow arrow = new LineArrow(from, to, controlPointChangeX, controlPointChangeY, this, List.of(transitionInputs));
        addArrowToBoard(from, to, arrow);
    }

    private boolean checkArrowForDuplicate(MyNode from, MyNode to, TransitionInputs transitionInputs) throws MyCustomException {
        Arrow arrow = sameArrowExists(from, to);
        if (arrow != null) {
            boolean sameTransition = arrow.sameTransitionExists(transitionInputs);
            if (sameTransition) {
                throw new MyCustomException("Prechodová funkcia už existuje");
            }
            arrow.addSymbolContainer(transitionInputs);
            return true;
        }
        return false;
    }

    public void createMyNodeFromHistory(String name, double x, double y, boolean starting, boolean ending, Long ID) {
        MyNode myNode = new MyNode(name, x, y, starting, ending, ID);
        if (starting) {
            setStarting(myNode, true);
        }
        addNodeToBoard(myNode);
    }


    // exit

    public void showExitConfirmation(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);


        alert.setTitle("Potvrdenie");
        alert.setHeaderText("Naozaj chcete ukončiť aplikáciu?");
        alert.setContentText(null);


        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(NODE_ICON_PATH))));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                stage.close();
            }
        });
    }

    // conversion

    public void convertPDA() {
        try {
            conversionLogic.convertPDA(getNodesTransitions(), nodes, startNode);
        } catch (MyCustomException e) {
            showErrorDialog(e.getMessage());
        }
    }

    // testing

    public void testBoard() {
        saveCurrentStateToHistory();
        clearBoard(true);
        MyNode firstNode = createMyNode(120, 150);
        setStarting(firstNode, true);
        MyNode secondNode = createMyNode(320, 150);
        setEnding(firstNode, true);

        createMyArrow(firstNode.getID(), firstNode.getID(), new TransitionInputs("1", "Z", "XZ"));
        createMyArrow(firstNode.getID(), firstNode.getID(), new TransitionInputs("1", "X", "XX"));
        createMyArrow(firstNode.getID(), firstNode.getID(), new TransitionInputs(EPSILON, "X", EPSILON));
        createMyArrow(firstNode.getID(), secondNode.getID(), new TransitionInputs("0", "X", "X"));
        createMyArrow(secondNode.getID(), secondNode.getID(), new TransitionInputs("1", "X", EPSILON));
        createMyArrow(secondNode.getID(), firstNode.getID(), new TransitionInputs("0", "Z", "Z"));
    }

}
