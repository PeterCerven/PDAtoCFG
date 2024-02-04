package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
import com.example.bakalar.logic.Board;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeType;

import static com.example.bakalar.logic.MainController.NODE_RADIUS;

public class SelfLoopArrow extends Arrow implements Cloneable {

    public static double ARC_WIDTH = NODE_RADIUS / 2.0;
    public static double ARC_HEIGHT = (NODE_RADIUS / 2.0) * 3;
    public static double ARC_START_ANGLE = 20;
    public static int ARC_LENGTH = 140;
    private Arc arc;

    public SelfLoopArrow(MyNode from, MyNode to, Board board, NodeTransition nodeTransition) {
        super(from, to, board, nodeTransition);

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
        arc.setStrokeWidth(2);
    }

    @Override
    public SelfLoopArrow clone() {
        SelfLoopArrow cloned = (SelfLoopArrow) super.clone();

        // Clone the Arc object
        cloned.arc = new Arc();
        cloned.arc.setCenterX(this.arc.getCenterX());
        cloned.arc.setCenterY(this.arc.getCenterY());
        cloned.arc.setRadiusX(this.arc.getRadiusX());
        cloned.arc.setRadiusY(this.arc.getRadiusY());
        cloned.arc.setStartAngle(this.arc.getStartAngle());

        // Add the cloned Arc to the children of the cloned SelfLoopArrow
        cloned.getChildren().add(cloned.arc);

        return cloned;
    }

    @Override
    public void updateObjects(boolean toEdge) {
        arc.setCenterX(from.getAbsoluteCentrePosX());
        arc.setCenterY(from.getAbsoluteCentrePosY());
        arc.setRadiusX(from.getCircle().getRadius() - ARC_WIDTH);
        arc.setRadiusY(from.getCircle().getRadius() + ARC_HEIGHT);

        updateArrowHead();
        updateSymbolContainerPosition();
    }

    @Override
    public void updateSymbolContainerPosition() {
        double midX = from.getAbsoluteCentrePosX();
        double midY = from.getAbsoluteCentrePosY() - (from.getCircle().getRadius() + ARC_HEIGHT);

        // Adjust position to place container above the line
        double offsetX = -symbolContainers.getWidth() / 2.0;
        double offsetY = -symbolContainers.getHeight(); // 10 is the offset above the line

        symbolContainers.setLayoutX(midX + offsetX);
        symbolContainers.setLayoutY(midY + offsetY);
    }

    @Override
    public void updateArrowHead() {
        double startAngleRadians = Math.toRadians(ARC_START_ANGLE);

        // Calculate the position of the start of the arc
        double startX = arc.getCenterX() + arc.getRadiusX() * Math.cos(startAngleRadians);
        double startY = arc.getCenterY() + arc.getRadiusY() * Math.sin(startAngleRadians);

        // Arrowhead size parameters
        double arrowHeadLength = 4; // Length of the arrowhead sides
        double arrowHeadAngle = Math.toRadians(30); // Angle at the arrowhead tip

        // Calculate the angle towards the node from the start of the arc
        double angleToNode = Math.atan2(from.getAbsoluteCentrePosY() - startY, from.getAbsoluteCentrePosX() - startX);

        // Calculate the intersection point with the node's circumference
        double radius = from.getCircle().getRadius();
        double intersectX = from.getAbsoluteCentrePosX() + (radius + 2) * Math.cos(angleToNode);
        double intersectY = from.getAbsoluteCentrePosY() + (radius + 4) * Math.sin(angleToNode);

        // Adjust the arrowhead position to the intersection point

        // Calculate the base points of the arrowhead
        double baseLeftX = intersectX + arrowHeadLength * Math.cos(angleToNode - arrowHeadAngle);
        double baseLeftY = intersectY + arrowHeadLength * Math.sin(angleToNode - arrowHeadAngle);
        double baseRightX = intersectX + arrowHeadLength * Math.cos(angleToNode + arrowHeadAngle);
        double baseRightY = intersectY + arrowHeadLength * Math.sin(angleToNode + arrowHeadAngle);

        // Update the arrowhead polygon points
        arrowHead = super.getArrowHead();
        arrowHead.setStrokeType(StrokeType.OUTSIDE);
        arrowHead.getPoints().setAll(
                intersectX, intersectY,            // Tip of the arrowhead (intersection point)
                baseLeftX, baseLeftY,  // Left base point
                baseRightX, baseRightY // Right base point
        );

        double angleToNodeDegrees = Math.toDegrees(angleToNode) + 20.0;
        arrowHead.setRotate(angleToNodeDegrees);

        // Translate the arrowhead to the intersection position
        arrowHead.setTranslateX(intersectX - arrowHead.getLayoutBounds().getCenterX());
        arrowHead.setTranslateY(intersectY - arrowHead.getLayoutBounds().getCenterY());
    }


    @Override
    public void move(boolean toEdge) {
        updateObjects(toEdge);
    }
}
