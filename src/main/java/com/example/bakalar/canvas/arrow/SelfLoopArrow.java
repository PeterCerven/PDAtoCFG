package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.Board;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeType;
import lombok.Getter;

import java.util.List;

import static com.example.bakalar.logic.Board.NODE_RADIUS;

@Getter
public class SelfLoopArrow extends Arrow {

    public static double ARC_WIDTH = NODE_RADIUS / 2.0;
    public static double ARC_HEIGHT = (NODE_RADIUS / 2.0) * 3;
    public static double ARC_START_ANGLE = 20;
    public static int ARC_LENGTH = 140;
    private Arc arc;


    public SelfLoopArrow(MyNode from, MyNode to, Board board, List<TransitionInputs> transitionInputs, Long arrowId) {
        super(from, to, board, transitionInputs, arrowId);

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
        arc.setCenterX(from.getAbsoluteCentrePosX());
        arc.setCenterY(from.getAbsoluteCentrePosY());
        arc.setRadiusX(from.getCircle().getRadius() - ARC_WIDTH);
        arc.setRadiusY(from.getCircle().getRadius() + ARC_HEIGHT);

        updateArrowHead();
        this.symbolContainers = updateStackPanePosition(this.symbolContainers);
    }

    @Override
    public VBox updateStackPanePosition(VBox symbolContainers) {
        double midX = from.getAbsoluteCentrePosX();
        double midY = from.getAbsoluteCentrePosY() - (from.getCircle().getRadius() + ARC_HEIGHT);

        double offsetX = -symbolContainers.getWidth() / 2.0;
        double offsetY = -(symbolContainers.getChildren().size() * 20);

        symbolContainers.setLayoutX(midX + offsetX);
        symbolContainers.setLayoutY(midY + offsetY);
        return symbolContainers;
    }

    @Override
    public void updateArrowHead() {
        double startAngleRadians = Math.toRadians(ARC_START_ANGLE);

        double startX = arc.getCenterX() + arc.getRadiusX() * Math.cos(startAngleRadians);
        double startY = arc.getCenterY() + arc.getRadiusY() * Math.sin(startAngleRadians);

        double arrowHeadLength = 4;
        double arrowHeadAngle = Math.toRadians(30);

        double angleToNode = Math.atan2(from.getAbsoluteCentrePosY() - startY, from.getAbsoluteCentrePosX() - startX);

        double radius = from.getCircle().getRadius();
        double intersectX = from.getAbsoluteCentrePosX() + (radius + 2) * Math.cos(angleToNode);
        double intersectY = from.getAbsoluteCentrePosY() + (radius + 4) * Math.sin(angleToNode);


        double baseLeftX = intersectX + arrowHeadLength * Math.cos(angleToNode - arrowHeadAngle);
        double baseLeftY = intersectY + arrowHeadLength * Math.sin(angleToNode - arrowHeadAngle);
        double baseRightX = intersectX + arrowHeadLength * Math.cos(angleToNode + arrowHeadAngle);
        double baseRightY = intersectY + arrowHeadLength * Math.sin(angleToNode + arrowHeadAngle);

        arrowHead = super.getArrowHead();
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
    public void erase(Board board) {
        board.getArrows().remove(this);
        this.getFrom().removeArrow(this);
        this.getTo().removeArrow(this);
    }
}
