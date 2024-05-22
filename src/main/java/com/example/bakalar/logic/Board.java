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
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

import static com.example.bakalar.logic.MainController.NODE_RADIUS;
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
    private List<MyNode> nodes;
    private List<Arrow> arrows;
    private MyNode startNode;
    private AnchorPane mainPane;
    private StartNodeArrow startNodeArrow;
    private int nodeCounter;
    private CheckBox startingCheckBox;
    private CheckBox endingCheckBox;
    private double startX, startY;
    private MyNode selectedNode;
    private DescribePDA describePDA;
    private HistoryLogic historyLogic;
    private FileLogic fileLogic;
    private Stage stage;
    private boolean dragging;
    private Long idCounter;
    private ButtonBehaviour btnBeh;
    private ConversionLogic conversionLogic;


    public Board(AnchorPane mainPane, DescribePDA describePDA, HistoryLogic historyLogic,
                 Stage stage, ButtonBehaviour btnBeh) {
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
        this.conversionLogic = new ConversionLogic();
    }

    // file operations

    public void saveCurrentStateToFile() {
        fileLogic.saveToJson(nodes, arrows, nodeCounter, idCounter, stage);
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


    // adding and removing objects

    public void removeObject(Node node) {
        if (node instanceof Arrow arrow) {
            arrows.remove(arrow);
            arrow.getFrom().removeArrow(arrow);
            arrow.getTo().removeArrow(arrow);
        } else if (node instanceof MyNode myNode) {
            Iterator<Arrow> iterator = myNode.getAllArrows().iterator();
            while (iterator.hasNext()) {
                Arrow arrow1 = iterator.next();
                if (arrow1.getFrom() == myNode) {
                    arrow1.getTo().removeArrow(arrow1);
                } else {
                    arrow1.getFrom().removeArrow(arrow1);
                }
                arrows.remove(arrow1);
                mainPane.getChildren().remove(arrow1);
                iterator.remove();
            }
            nodes.remove(myNode);
        }
        mainPane.getChildren().remove(node);
        updateAllDescribePDA();
    }

    public Arrow createArrow(MyNode from, MyNode to, TransitionInputs transitionInputs) throws MyCustomException {
        if (transitionInputs == null) {
            transitionInputs = createArrowTransition("", "", "");
        }
        if (transitionInputs == null) {
            return null;
        }
        Arrow arrow = sameArrowExists(from, to);
        if (arrow != null) {
            boolean sameTransition = arrow.sameTransitionExists(transitionInputs);
            if (sameTransition) {
                throw new MyCustomException("Prechodová funkcia už existuje");
            }
            arrow.addSymbolContainer(transitionInputs);
            return arrow;
        }
        List<TransitionInputs> transitions = List.of(transitionInputs);
        if (from == to) {
            arrow = new SelfLoopArrow(from, to, this, transitions, this.idCounter);
        } else {
            if (oppositeExists(from, to)) {
                arrow = new LineArrow(from, to, 30, this, transitions, this.idCounter);
            } else {
                arrow = new LineArrow(from, to, this, transitions, this.idCounter);
            }
        }
        to.addArrow(arrow, "to");
        from.addArrow(arrow, "from");
        this.addObject(arrow);
        return arrow;
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
        MyNode myNode = new MyNode(x, y, NODE_RADIUS, idCounter);
        this.makeDraggable(myNode);
        this.enableArrowCreation(myNode);
        this.cursorChange(myNode);
        this.addObject(myNode);
        return myNode;
    }

    private void createArrow(MyNode node) {
        if (selectedNode != null) {
            saveCurrentStateToHistory();
            createMyArrow(selectedNode.getNodeId(), node.getNodeId(), null);
            this.selectNode(selectedNode, false);
            selectedNode = null;

        } else {
            selectedNode = node;
            this.selectNode(node, true);
        }
    }

    public void createMyArrow(Long fromId, Long toId, TransitionInputs transitionInputs) {
        try {
            MyNode fromNode = findNodeById(fromId);
            MyNode toNode = findNodeById(toId);
            Arrow arrow = this.createArrow(fromNode, toNode, transitionInputs);
            if (arrow != null) {
                this.makeErasable(arrow);
                if (arrow instanceof LineArrow lineArrow) {
                    this.makeCurveDraggable(lineArrow);
                }
            }
        } catch (MyCustomException e) {
            showErrorDialog(e.getMessage());
        }
    }

    // utility functions

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

    public void clearBoard() {
        mainPane.getChildren().clear();
        nodes.clear();
        arrows.clear();
        nodeCounter = 0;
        startNode = null;
        updateAllDescribePDA();
        setSelectedNode(null);
        btnBeh.resetToSelect();
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
            if (node.getNodeId().equals(nodeId)) {
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

    // event handlers

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

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String newRead = input1.getText().isBlank() ? EPSILON : input1.getText().trim();
            String newPop = input2.getText().isBlank() ? EPSILON : input2.getText().trim();
            String newPush = input3.getText().isBlank() ? EPSILON : input3.getText().trim();

            return new TransitionInputs(newRead, newPop, newPush);
        }
        return null;
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

    public void makeCurveDraggable(LineArrow arrow) {
        arrow.getControlIndicator().setOnMousePressed(event -> {
            if (btnBeh.getCurrentState().equals(ButtonState.ARROW)) {
                btnBeh.resetToSelect();
            }
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - arrow.getControlIndicator().getTranslateX();
                    startY = event.getSceneY() - arrow.getControlIndicator().getTranslateY();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    arrow.resetControlPoint();
                }
            }
        });

        arrow.getControlIndicator().setOnMouseDragged(e -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                if (!dragging) {
                    dragging = true;
                    this.saveCurrentStateToHistory();
                }
                arrow.moveControlPoint(e.getSceneX() - startX, e.getSceneY() - startY);
            }
        });

        arrow.getControlIndicator().setOnMouseReleased(e -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                dragging = false;
            }
        });


    }

    public void makeDraggable(MyNode node) {
        node.setOnMousePressed(event -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    this.showDialog(node);
                } else if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - node.getTranslateX();
                    startY = event.getSceneY() - node.getTranslateY();
                }
            }
        });

        node.setOnMouseDragged(e -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                if (!dragging) {
                    dragging = true;
                    this.saveCurrentStateToHistory();
                }
                node.move(e.getSceneX() - startX, e.getSceneY() - startY);
                node.updateArrows(false);
            }
        });

        node.setOnMouseReleased(event -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT)) {
                node.updateArrows(true);
                dragging = false;
            }
        });
    }


    public void enableArrowCreation(MyNode node) {
        node.setOnMouseClicked(event -> {
            if (btnBeh.getCurrentState().equals(ButtonState.ARROW) && event.getButton() == MouseButton.PRIMARY) {
                createArrow(node);
            } else if (btnBeh.getCurrentState().equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                saveCurrentStateToHistory();
                this.removeObject(node);
            }
        });
    }

    public void cursorChange(MyNode myNode) {
        myNode.setOnMouseEntered(e -> {
            if (btnBeh.getCurrentState().equals(ButtonState.SELECT) ||
                    btnBeh.getCurrentState().equals(ButtonState.ERASE) ||
                    btnBeh.getCurrentState().equals(ButtonState.ARROW)) {
                myNode.setCursor(Cursor.HAND);
            }

        });
        myNode.setOnMouseExited(e -> myNode.setCursor(Cursor.DEFAULT));
    }

    public void makeErasable(Node node) {
        node.setOnMouseClicked(event -> {
            if (btnBeh.getCurrentState().equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                saveCurrentStateToHistory();
                this.removeObject(node);
            }
        });
    }

    // history

    public void saveCurrentStateToHistory() {
        historyLogic.saveCurrentState(nodeCounter, arrows, nodes);
    }

    public void redo() {
        AppState appState = historyLogic.redo(nodeCounter, arrows, nodes);
        createBoardFromAppState(appState);
    }

    public void undo() {
        AppState appState = historyLogic.undo(nodeCounter, arrows, nodes);
        createBoardFromAppState(appState);
    }

    public void createBoardFromAppState(AppState appState) {
        try {
            clearBoard();
            for (NodeModel myNodeModel : appState.getNodes()) {
                createMyNodeFromHistory(myNodeModel.getName(), myNodeModel.getX(), myNodeModel.getY(), myNodeModel.getNodeId(),
                        myNodeModel.isStarting(), myNodeModel.isEnding());
            }
            for (ArrowModel arrowModel : appState.getArrows()) {
                MyNode from = findNodeById(arrowModel.getFromNodeId());
                MyNode to = findNodeById(arrowModel.getToNodeId());
                if (arrowModel.isLineArrow()) {
                    createLineArrowFromHistory(from, to, arrowModel.getTransition(), arrowModel.getControlPointChangeX(), arrowModel.getControlPointChangeY());
                } else {
                    createSelfLoopArrowFromHistory(from, to, arrowModel.getTransition());
                }
            }
            this.nodeCounter = appState.getNodeCounter();
        } catch (MyCustomException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void createSelfLoopArrowFromHistory(MyNode from, MyNode to, TransitionInputs transitionInputs) throws MyCustomException {
        Arrow arrow = sameArrowExists(from, to);
        if (arrow != null) {
            boolean sameTransition = arrow.sameTransitionExists(transitionInputs);
            if (sameTransition) {
                throw new MyCustomException("Prechodová funkcia už existuje");
            }
            arrow.addSymbolContainer(transitionInputs);
            return;
        }
        arrow = new SelfLoopArrow(from, to, this, List.of(transitionInputs), this.idCounter);
        from.addArrow(arrow, "from");
        to.addArrow(arrow, "to");
        this.makeErasable(arrow);
        this.addObject(arrow);
    }

    private void createLineArrowFromHistory(MyNode from, MyNode to, TransitionInputs transitionInputs, Double controlPointChangeX, Double controlPointChangeY) throws MyCustomException {
        Arrow arrow = sameArrowExists(from, to);
        if (arrow != null) {
            boolean sameTransition = arrow.sameTransitionExists(transitionInputs);
            if (sameTransition) {
                throw new MyCustomException("Prechodová funkcia už existuje");
            }
            arrow.addSymbolContainer(transitionInputs);
            return;
        }
        arrow = new LineArrow(from, to, controlPointChangeX, controlPointChangeY, this, List.of(transitionInputs), this.idCounter);
        from.addArrow(arrow, "from");
        to.addArrow(arrow, "to");
        this.makeErasable(arrow);
        this.makeCurveDraggable((LineArrow) arrow);
        this.addObject(arrow);
    }

    public void createMyNodeFromHistory(String name, double x, double y, Long nodeId, boolean starting, boolean ending) {
        MyNode myNode = new MyNode(name, x, y, nodeId, starting, ending);
        if (starting) {
            setStarting(myNode, true);
        }
        this.makeDraggable(myNode);
        this.enableArrowCreation(myNode);
        this.cursorChange(myNode);
        this.addObject(myNode);
    }

    // testing

    public void testBoard() {
        saveCurrentStateToHistory();
        clearBoard();
        MyNode firstNode = createMyNode(120, 150);
        setStarting(firstNode, true);
        MyNode secondNode = createMyNode(320, 150);
        setEnding(firstNode, true);

        createMyArrow(firstNode.getNodeId(), firstNode.getNodeId(), new TransitionInputs("1", "Z", "XZ"));
        createMyArrow(firstNode.getNodeId(), firstNode.getNodeId(), new TransitionInputs("1", "X", "XX"));
        createMyArrow(firstNode.getNodeId(), firstNode.getNodeId(), new TransitionInputs(EPSILON, "X", EPSILON));
        createMyArrow(firstNode.getNodeId(), secondNode.getNodeId(), new TransitionInputs("0", "X", "X"));
        createMyArrow(secondNode.getNodeId(), secondNode.getNodeId(), new TransitionInputs("1", "X", EPSILON));
        createMyArrow(secondNode.getNodeId(), firstNode.getNodeId(), new TransitionInputs("0", "Z", "Z"));
    }

    public void convertPDA() {
        try {
            conversionLogic.convertPDA(getNodesTransitions(), nodes, startNode);
        } catch (MyCustomException e) {
            showErrorDialog(e.getMessage());
        }
    }
}
