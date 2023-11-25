package com.example.bakalar.canvas;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.SelfLoopArrow;
import com.example.bakalar.canvas.node.MyNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MainController {

    public static final int NODE_RADIUS = 30;
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


    public void createNode(MouseEvent event) {
        if (nodeBtnOn && event.getButton() == MouseButton.PRIMARY) {
            createNode(event.getX(), event.getY());
        }
    }

    private void createArrow(MyNode node) {
        if (currentNode != null) {
            log.info("Arrow created: startX:{} startY:{} | finishX:{} finishY:{}",
                    currentNode.getAbsoluteCentrePosX(), currentNode.getAbsoluteCentrePosY(),
                    node.getAbsoluteCentrePosX(), node.getAbsoluteCentrePosY());
            Arrow arrow = board.createArrow(currentNode, node);
            makeErasable(arrow);
            currentNode.unselectNode();
            currentNode = null;
        } else {
            log.info("Starting point for arrow");
            currentNode = node;
            node.selectNode();
        }
    }

    private void makeCurveDraggable(LineArrow arrow) {
        arrow.getControlIndicator().setOnMousePressed(event -> {
            if (selectBtnOn) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - arrow.getControlIndicator().getTranslateX();
                    startY = event.getSceneY() - arrow.getControlIndicator().getTranslateY();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    arrow.resetControlPoint();
                }
            }
        });

        arrow.getControlIndicator().setOnMouseDragged(e -> {
            if (selectBtnOn) {
                arrow.moveControlPoint(e.getSceneX() - startX, e.getSceneY() - startY);
            }
        });
    }


    private void makeDraggable(MyNode node) {
        node.setOnMousePressed(event -> {
            if (selectBtnOn) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    log.info("secondary button");
                    node.showDialog();
                } else if (event.getButton() == MouseButton.PRIMARY) {
                    startX = event.getSceneX() - node.getTranslateX();
                    startY = event.getSceneY() - node.getTranslateY();
                }
            }
        });

        node.setOnMouseDragged(e -> {
            if (selectBtnOn) {
                node.move(e.getSceneX() - startX, e.getSceneY() - startY);
            }
        });
    }

    private void enableArrowCreation(MyNode node) {
        node.setOnMouseClicked(event -> {
            if (arrowBtnOn && event.getButton() == MouseButton.PRIMARY) {
                createArrow(node);
            }
        });
    }

    private void makeErasable(Node node) {
        node.setOnMouseClicked(event -> {
            if (eraseBtnOn && event.getButton() == MouseButton.PRIMARY) {
                board.removeObject(node);
            }
        });
    }


    private void createNode(double x, double y) {
        log.info("Node created: X:{} Y:{}", x, y);
        MyNode myNode = new MyNode(x, y, NODE_RADIUS, board);
        makeDraggable(myNode);
        makeErasable(myNode);
        enableArrowCreation(myNode);
        board.addObject(myNode);
    }

    public void resetAll() {
        board.clearBoard();
        turnOffAllButtons();
    }


    public void drawNodeOn() {
        if (nodeBtnOn) {
            turnOffAllButtons();
        } else {
            turnOffAllButtons();
            nodeBtnOn = true;
            nodeBtn.setText("Vypni");
        }
    }

    public void drawArrowOn() {
        turnOffAllButtons();
        if (arrowBtnOn) {
            turnOffAllButtons();
        } else {
            turnOffAllButtons();
            arrowBtnOn = true;
            arrowBtn.setText("Vypni");
        }
    }

    public void eraseFunctionOn() {
        turnOffAllButtons();
        if (eraseBtnOn) {
            turnOffAllButtons();
        } else {
            turnOffAllButtons();
            eraseBtnOn = true;
            eraseBtn.setText("Vypni");
        }
    }

    public void selectOn() {
        turnOffAllButtons();
        if (selectBtnOn) {
            turnOffAllButtons();
        } else {
            turnOffAllButtons();
            selectBtnOn = true;
            selectBtn.setText("Vypni");
        }
    }

    private void turnOffAllButtons() {
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

    public void undo() {
        turnOffAllButtons();
        // TODO undo
    }
}
