package com.example.bakalar.canvas.arrow;

import com.example.bakalar.logic.Board;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.bakalar.logic.MainController.NODE_RADIUS;

@Getter
@Setter
public abstract class Arrow extends Group implements Cloneable{
    public static final int ARROW_HEAD_SZIE = NODE_RADIUS / 2;
    public static final String EPSILON = "ε";
    protected static final Logger log = LogManager.getLogger(Arrow.class.getName());
    protected MyNode from;
    protected MyNode to;
    protected List<NodeTransition> transitions;
    protected VBox symbolContainers;
    protected Polygon arrowHead;
    protected Board board;


    public Arrow(MyNode from, MyNode to, Board board, NodeTransition nodeTransition) {
        super();
        this.transitions = new ArrayList<>();
        this.from = from;
        this.to = to;
        this.board = board;

        this.symbolContainers = new VBox();
        setViewOrder(1);
        createArrowHead();
        addSymbolContainer(nodeTransition);
        this.getChildren().addAll(arrowHead, symbolContainers);
    }

    @Override
    public Arrow clone() {
        try {
            Arrow cloned = (Arrow) super.clone();

            // Deep clone the transitions list
            cloned.transitions = this.transitions.stream()
                    .map(NodeTransition::clone) // Assuming NodeTransition implements Cloneable
                    .collect(Collectors.toList());

            // Clone and recreate the symbolContainers and arrowHead
            cloned.symbolContainers = new VBox();
            for (NodeTransition transition : cloned.transitions) {
                // Recreate symbol containers for each transition
                // This assumes you have a method to create a container from a NodeTransition
                HBox container = createSymbolContainerForTransition(transition);
                cloned.symbolContainers.getChildren().add(container);
            }

            cloned.createArrowHead(); // Assuming this method initializes arrowHead properly

            // Add the cloned components to the children of the cloned Arrow
            cloned.getChildren().clear();
            cloned.getChildren().addAll(cloned.arrowHead, cloned.symbolContainers);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can never happen if Cloneable is implemented
        }
    }

    private HBox createSymbolContainerForTransition(NodeTransition transition) {
        HBox container = new HBox(NODE_RADIUS / 5.0);
        Text readSymbol = new Text(transition.getRead());
        Text popSymbol = new Text(transition.getPop());
        Text pushSymbol = new Text(transition.getPush());
        container.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
        container.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                NodeTransition newNodeTransition = board.createArrowTransition(transition.getRead(), transition.getPop(), transition.getPush());
                transition.setRead(newNodeTransition.getRead());
                transition.setPop(newNodeTransition.getPop());
                transition.setPush(newNodeTransition.getPush());
                container.getChildren().clear();
                readSymbol.setText(newNodeTransition.getRead());
                popSymbol.setText(newNodeTransition.getPop());
                pushSymbol.setText(newNodeTransition.getPush());
                container.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
                this.board.updateAllDescribePDA();
            }
        });
        container.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            container.setLayoutX(newValue.getWidth());
            container.setLayoutX(newValue.getHeight());
            updateSymbolContainerPosition();
        });
        return container;
    }


    private void createArrowHead() {
        arrowHead = new Polygon();
        arrowHead.setStroke(Color.BLUE);
        arrowHead.setStrokeWidth(3);
        arrowHead.setFill(Color.BLUE);
        arrowHead.setViewOrder(-1);
    }

    public void addSymbolContainer(NodeTransition nodeTransition) {
        HBox container = new HBox(NODE_RADIUS / 5.0);
        Text readSymbol = new Text(nodeTransition.getRead());
        Text popSymbol = new Text(nodeTransition.getPop());
        Text pushSymbol = new Text(nodeTransition.getPush());
        container.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
        container.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                NodeTransition newNodeTransition = board.createArrowTransition(nodeTransition.getRead(), nodeTransition.getPop(), nodeTransition.getPush());
                nodeTransition.setRead(newNodeTransition.getRead());
                nodeTransition.setPop(newNodeTransition.getPop());
                nodeTransition.setPush(newNodeTransition.getPush());
                container.getChildren().clear();
                readSymbol.setText(newNodeTransition.getRead());
                popSymbol.setText(newNodeTransition.getPop());
                pushSymbol.setText(newNodeTransition.getPush());
                container.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
                this.board.updateAllDescribePDA();
            }
        });
        container.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            container.setLayoutX(newValue.getWidth());
            container.setLayoutX(newValue.getHeight());
            updateSymbolContainerPosition();
        });
        this.transitions.add(nodeTransition);
        this.symbolContainers.getChildren().add(container);
        this.board.updateAllDescribePDA();
    }

    public abstract void updateObjects(boolean toEdge);

    public abstract void updateSymbolContainerPosition();


    protected Point2D getNodeEdgePoint(MyNode node, double targetX, double targetY) {

        double side1 = targetX - node.getAbsoluteCentrePosX();
        double side2 = targetY - node.getAbsoluteCentrePosY();

        double angle = Math.atan2(side2, side1);
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        double edgeX = node.getAbsoluteCentrePosX() + cosAngle * node.getCircle().getRadius();
        double edgeY = node.getAbsoluteCentrePosY() + sinAngle * node.getCircle().getRadius();

        return new Point2D(edgeX, edgeY);
    }

    protected ArrowHeadPoints getArrowHeadPoints(double endX, double endY, double endXAngled, double endYAngled) {
        double angle = Math.atan2((endY - endYAngled), (endX - endXAngled)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        //point1
        double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SZIE + endX;
        double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SZIE + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SZIE + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SZIE + endY;
        return new ArrowHeadPoints(endX, endY, x1, y1, x2, y2);
    }

    public abstract void updateArrowHead();


    public abstract void move(boolean toEdge);
}



