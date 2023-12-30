package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
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

import static com.example.bakalar.canvas.MainController.NODE_RADIUS;

@Getter
@Setter
public abstract class Arrow extends Group {
    public static final int ARROW_HEAD_SZIE = NODE_RADIUS / 2;
    public static final String EPSILON = "ε";
    protected static final Logger log = LogManager.getLogger(Arrow.class.getName());
    protected MyNode from;
    protected MyNode to;
    protected String read;
    protected String pop;
    protected String push;
    protected VBox symbolContainers;
    protected Polygon arrowHead;
    protected Board board;


    public Arrow(MyNode from, MyNode to, Board board, NodeTransition nodeTransition) {
        super();
        this.from = from;
        this.to = to;
        this.read = nodeTransition.getRead();
        this.pop = nodeTransition.getPop();
        this.push = nodeTransition.getPush();
        this.board = board;

        this.symbolContainers = new VBox();
        setViewOrder(1);
        createArrowHead();
        addSymbolContainer(nodeTransition.getRead(), nodeTransition.getPop(), nodeTransition.getPush());
        this.getChildren().addAll(arrowHead, symbolContainers);
    }


    private void createArrowHead() {
        arrowHead = new Polygon();
        arrowHead.setStroke(Color.BLUE);
        arrowHead.setStrokeWidth(3);
        arrowHead.setFill(Color.BLUE);
        arrowHead.setViewOrder(-1);
    }

    public void addSymbolContainer(String read, String pop, String push) {
        HBox container = new HBox(NODE_RADIUS / 6.0);
        Text readSymbol = new Text(read);
        Text popSymbol = new Text(pop);
        Text pushSymbol = new Text(push);
        container.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
        container.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                board.createArrowTransition(this.read, this.pop, this.push);
            }
        });
        container.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            container.setLayoutX(newValue.getWidth());
            container.setLayoutX(newValue.getHeight());
            updateSymbolContainerPosition();
        });
        this.symbolContainers.getChildren().add(container);
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


    public NodeTransition getTransition() {
        return new NodeTransition(read, pop, push);
    }
}



