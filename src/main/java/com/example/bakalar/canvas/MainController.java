package com.example.bakalar.canvas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MainController {

    public static final int NODE_RADIUS = 50;
    private static final Logger log = LogManager.getLogger(MainController.class.getName());
    @FXML
    private ScrollPane myScrollPane;

    @FXML
    private AnchorPane mainPane;

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
    private double startX;
    private double startY;
    private Board board;


    public MainController() {
    }

    @FXML
    private void initialize() {
        board = new Board(mainPane);
    }


    public void createObject(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        if (nodeBtnOn) {
            createNode(x, y);
        }
    }

    private void createArrow(MyNode node) {
        if (currentNode != null && currentNode != node) {
            log.info("Arrow created: startX:{} startY:{} | finishX:{} finishY:{}",
                    currentNode.getAbsoluteCentrePosX(), currentNode.getAbsoluteCentrePosY(),
                    node.getAbsoluteCentrePosX(), node.getAbsoluteCentrePosY());
            Arrow arrow = new Arrow(currentNode, node, board);
            arrow.setFrom(currentNode);
            arrow.setTo(node);
            currentNode.addArrow(arrow);
            node.addArrow(arrow);
            makeErasable(arrow);
            currentNode = null;
        } else {
            log.info("Starting point for arrow");
            currentNode = node;
        }
    }


    private void makeDraggable(MyNode node) {
        node.setOnMousePressed(e -> {
            if (selectBtnOn) {
                startX = e.getSceneX() - node.getTranslateX();
                startY = e.getSceneY() - node.getTranslateY();
            }
        });

        node.setOnMouseDragged(e -> {
            if (selectBtnOn) {
                double x = e.getSceneX() - startX;
                double y = e.getSceneY() - startY;
                node.setTranslateX(x);
                node.setTranslateY(y);
                node.moveAllArrows();
            }
        });
    }

    private void enableArrowCreation(MyNode node) {
        node.setOnMouseClicked(n -> {
            if (arrowBtnOn) {
                createArrow(node);
            }
        });
    }

    private void makeErasable(Node node) {
        node.setOnMouseClicked(n -> {
            if (eraseBtnOn) {
                board.removeObject(node);
            }
        });
    }


    private void createNode(double x, double y) {
        log.info("Node created: X:{} Y:{}", x, y);
        MyNode myNode = new MyNode(x, y, NODE_RADIUS);
        makeDraggable(myNode);
        makeErasable(myNode);
        enableArrowCreation(myNode);
        board.addObject(myNode);
    }

    public void resetAll() {
        board.clearBoard();
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
