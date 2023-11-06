package com.example.bakalar.canvas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        if (currentNode != null && currentNode != node) {
            log.info("Arrow created: startX:{} startY:{} | finishX:{} finishY:{}",
                    node.getX(), node.getY(), currentNode.getX(), currentNode.getY());
            LineCoordinates lineCoordinates = getEdgePoints(currentNode.getX(), currentNode.getY(), node.getX(), node.getY());
            Arrow arrow = new Arrow(currentNode, node, lineCoordinates);
            arrow.setStroke(Color.RED);
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

    private LineCoordinates getEdgePoints(double startX, double startY, double endX, double endY) {
        log.info("Start X:{} Y:{}", startX, startY);
        log.info("End X:{} Y:{}", endX, endY);
        double side1 = startX - endX;
        double side2 = startY - endY;

        double angle1 = Math.atan(side1 / side2);

        double newDiffX = Math.sin(angle1) * (double) MainController.NODE_RADIUS;
        double newDiffY = Math.cos(angle1) * (double) MainController.NODE_RADIUS;

        if (startX > endX && startY > endY) {
            newDiffX = -newDiffX;
            newDiffY = -newDiffY;
        } else if (startX < endX && startY > endY) {
            newDiffY = -newDiffY;
        }
        return new LineCoordinates(startX + newDiffX, startY + newDiffY, endX - newDiffX, endY - newDiffY);
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
