package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.MyObject;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.MainLogic;
import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.example.bakalar.logic.MainLogic.NODE_RADIUS;

@Getter
@Setter
public abstract class Arrow extends MyObject {
    private static final double ARROW_HEAD_STROKE_WIDTH = 3.0;
    private static final double ARROW_HEAD_SCALE = NODE_RADIUS / 2.0;
    private static final double VIEW_ORDER_ARROW_HEAD = 1;

    protected VBox symbolContainers;
    protected MyNode nodeFrom;
    protected MyNode nodeTo;
    protected Polygon arrowHead;
    protected MainLogic mainLogic;
    private Long nodeFromId;
    private Long nodeToId;
    private ArrowSymbolContainerHandler arrowSymbolContainerHandler;


    public Arrow(MyNode nodeFrom, MyNode nodeTo, MainLogic mainLogic, List<TransitionInputs> transitions) {
        super();
        this.symbolContainers = new VBox();
        this.arrowSymbolContainerHandler = new ArrowSymbolContainerHandler(mainLogic, transitions, symbolContainers, this);
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.nodeFromId = nodeFrom.getID();
        this.nodeToId = nodeTo.getID();
        this.mainLogic = mainLogic;
        createArrowHead();
        this.setViewOrder(VIEW_ORDER_ARROW_HEAD);
        this.getChildren().addAll(arrowHead, symbolContainers);
    }


    private void createArrowHead() {
        arrowHead = new Polygon();
        arrowHead.setStroke(Color.BLACK);
        arrowHead.setStrokeWidth(ARROW_HEAD_STROKE_WIDTH);
        arrowHead.setFill(Color.BLACK);
        arrowHead.setViewOrder(VIEW_ORDER_ARROW_HEAD);
    }

    public abstract void updateObjects(boolean toEdge);

    public abstract VBox updateStackPanePosition(VBox symbolContainers);


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
        double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * (ARROW_HEAD_SCALE) + endX;
        double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * (ARROW_HEAD_SCALE) + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * (ARROW_HEAD_SCALE) + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * (ARROW_HEAD_SCALE) + endY;
        return new ArrowHeadPoints(endX, endY, x1, y1, x2, y2);
    }

    public abstract void updateArrowHead();


    public abstract void move(boolean toEdge);


    public boolean sameTransitionExists(TransitionInputs transitionInputs) {
        for (TransitionInputs transition : arrowSymbolContainerHandler.getTransitions()) {
            if (transition.equals(transitionInputs)) {
                return true;
            }
        }
        return false;
    }

    public void addSymbolContainer(TransitionInputs transitionInputs) {
        arrowSymbolContainerHandler.addSymbolContainer(transitionInputs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arrow arrow)) return false;

        if (getNodeFromId() != null ? !getNodeFromId().equals(arrow.getNodeFromId()) : arrow.getNodeFromId() != null) return false;
        if (getNodeToId() != null ? !getNodeToId().equals(arrow.getNodeToId()) : arrow.getNodeToId() != null) return false;
        return getID() != null ? getID().equals(arrow.getID()) : arrow.getID() == null;
    }

    @Override
    public int hashCode() {
        int result = getNodeFromId() != null ? getNodeFromId().hashCode() : 0;
        result = 31 * result + (getNodeToId() != null ? getNodeToId().hashCode() : 0);
        result = 31 * result + (getID() != null ? getID().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Arrow{" +
                "transitions=" + arrowSymbolContainerHandler.getTransitions() +
                ", fromId=" + nodeFromId +
                ", toId=" + nodeToId +
                '}';
    }

}



