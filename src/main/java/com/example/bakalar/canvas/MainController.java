package com.example.bakalar.canvas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class MainController {

    private static final Logger log = LogManager.getLogger(MainController.class.getName());

    public static final int NODE_RADIUS = 50;

    @FXML
    private ScrollPane myScrollPane;

    @FXML
    private AnchorPane mySecondAnchor;

    @FXML
    private Button nodeBtn;
    @FXML
    private Button arrowBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button eraseBtn;
    @FXML
    private Button undoBtn;
    @FXML
    private Button reUndoBtn;
    @FXML
    private Button selectBtn;

    private boolean nodeBtnOn;
    private boolean arrowBtnOn;
    private boolean selectBtnOn;
    private boolean eraseBtnOn;
    private MyNode currentNode;


    public MainController() {
    }


    public void createObject(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        if (nodeBtnOn) {
            createNode(x, y);
        }
    }

    private void createArrow(MyNode node) {
        if (currentNode != null) {
            log.info("Arrow created: startX:{} startY:{} | finishX:{} finishY:{}",
                    node.getX(), node.getY(), currentNode.getX(), currentNode.getY());
            Arrow arrow = new Arrow(currentNode, node,
                    currentNode.getX(), currentNode.getY(),
                    node.getX(), node.getY(), NODE_RADIUS);
            arrow.setOnMouseClicked(e -> {
                        if (eraseBtnOn) {
                            mySecondAnchor.getChildren().remove(arrow);
                        }
                    }
            );
            mySecondAnchor.getChildren().add(arrow);
            currentNode = null;
        } else {
            log.info("Starting point for arrow");
            currentNode = node;
        }
    }

    private void createNode(double x, double y) {
        log.info("Node created: X:{} Y:{}", x, y);
        MyNode myNode = new MyNode(x, y, NODE_RADIUS);
        myNode.setOnMouseClicked(e -> {
                    if (eraseBtnOn) {
                        mySecondAnchor.getChildren().remove(myNode);
                    }
                    if (arrowBtnOn) {
                        createArrow(myNode);
                    }
                }
        );
        mySecondAnchor.getChildren().add(myNode);
    }

    public void resetAll() {
        mySecondAnchor.getChildren().clear();
        turnOffAll();
    }


    public void drawNodeOn() {
        if (nodeBtnOn) {
            turnOffAll();
        } else {
            turnOffAll();
            nodeBtnOn = true;
            nodeBtn.setText("Vypni");
        }
    }

    public void drawArrowOn() {
        turnOffAll();
        if (arrowBtnOn) {
            turnOffAll();
        } else {
            turnOffAll();
            arrowBtnOn = true;
            arrowBtn.setText("Vypni");
        }
    }

    public void eraseFunctionOn() {
        turnOffAll();
        if (eraseBtnOn) {
            turnOffAll();
        } else {
            turnOffAll();
            eraseBtnOn = true;
            eraseBtn.setText("Vypni");
        }
    }

    public void selectOn() {
        turnOffAll();
        if (selectBtnOn) {
            turnOffAll();
        } else {
            turnOffAll();
            selectBtnOn = true;
            selectBtn.setText("Vypni");
        }
    }

    public void undo() {
        turnOffAll();
        // TODO undo
    }

    private void turnOffAll() {
        eraseBtnOn = false;
        nodeBtnOn = false;
        arrowBtnOn = false;
        selectBtnOn = false;
        currentNode = null;
        selectBtn.setText("Zvol vyber");
        arrowBtn.setText("Zvol sip");
        nodeBtn.setText("Zvol kruh");
        eraseBtn.setText("Zvol zmizik");
    }


    public void reUndo(ActionEvent actionEvent) {
        // TODO reUndo
    }
}
