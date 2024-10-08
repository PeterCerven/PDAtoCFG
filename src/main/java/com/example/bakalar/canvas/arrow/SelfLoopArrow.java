package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.MainLogic;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeType;
import lombok.Getter;

import java.util.List;

import static com.example.bakalar.logic.MainLogic.NODE_RADIUS;

@Getter
public class SelfLoopArrow extends Arrow {

    public double arcWidth;
    public double arcHeight;
    public double ARC_START_ANGLE = 20;
    public static int ARC_LENGTH = 150;
    private Arc arc;


    public SelfLoopArrow(MyNode from, MyNode to, MainLogic mainLogic, List<TransitionInputs> transitionInputs) {
        super(from, to, mainLogic, transitionInputs);

        this.arcWidth = NODE_RADIUS / 2.0;
        this.arcHeight = (NODE_RADIUS / 2.0) * 3;

        createArc();
        this.getChildren().add(arc);
        this.updateObjects(true);
    }

    private void createArc() {
        arc = new Arc();
        arc.setStroke(Color.BLACK);
        arc.setFill(Color.TRANSPARENT);
        arc.setStrokeType(StrokeType.OUTSIDE);
        arc.setType(ArcType.OPEN);
        arc.setLength(ARC_LENGTH);
        arc.setStartAngle(ARC_START_ANGLE);
        arc.setStrokeWidth(NODE_RADIUS / 13.0);
        arc.setStyle("-fx-cursor: hand;");
    }

    @Override
    public void updateObjects(boolean toEdge) {
        arc.setCenterX(nodeFrom.getAbsoluteCentrePosX());
        arc.setCenterY(nodeFrom.getAbsoluteCentrePosY());
        arc.setRadiusX(nodeFrom.getCircle().getRadius() - arcWidth);
        arc.setRadiusY(nodeFrom.getCircle().getRadius() + arcHeight);

        updateArrowHead();
        this.symbolContainers = updateStackPanePosition(this.symbolContainers);
    }

    @Override
    public VBox updateStackPanePosition(VBox symbolContainers) {
        double midX = nodeFrom.getAbsoluteCentrePosX();
        double midY = nodeFrom.getAbsoluteCentrePosY() - (nodeFrom.getCircle().getRadius() + arcHeight);

        double offsetX = -symbolContainers.getWidth() / 2.0;
        double offsetY = -(symbolContainers.getChildren().size() * (NODE_RADIUS / 1.8)) - (NODE_RADIUS / 8.0);

        symbolContainers.setLayoutX(midX + offsetX);
        symbolContainers.setLayoutY(midY + offsetY);
        return symbolContainers;
    }

    @Override
    public void updateArrowHead() {
        double startAngleRadians = Math.toRadians(ARC_START_ANGLE);

        double startX = arc.getCenterX() + arc.getRadiusX() * Math.cos(startAngleRadians);
        double startY = arc.getCenterY() + arc.getRadiusY() * Math.sin(startAngleRadians);

        double arrowHeadLength = NODE_RADIUS / 5.0;
        double arrowHeadAngle = Math.toRadians(30);

        double angleToNode = Math.atan2(nodeFrom.getAbsoluteCentrePosY() - startY, nodeFrom.getAbsoluteCentrePosX() - startX);

        double radius = nodeFrom.getCircle().getRadius();
        double intersectX = nodeFrom.getAbsoluteCentrePosX() + (radius + 2) * Math.cos(angleToNode);
        double intersectY = nodeFrom.getAbsoluteCentrePosY() + (radius + 4) * Math.sin(angleToNode);


        double baseLeftX = intersectX + arrowHeadLength * Math.cos(angleToNode - arrowHeadAngle);
        double baseLeftY = intersectY + arrowHeadLength * Math.sin(angleToNode - arrowHeadAngle);
        double baseRightX = intersectX + arrowHeadLength * Math.cos(angleToNode + arrowHeadAngle);
        double baseRightY = intersectY + arrowHeadLength * Math.sin(angleToNode + arrowHeadAngle);

        arrowHead.setStrokeType(StrokeType.OUTSIDE);
        arrowHead.getPoints().setAll(
                intersectX, intersectY,
                baseLeftX, baseLeftY,
                baseRightX, baseRightY
        );

        double angleToNodeDegrees = Math.toDegrees(angleToNode) + 20.0;
        arrowHead.setRotate(angleToNodeDegrees);

        arrowHead.setTranslateX(intersectX - arrowHead.getLayoutBounds().getCenterX());
        arrowHead.setTranslateY(intersectY - arrowHead.getLayoutBounds().getCenterY());
    }


    @Override
    public void move(boolean toEdge) {
        updateObjects(toEdge);
    }

    @Override
    public void erase(MainLogic mainLogic) {
        mainLogic.getArrows().remove(this);
        super.getNodeFrom().removeArrow(this);
        super.getNodeTo().removeArrow(this);
    }
}
