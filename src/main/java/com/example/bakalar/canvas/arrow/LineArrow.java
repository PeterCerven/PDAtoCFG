package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.node.MyNode;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;
import lombok.Getter;

@Getter
public class LineArrow extends Arrow {

    private QuadCurve line;
    private Circle controlIndicator;
    private double controlX;
    private double controlY;
    private double startX;
    private double startY;
    private double controlPointChangeX;
    private double controlPointChangeY;
    private double endX;
    private double endY;

    public LineArrow(MyNode from, MyNode to) {
        super(from, to);

        createLine();
        createControlIndicator();
        setLinePoints(true);

        this.getChildren().addAll(line, controlIndicator);
        this.updateObjects(true);
    }

    public LineArrow(MyNode from, MyNode to, double change) {
        this(from, to);
        Point2D thirdPoint = getThirdPoint(change);
        this.controlPointChangeX = thirdPoint.getX() - (startX + endX) / 2.0;
        this.controlPointChangeY = thirdPoint.getY() - (startY + endY) / 2.0;
        this.updateObjects(true);
    }

    private Point2D getThirdPoint(double change) {
        double midX = (startX + endX) / 2.0;
        double midY = (startY + endY) / 2.0;

        double vectorX = endX - startX;
        double vectorY = endY - startY;

        double vectorLength = Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        double vectorXNormal = vectorX / vectorLength;
        double vectorYNormal = vectorY / vectorLength;

        double pointX = midX + change * vectorYNormal;
        double pointY = midY - change * vectorXNormal;
        return new Point2D(pointX, pointY);
    }

    private void setLinePoints(boolean toEdge) {
        startX = from.getAbsoluteCentrePosX();
        startY = from.getAbsoluteCentrePosY();
        endX = to.getAbsoluteCentrePosX();
        endY = to.getAbsoluteCentrePosY();

        if (toEdge) {
            Point2D toEdgePoint = getNodeEdgePoint(to, startX, startY);
            endX = toEdgePoint.getX();
            endY = toEdgePoint.getY();
        }
        line.setStartX(startX);
        line.setStartY(startY);

        this.controlX = (startX + endX) / 2.0 + controlPointChangeX;
        this.controlY = (startY + endY) / 2.0 + controlPointChangeY;

        line.setControlX(controlX);
        line.setControlY(controlY);

        line.setEndX(endX);
        line.setEndY(endY);
    }

    @Override
    public void updateObjects(boolean toEdge) {
        setLinePoints(toEdge);
        updateControlPointPos(this.controlPointChangeX, this.controlPointChangeY);
        updateArrowHead();
        updateSymbolContainerPosition();
    }

    public void moveControlPoint(double controlIndicatorPointX, double controlIndicatorPointY) {
        this.controlPointChangeX = (startX + endX) / 2.0 - controlIndicatorPointX;
        this.controlPointChangeY = (startY + endY) / 2.0 - controlIndicatorPointY;
        updateControlPointPos(this.controlPointChangeX, this.controlPointChangeY);
        updateArrowHead();
        updateSymbolContainerPosition();
    }

    private void updateControlPointPos(double controlIndicatorPointX, double controlIndicatorPointY) {
        double controlXZero = (startX + endX) / 2.0 - controlIndicatorPointX;
        double controlYZero = (startY + endY) / 2.0 - controlIndicatorPointY;
        Point2D controlPoint = findControlPoint(startX, startY, controlXZero, controlYZero, endX, endY, 0.5);
        this.controlX = controlPoint.getX();
        this.controlY = controlPoint.getY();
        this.controlIndicator.setTranslateX(controlXZero);
        this.controlIndicator.setTranslateY(controlYZero);
        this.line.setControlX(controlX);
        this.line.setControlY(controlY);
    }

    @Override
    public void updateArrowHead() {
        Point2D highestPoint = findHighestPoint(startX, startY, controlX, controlY, endX, endY, 0.9);
        double endXAngled = highestPoint.getX();
        double endYAngled = highestPoint.getY();
        ArrowHeadPoints arrowHeadPoints = getArrowHeadPoints(endX, endY, endXAngled, endYAngled);

        arrowHead.getPoints().setAll(
                endX, endY,
                arrowHeadPoints.getSecondPointX(), arrowHeadPoints.getSecondPointY(),
                arrowHeadPoints.getThirdPointX(), arrowHeadPoints.getThirdPointY()
        );
    }

    @Override
    public void updateSymbolContainerPosition() {
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();

        Point2D highestPoint = findHighestPoint(startX, startY, controlX, controlY, endX, endY, 0.5);
        double midX = highestPoint.getX();
        double midY = highestPoint.getY();

        double offsetX = -symbolContainers.getWidth() / 2.0;
        double offsetY = -symbolContainers.getHeight();

        symbolContainers.setLayoutX(midX + offsetX);
        symbolContainers.setLayoutY(midY + offsetY);

        double angle = Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        if (angle > 90 && angle < 270 || angle < -90 && angle > -180) {
            angle += 180;
        }
        Rotate rotate = new Rotate(angle, symbolContainers.getWidth() / 2.0, symbolContainers.getHeight());
        symbolContainers.getTransforms().clear();
        symbolContainers.getTransforms().add(rotate);
    }

    private Point2D findHighestPoint(double startX, double startY, double controlX, double controlY, double endX, double endY, double t) {
        double x = (1 - t) * (1 - t) * startX + 2 * (1 - t) * t * controlX + t * t * endX;
        double y = (1 - t) * (1 - t) * startY + 2 * (1 - t) * t * controlY + t * t * endY;
        return new Point2D(x, y);
    }

    private Point2D findControlPoint(double startX, double startY, double controlIndicatorX, double controlIndicatorY, double endX, double endY, double t) {
        double controlX = (controlIndicatorX - (1 - t) * (1 - t) * startX - t * t * endX) / (2 * (1 - t) * t);
        double controlY = (controlIndicatorY - (1 - t) * (1 - t) * startY - t * t * endY) / (2 * (1 - t) * t);
        return new Point2D(controlX, controlY);
    }


    @Override
    public void move(boolean toEdge) {
        this.updateObjects(toEdge);
    }

    private void createLine() {
        line = new QuadCurve();
        line.setStrokeType(StrokeType.OUTSIDE);
        line.setStrokeWidth(2);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        resetLine();
    }

    public void resetControlPoint() {
        resetLine();
        updateControlPointPos(this.controlPointChangeX, this.controlPointChangeY);
        updateArrowHead();
        updateSymbolContainerPosition();
    }

    private void resetLine() {
        this.startX = from.getAbsoluteCentrePosX();
        this.startY = from.getAbsoluteCentrePosY();

        Point2D toEdgePoint = getNodeEdgePoint(to, startX, startY);
        endX = toEdgePoint.getX();
        endY = toEdgePoint.getY();

        this.controlX = (startX + endX) / 2.0;
        this.controlY = (startY + endY) / 2.0;
        this.controlPointChangeX = 0;
        this.controlPointChangeY = 0;
        line.setControlX(controlX);
        line.setControlY(controlY);

        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
    }

    private Point2D getDifference(double x1, double y1, double x2, double y2) {
        return new Point2D(x2 - x1, y2 - y1);
    }

    private void createControlIndicator() {
        controlIndicator = new Circle(5, Color.WHITE);
        controlIndicator.setStroke(Color.BLACK);
        controlIndicator.setTranslateX(controlX);
        controlIndicator.setTranslateY(controlY);
    }

}
