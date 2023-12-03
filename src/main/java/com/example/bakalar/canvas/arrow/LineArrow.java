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

    private void setLinePoints(boolean toEdge) {
        double fromCentreX = from.getAbsoluteCentrePosX();
        double fromCentreY = from.getAbsoluteCentrePosY();
        double toCentreX = to.getAbsoluteCentrePosX();
        double toCentreY = to.getAbsoluteCentrePosY();

        if (toEdge) {
            Point2D toEdgePoint = getNodeEdgePoint(to, fromCentreX, fromCentreY);
            endX = toEdgePoint.getX();
            endY = toEdgePoint.getY();

        } else {
            endX = toCentreX;
            endY = toCentreY;
        }


        startX = fromCentreX;
        startY = fromCentreY;


        this.controlX = (startX + endX) / 2.0;
        this.controlY = (startY + endY) / 2.0;
        line.setControlX(controlX);
        line.setControlY(controlY);

        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
    }

    @Override
    public void updateObjects(boolean toEdge) {
        setLinePoints(toEdge);
        updateControlIndicator(controlX, controlY);
        updateArrowHead();
        updateSymbolContainerPosition();

    }

    private void updateControlIndicator(double controlX, double controlY) {
        this.controlIndicator.setTranslateX(controlX);
        this.controlIndicator.setTranslateY(controlY);
    }

    public void moveControlPoint(double controlIndicatorPointX, double controlIndicatorPointY) {
        Point2D controlPoint = findControlPoint(startX, startY, controlIndicatorPointX, controlIndicatorPointY, endX, endY, 0.5);
        this.controlX = controlPoint.getX();
        this.controlY = controlPoint.getY();
        updateControlIndicator(controlIndicatorPointX, controlIndicatorPointY);
        this.line.setControlX(controlX);
        this.line.setControlY(controlY);
        updateArrowHead();
        updateSymbolContainerPosition();
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
    }

    public void resetControlPoint() {
        this.controlX = (startX + endX) / 2.0;
        this.controlY = (startX + endX) / 2.0;
        moveControlPoint(controlX, controlY);
    }

    private void createControlIndicator() {
        controlIndicator = new Circle(5, Color.WHITE);
        controlIndicator.setStroke(Color.BLACK);
    }

}
