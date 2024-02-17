package com.example.bakalar.logic;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.logic.button.ButtonState;
import com.example.bakalar.logic.conversion.Rule;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
import com.example.bakalar.canvas.node.StartNodeArrow;
import com.example.bakalar.logic.history.ArrowHistory;
import com.example.bakalar.logic.history.BoardHistory;
import com.example.bakalar.logic.history.MyHistory;
import com.example.bakalar.logic.history.NodeHistory;
import com.example.bakalar.logic.transitions.Transition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.*;

import static com.example.bakalar.logic.MainController.NODE_RADIUS;

@Getter
@Setter
public class Board implements Serializable {
    public static final String EPSILON = "ε";
    public static final String GAMMA_CAPITAL = "Γ";
    public static final String SIGMA = "Σ";
    public static final String DELTA_LOWER = "δ";
    public static final String STARTING_Z = "Z";
    public static final String STARTING_S = "S";
    private static final Logger log = LogManager.getLogger(Board.class.getName());
    private List<MyNode> nodes;
    private List<Arrow> arrows;
    private MyNode startNode;
    private  AnchorPane mainPane;
    private StartNodeArrow startNodeArrow;
    private int nodeCounter;
    private  CheckBox startingCheckBox;
    private  CheckBox endingCheckBox;
    private  TextField inputFieldAlphabet;
    private  Button conversionBtn;
    private  TextField describeStates;
    private  TextField describeAlphabet;
    private  TextField describeStackAlphabet;
    private  TextField describeStartingState;
    private  TextField describeStartingStackSymbol;
    private  TextField describeEndStates;
    private  VBox transFunctions;
    private  VBox rulesContainer;
    private List<Rule> rules;
    private int idNodeCounter;
    private ButtonState currentState;
    private Stack<MyHistory> undoStack;
    private Stack<MyHistory> redoStack;
    private double startX, startY;
    private MyNode selectedNode;
    


    public Board(AnchorPane mainPane, TextField inputFieldAlphabet, List<TextField> describeFields, VBox transFunctions,
                 VBox rulesContainer, ButtonState currentState, Stack<MyHistory> undoStack, Stack<MyHistory> redoStack) {
        this.nodes = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.transFunctions = transFunctions;
        this.rulesContainer = rulesContainer;
        initDescribeFields(describeFields);
        this.arrows = new ArrayList<>();
        this.inputFieldAlphabet = inputFieldAlphabet;
        this.mainPane = mainPane;
        this.startNodeArrow = new StartNodeArrow(0, 0, 0);
        this.addObject(startNodeArrow);
        this.startNodeArrow.setVisible(false);
        this.nodeCounter = 0;
        this.startingCheckBox = new CheckBox("Starting Node");
        this.endingCheckBox = new CheckBox("End Node");
        this.idNodeCounter = 0;
        this.currentState = currentState;
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    // init functions

    private void initDescribeFields(List<TextField> describeFields) {
        this.describeStates = describeFields.get(0);
        this.describeAlphabet = describeFields.get(1);
        this.describeStackAlphabet = describeFields.get(2);
        this.describeEndStates = describeFields.get(3);
    }

    // describe functions

    public void updateAllDescribePDA() {
        updateDescribeStates();
        updateDescribeAlphabet();
        updateDescribeStackAlphabet();
        updateDescribeEndStates();
        updateDescribeTransFunctions();
    }

    private void updateDescribeStates() {
        StringBuilder text = new StringBuilder("K = {");
        for (MyNode node : nodes) {
            text.append(node.getName()).append(", ");
        }
        if (!nodes.isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeStates.setText(text.toString());
    }

    private void updateDescribeAlphabet() {
        StringBuilder text = new StringBuilder(SIGMA + " = {");
        Set<Character> alphabet = new HashSet<>();
        for (Character character : inputFieldAlphabet.getText().toCharArray()) {
            alphabet.add(character);
        }
        for (Character character : alphabet) {
            text.append(character).append(", ");
        }
        if (!inputFieldAlphabet.getText().isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeAlphabet.setText(text.toString());
    }

    private void updateDescribeStackAlphabet() {
        StringBuilder text = new StringBuilder(GAMMA_CAPITAL + " = {");
        Set<String> stackAlphabet = getStrings();
        for (String symbol : stackAlphabet) {
            text.append(symbol).append(", ");
        }
        if (!stackAlphabet.isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeStackAlphabet.setText(text.toString());
    }

    private Set<String> getStrings() {
        Set<String> stackAlphabet = new HashSet<>();
        stackAlphabet.add(STARTING_Z);
        for (Arrow arrow : arrows) {
            for (NodeTransition nodeTransition : arrow.getTransitions()) {
                String pushString = nodeTransition.getPush();
                String popString = nodeTransition.getPop();
                if (!popString.equals(EPSILON))
                    stackAlphabet.add(popString);
                for (int i = 0; i < nodeTransition.getPush().length(); i++) {
                    String substring = pushString.substring(i, i + 1);
                    if (!substring.equals(EPSILON))
                        stackAlphabet.add(substring);
                }
            }
        }
        return stackAlphabet;
    }


    private void updateDescribeEndStates() {
        StringBuilder text = new StringBuilder("F = {");
        boolean hasEndStates = false;
        for (MyNode node : nodes) {
            if (node.isEnding()) {
                text.append(node.getName()).append(", ");
                hasEndStates = true;
            }
        }
        if (hasEndStates) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeEndStates.setText(text.toString());
    }

    public void updateDescribeTransFunctions() {
        this.transFunctions.getChildren().clear();
        List<Transition> transitions = getNodesTransitions();
        for (Transition transition : transitions) {
            TextField textField = new TextField(transition.toString());
            textField.setEditable(false);
            this.transFunctions.getChildren().add(textField);
        }
    }


    public List<Transition> getNodesTransitions() {
        List<Transition> transitions = new ArrayList<>();
        for (MyNode node : nodes) {
            for (Arrow arrow : node.getArrowsFrom()) {
                for (NodeTransition nodeTransition : arrow.getTransitions()) {
                    transitions.add(new Transition(arrow.getFrom().getName(), nodeTransition.getRead(),
                            nodeTransition.getPop(), arrow.getTo().getName(), nodeTransition.getPush()));
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

    public Arrow createArrow(MyNode from, MyNode to, String read, String pop, String push) {
        NodeTransition nodeTransition;
        if (read != null) {
            nodeTransition = new NodeTransition(read, pop, push);
            this.updateAllDescribePDA();
        } else {
            nodeTransition = createArrowTransition("", "", "");
        }
        if (nodeTransition == null) {
            return null;
        }
        Arrow arrow = sameArrowExists(from, to);
        if (arrow != null) {
            arrow.addSymbolContainer(nodeTransition);
            return arrow;
        }
        List<NodeTransition> transitions = List.of(nodeTransition);
        if (from == to) {
            arrow = new SelfLoopArrow(from, to, this, transitions);
        } else {
            if (oppositeExists(from, to)) {
                arrow = new LineArrow(from, to, 30, this, transitions);
            } else {
                arrow = new LineArrow(from, to, this, transitions);
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
            myNode.setNodeId(idNodeCounter++);

            myNode.setName(name);
            nodeCounter++;
            nodes.add(myNode);

        }
        mainPane.getChildren().add(node);
        updateAllDescribePDA();
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
    }


    public void removeStartingFromOtherNodes() {
        for (MyNode node : nodes) {
            setStarting(node, false);
        }
    }

    public void setStarting(MyNode node, boolean starting) {
        node.setStarting(starting);
        this.setStartNode(node);
    }

    public void setEnding(MyNode node, boolean ending) {
        node.setEnding(ending);

    }

    public void selectNode(MyNode node, boolean select) {
        node.setSelected(select);
    }

    // event handlers

    public NodeTransition createArrowTransition(String read, String pop, String push) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create transition");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField input1 = new TextField(read == null || read.isBlank() ? EPSILON : read);
        input1.setPromptText("Read");
        Label label1 = new Label("Read");
        TextField input2 = new TextField(pop == null || pop.isBlank() ? EPSILON : pop);
        input2.setPromptText("Pop");
        Label label2 = new Label("Pop");
        TextField input3 = new TextField(push == null || push.isBlank() ? EPSILON : push);
        input3.setPromptText("Push");
        Label label3 = new Label("Push");

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
            String newRead = input1.getText().isBlank() ? EPSILON : input1.getText();
            String newPop = input2.getText().isBlank() ? EPSILON : input2.getText();
            String newPush = input3.getText().isBlank() ? EPSILON : input3.getText();

            this.updateAllDescribePDA();
            return new NodeTransition(newRead, newPop, newPush);
        }
        return null;
    }

    public void showDialog(MyNode node) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Node");

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
                node.setName(nameField.getText());
                if (startingCheckBox.isSelected()) {
                    removeStartingFromOtherNodes();
                    this.startNode = node;
                }
                setStarting(node, startingCheckBox.isSelected());
                setEnding(node, endingCheckBox.isSelected());
            }
            updateAllDescribePDA();
        });
    }

    // rules

    public void testBoard() {
        clearBoard();
    }

    // history

    public MyHistory createHistory() {
        BoardHistory boardHistory = new BoardHistory(startNode == null ? null : startNode.getNodeId(), nodeCounter, idNodeCounter);
        List<ArrowHistory> arrowHistories = createArrowHistory();
        List<NodeHistory> nodeHistories = createNodeHistory();

        return new MyHistory(boardHistory, nodeHistories, arrowHistories);
    }

    private List<ArrowHistory> createArrowHistory() {
        List<ArrowHistory> arrowHistories = new ArrayList<>();
        for (Arrow arrow : arrows) {
            if (arrow instanceof LineArrow lineArrow) {
                arrowHistories.add(new ArrowHistory(lineArrow.getStartX(), lineArrow.getStartY(), lineArrow.getEndX(), lineArrow.getEndY(),
                        lineArrow.getFromId(), lineArrow.getToId(), lineArrow.getTransitions(), lineArrow.getControlX(), lineArrow.getControlY(), true));
            } else if (arrow instanceof SelfLoopArrow selfLoopArrow) {
                arrowHistories.add(new ArrowHistory(selfLoopArrow.getFromId(), selfLoopArrow.getToId(), selfLoopArrow.getTransitions(), false));
            }
        }
        return arrowHistories;
    }

    private List<NodeHistory> createNodeHistory() {
        List<NodeHistory> nodeHistories = new ArrayList<>();
        for (MyNode node : nodes) {
            nodeHistories.add(new NodeHistory(node.getName(), node.getAbsoluteCentrePosX(), node.getAbsoluteCentrePosY(), node.isStarting(), node.isEnding(), node.getNodeId()));
        }
        return nodeHistories;

    }

    public void restoreBoardFromHistory(MyHistory myHistory) {
        clearBoard();
        BoardHistory boardHistory = myHistory.getBoardHistory();
        this.nodeCounter = boardHistory.getNodeCounter();
        this.idNodeCounter = boardHistory.getIdNodeCounter();
        Integer startNodeId = boardHistory.getStartNodeId();
        List<NodeHistory> nodeHistories = myHistory.getNodeHistory();
        List<ArrowHistory> arrowHistories = myHistory.getArrowHistory();
        List<Node> newNodes = new ArrayList<>();
        for (NodeHistory nodeHistory : nodeHistories) {
            MyNode myNode = new MyNode(nodeHistory.getName(), nodeHistory.getX(), nodeHistory.getY(), nodeHistory.getNodeId(), nodeHistory.isStarting(), nodeHistory.isEnding());
            this.makeDraggable(myNode);
            this.enableArrowCreation(myNode);
            newNodes.add(addObjectFromHistory(myNode));
        }
        for (ArrowHistory arrowHistory : arrowHistories) {
            Arrow arrow;
            MyNode from = findNodeById(arrowHistory.getFromId());
            MyNode to = findNodeById(arrowHistory.getToId());
            if (arrowHistory.isLineArrow()) {
                arrow = new LineArrow(from, to, this, arrowHistory.getTransitions());
                makeCurveDraggable((LineArrow) arrow);
            } else {
                arrow = new SelfLoopArrow(from, to, this, arrowHistory.getTransitions());
            }
            newNodes.add(addObjectFromHistory(arrow));
        }
        if (startNodeId != null) {
            for (Node node : newNodes) {
                if (node instanceof MyNode myNode) {
                    if (myNode.getNodeId() == startNodeId) {
                        this.setStarting(myNode, true);
                    }
                }
            }
        }
        mainPane.getChildren().addAll(newNodes);
        updateAllDescribePDA();
    }

    private MyNode findNodeById(int toId) {
        for (MyNode node : nodes) {
            if (node.getNodeId() == toId) {
                return node;
            }
        }
        return null;
    }

    private Node addObjectFromHistory(Node node) {
        if (node instanceof Arrow arrow) {
            arrows.add(arrow);
        } else if (node instanceof MyNode myNode) {
            nodes.add(myNode);
        }
        return node;
    }

    public void saveCurrentState() {
        MyHistory myHistory = this.createHistory();
        undoStack.push(myHistory);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(this.createHistory());
            MyHistory boardHistory = undoStack.pop();
            this.restoreBoardFromHistory(boardHistory);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(this.createHistory());
            MyHistory nextState = redoStack.pop();
            this.restoreBoardFromHistory(nextState);
        }
    }

    public MyNode createMyNode(double x, double y) {
        MyNode myNode = new MyNode(x, y, NODE_RADIUS);
        this.makeDraggable(myNode);
        this.enableArrowCreation(myNode);
        this.addObject(myNode);
        return myNode;
    }

    // Event handlers


    public void makeCurveDraggable(LineArrow arrow) {
        arrow.getControlIndicator().setOnMousePressed(event -> {
            if (currentState.equals(ButtonState.SELECT)) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - arrow.getControlIndicator().getTranslateX();
                    startY = event.getSceneY() - arrow.getControlIndicator().getTranslateY();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    arrow.resetControlPoint();
                }
            }
        });

        arrow.getControlIndicator().setOnMouseDragged(e -> {
            if (currentState.equals(ButtonState.SELECT)) {
                arrow.moveControlPoint(e.getSceneX() - startX, e.getSceneY() - startY);
            }
        });


    }


    public void makeDraggable(MyNode node) {
        node.setOnMousePressed(event -> {
            if (currentState.equals(ButtonState.SELECT)) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    this.showDialog(node);
                } else if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - node.getTranslateX();
                    startY = event.getSceneY() - node.getTranslateY();
                }
            }
        });

        node.setOnMouseDragged(e -> {
            if (currentState.equals(ButtonState.SELECT)) {
                node.move(e.getSceneX() - startX, e.getSceneY() - startY);
                node.updateArrows(false);
            }
        });

        node.setOnMouseReleased(event -> {
            if (currentState.equals(ButtonState.SELECT)) {
                node.updateArrows(true);
            }
        });
    }


    public void enableArrowCreation(MyNode node) {
        node.setOnMouseClicked(event -> {
            if (currentState.equals(ButtonState.ARROW) && event.getButton() == MouseButton.PRIMARY) {
                createArrow(node);
            } else if (currentState.equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                saveCurrentState();
                this.removeObject(node);
            }
        });
    }

    private void createArrow(MyNode node) {
        if (selectedNode != null) {
            saveCurrentState();
            createMyArrow(selectedNode, node, null, null, null);
            this.selectNode(selectedNode, false);
            selectedNode = null;

        } else {
            selectedNode = node;
            this.selectNode(node, true);
        }
    }

    public void createMyArrow(MyNode from, MyNode to, String input, String stackTop, String stackPush) {
        Arrow arrow = this.createArrow(from, to, input, stackTop, stackPush);
        if (arrow != null) {
            this.makeErasable(arrow);
            if (arrow instanceof LineArrow lineArrow) {
                this.makeCurveDraggable(lineArrow);
            }
        }
    }

    public void makeErasable(Node node) {
        node.setOnMouseClicked(event -> {
            if (currentState.equals(ButtonState.ERASE) && event.getButton() == MouseButton.PRIMARY) {
                saveCurrentState();
                this.removeObject(node);
            }
        });
    }
}
