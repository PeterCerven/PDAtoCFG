package com.example.bakalar.canvas;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
import com.example.bakalar.canvas.node.StartNodeArrow;
import com.example.bakalar.character.MySymbol;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Getter
@Setter
public class Board {
    public static final String LAMDA = "λ";
    public static final String EPSILON = "ε";
    public static final String GAMMA_CAPITAL = "Γ";
    public static final String SIGMA = "Σ";
    public static final String DELTA_LOWER = "δ";
    public static final String Z_ZERO = "Z₀";
    private static final Logger log = LogManager.getLogger(Board.class.getName());
    private List<MyNode> nodes;
    private List<Arrow> arrows;
    private MyNode startNode;
    private AnchorPane mainPane;
    private StartNodeArrow startNodeArrow;
    private int nodeCounter;
    private CheckBox startingCheckBox;
    private CheckBox endingCheckBox;
    private TextField inputFieldAlphabet;
    private Button conversionBtn;
    private TextField describeStates;
    private TextField describeAlphabet;
    private TextField describeStackAlphabet;
    private TextField describeStartingState;
    private TextField describeStartingStackSymbol;
    private TextField describeEndStates;
    private VBox transFunctions;


    public Board(AnchorPane mainPane, TextField inputFieldAlphabet, List<TextField> describeFields, VBox transFunctions) {
        this.nodes = new ArrayList<>();
        this.transFunctions = transFunctions;
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
        StringBuilder text = new StringBuilder(GAMMA_CAPITAL + " = { " + Z_ZERO);
        Set<String> stackAlphabet = new HashSet<>();
        for (Arrow arrow : arrows) {
            if (!arrow.getPush().equals(EPSILON)) {
                String pushString = arrow.getPush();
                for (int i = 0; i < arrow.getPush().length(); i++) {
                    stackAlphabet.add((String) pushString.subSequence(i, i + 1));
                }
            }
        }
        for (String symbol : stackAlphabet) {
            text.append(", ").append(symbol);
        }
        text.append(" }");
        this.describeStackAlphabet.setText(text.toString());
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
        for (MyNode node : nodes) {
            for(Arrow arrow : node.getArrowsFrom()) {
                TextField textField = new TextField(createTransFunction(arrow));
                textField.setEditable(false);
                this.transFunctions.getChildren().add(textField);
            }
        }
    }

    private String createTransFunction(Arrow arrow) {
        StringBuilder text = new StringBuilder();
        text.append(DELTA_LOWER).append("(").append(arrow.getFrom().getName()).append(", ");
        text.append(arrow.getRead()).append(", ");
        text.append(arrow.getPop()).append(") = {");
        text.append(arrow.getTo().getName()).append(", ");
        text.append(arrow.getPush()).append("}");
        return text.toString();
    }


    // adding and removing objects

    public void removeObject(Node node) {
        if (node instanceof Arrow arrow) {
            arrows.remove(arrow);
        } else if (node instanceof MyNode myNode) {
            Iterator<Arrow> iterator = myNode.getArrows().iterator();
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

    public Arrow createArrow(MyNode from, MyNode to) {
        Arrow arrow = sameArrowExists(from, to);
        if (arrow != null) {
            arrow.addSymbolContainer();
            return arrow;
        }
        if (from == to) {
            arrow = new SelfLoopArrow(from, to, this);
        } else {
            if (oppositeExists(from, to)) {
                arrow = new LineArrow(from, to, 30, this);
            } else {
                arrow = new LineArrow(from, to, this);
            }
        }
        from.addArrow(arrow, true);
        to.addArrow(arrow, false);
        this.addObject(arrow);
        return arrow;
    }

    public void addObject(Node node) {
        if (node instanceof Arrow arrow) {
            arrows.add(arrow);
        } else if (node instanceof MyNode myNode) {
            char character = (char) (nodeCounter + '₀');
            // utf-8 subscript 0
            String name = "q" + character;

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
    }

    private void setEnding(MyNode node, boolean ending) {
        node.setEnding(ending);

    }

    public void selectNode(MyNode node, boolean select) {
        node.setSelected(select);
    }

    // event handlers

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


}
