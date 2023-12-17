package com.example.bakalar.canvas;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.StartNodeArrow;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class Board {
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


    public Board(AnchorPane mainPane, TextField inputFieldAlphabet, List<TextField> describeFields) {
        this.nodes = new ArrayList<>();
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
        this.describeStartingState = describeFields.get(3);
        this.describeStartingStackSymbol = describeFields.get(4);
        this.describeEndStates = describeFields.get(5);
    }

    // describe functions

    public void updateAllDescribePDA() {
        updateDescribeStates();
        updateDescribeAlphabet();
//        updateDescribeStackAlphabet();
//        updateDescribeStartingState();
//        updateDescribeStartingStackSymbol();
//        updateDescribeEndStates();
    }

    private void updateDescribeStates() {
        StringBuilder text = new StringBuilder("Q = {");
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
        StringBuilder text = new StringBuilder("\u03A3 = {");
        for (Character character : inputFieldAlphabet.getText().toCharArray()) {
            text.append(character).append(", ");
        }
        if (!inputFieldAlphabet.getText().isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeAlphabet.setText(text.toString());
    }

    private void updateDescribeStackAlphabet() {
        this.describeStackAlphabet.setText("s");
    }

    private void updateDescribeStartingState() {
        this.describeStartingState.setText(this.startNode.toString());
    }

    private void updateDescribeStartingStackSymbol() {
        this.describeStartingStackSymbol.setText(this.startNode.toString());
    }

    private void updateDescribeEndStates() {
        this.describeEndStates.setText(this.nodes.toString());
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
            arrow = new SelfLoopArrow(from, to);
        } else {
            if (oppositeExists(from, to)) {
                arrow = new LineArrow(from, to, 30);
            } else {
                arrow = new LineArrow(from, to);
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
            myNode.setName("Q" + nodeCounter++);
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
        });
    }


}
