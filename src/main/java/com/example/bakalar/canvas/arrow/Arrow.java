package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
import com.example.bakalar.logic.Board;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.bakalar.logic.MainController.NODE_RADIUS;

@Getter
@Setter
public abstract class Arrow extends Group implements Serializable {
    public static final int ARROW_HEAD_SZIE = NODE_RADIUS / 2;
    public static final String EPSILON = "ε";
    protected static final Logger log = LogManager.getLogger(Arrow.class.getName());
    protected MyNode from;
    protected MyNode to;
    protected List<NodeTransition> transitions;
    protected VBox symbolContainers;
    protected Polygon arrowHead;
    protected Board board;
    protected int fromId;
    protected int toId;


    public Arrow(MyNode from, MyNode to, Board board, NodeTransition nodeTransition) {
        super();
        this.transitions = new ArrayList<>();
        this.from = from;
        this.to = to;
        this.fromId = from.getNodeId();
        this.toId = to.getNodeId();
        this.board = board;

        this.symbolContainers = new VBox();
        setViewOrder(1);
        this.arrowHead = createArrowHead();
        addSymbolContainer(nodeTransition);
        this.getChildren().addAll(arrowHead, symbolContainers);
    }

    public Arrow(MyNode from, MyNode to, Board board, List<NodeTransition> nodeTransition) {
        super();
        this.transitions = new ArrayList<>();
        this.from = from;
        this.to = to;
        this.fromId = from.getNodeId();
        this.toId = to.getNodeId();
        this.board = board;

        this.symbolContainers = new VBox();
        setViewOrder(1);
        this.arrowHead = createArrowHead();
        for (NodeTransition transition : nodeTransition) {
            addSymbolContainer(transition);
        }
        this.getChildren().addAll(arrowHead, symbolContainers);
    }


    private Polygon createArrowHead() {
        Polygon arrowHead = new Polygon();
        arrowHead.setStroke(Color.BLUE);
        arrowHead.setStrokeWidth(3);
        arrowHead.setFill(Color.BLUE);
        arrowHead.setViewOrder(-1);
        return arrowHead;
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
            this.symbolContainers = updateSymbolContainerPosition(this.symbolContainers);
        });
        this.transitions.add(nodeTransition);
        this.symbolContainers.getChildren().add(container);
        this.board.updateAllDescribePDA();
    }

    public abstract void updateObjects(boolean toEdge);

    public abstract VBox updateSymbolContainerPosition(VBox symbolContainers);


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arrow arrow)) return false;

        if (!getFrom().equals(arrow.getFrom())) return false;
        if (!getTo().equals(arrow.getTo())) return false;
        return getTransitions().equals(arrow.getTransitions());
    }

    @Override
    public int hashCode() {
        return 31 * getTransitions().hashCode();
    }
}



