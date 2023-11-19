package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.node.MyNode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class SelfLoopArrow extends Arrow {

    public static int ARC_WIDTH = 12;
    public static int ARC_HEIGHT = 45;
    public static int ARC_START_ANGLE = 20;
    public static int ARC_LENGTH = 140;
    private Arc arc;
    public SelfLoopArrow(MyNode from, MyNode to, Board board) {
        super(from, to, board);

        createArc();
        createSymbolContainer();
        this.getChildren().addAll(arc, super.getArrowHead(), super.getSymbolContainer());
        board.addObject(this);
        this.updateObjects();
    }

    private void createArc() {
        arc = new Arc();
        arc.setStroke(Color.BLACK);
        arc.setFill(Color.WHITE);
        arc.setStrokeType(StrokeType.OUTSIDE);
        arc.setStrokeWidth(2);
    }

    @Override
    public void updateObjects() {
        arc.setCenterX(from.getAbsoluteCentrePosX());
        arc.setCenterY(from.getAbsoluteCentrePosY());
        arc.setRadiusX(from.getCircle().getRadius() - ARC_WIDTH);
        arc.setRadiusY(from.getCircle().getRadius() + ARC_HEIGHT);
        arc.setStartAngle(ARC_START_ANGLE);
        arc.setLength(ARC_LENGTH);
        arc.setType(ArcType.OPEN);



//        log.info("Line Start X:{} Y:{}", line.getStartX(), line.getEndX());
//        log.info("Line End X:{} Y:{}", line.getStartY(), line.getEndY());

        updateArrowHead();
        updateSymbolContainerPosition();
    }

    @Override
    public void updateSymbolContainerPosition() {

        double midX = from.getAbsoluteCentrePosX();
        double midY = from.getAbsoluteCentrePosY() - (from.getCircle().getRadius() + ARC_HEIGHT);

        // Adjust position to place container above the line
        double offsetX = -symbolContainer.getWidth() / 2.0;
        double offsetY = -symbolContainer.getHeight(); // 10 is the offset above the line

        symbolContainer.setLayoutX(midX + offsetX);
        symbolContainer.setLayoutY(midY + offsetY);
    }

    @Override
    public void updateArrowHead() {
        double startAngleRadians = Math.toRadians(ARC_START_ANGLE);

        // Calculate the position of the start of the arc
        double startX = arc.getCenterX() + arc.getRadiusX() * Math.cos(startAngleRadians);
        double startY = arc.getCenterY() + arc.getRadiusY() * Math.sin(startAngleRadians);

        // Arrowhead size parameters
        double arrowHeadLength = 8; // Length of the arrowhead sides
        double arrowHeadAngle = Math.toRadians(30); // Angle at the arrowhead tip

        // Calculate the angle towards the node from the start of the arc
        double angleToNode = Math.atan2(from.getAbsoluteCentrePosY() - startY, from.getAbsoluteCentrePosX() - startX);

        // Calculate the intersection point with the node's circumference
        double radius = from.getCircle().getRadius();
        double intersectX = from.getAbsoluteCentrePosX() + radius * Math.cos(angleToNode);
        double intersectY = from.getAbsoluteCentrePosY() + radius * Math.sin(angleToNode);

        // Adjust the arrowhead position to the intersection point

        // Calculate the base points of the arrowhead
        double baseLeftX = intersectX + arrowHeadLength * Math.cos(angleToNode - arrowHeadAngle);
        double baseLeftY = intersectY + arrowHeadLength * Math.sin(angleToNode - arrowHeadAngle);
        double baseRightX = intersectX + arrowHeadLength * Math.cos(angleToNode + arrowHeadAngle);
        double baseRightY = intersectY + arrowHeadLength * Math.sin(angleToNode + arrowHeadAngle);

        // Update the arrowhead polygon points
        arrowHead = super.getArrowHead();
        arrowHead.getPoints().setAll(
                intersectX, intersectY,            // Tip of the arrowhead (intersection point)
                baseLeftX, baseLeftY,  // Left base point
                baseRightX, baseRightY // Right base point
        );

        double angleToNodeDegrees = Math.toDegrees(angleToNode) - 90;
        arrowHead.setRotate(angleToNodeDegrees);

        // Translate the arrowhead to the intersection position
        arrowHead.setTranslateX(intersectX - arrowHead.getLayoutBounds().getCenterX());
        arrowHead.setTranslateY(intersectY - arrowHead.getLayoutBounds().getCenterY());
    }


    @Override
    public void move() {
        updateObjects();
    }
}
