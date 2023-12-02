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
        setLinePoints();
        this.getChildren().addAll(line, controlIndicator);
        this.updateObjects();
    }

    private void setLinePoints() {
        startX = from.getAbsoluteCentrePosX();
        startY = from.getAbsoluteCentrePosY();
        endX = to.getAbsoluteCentrePosX();
        endY = to.getAbsoluteCentrePosY();

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
    public void updateObjects() {
        setLinePoints();
        updateControlIndicator(controlX, controlY);
        updateArrowHead();
        updateSymbolContainerPosition();

    }

    private void updateControlIndicator(double controlX, double controlY) {
        this.controlIndicator.setTranslateX(controlX);
        this.controlIndicator.setTranslateY(controlY);
    }

    public void moveControlPoint(double controlX, double controlY) {
        updateControlIndicator(controlX, controlY);
        this.controlX = controlX;
        this.controlY = controlY;
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
        Rotate rotate = new Rotate(angle, symbolContainers.getWidth() / 2.0, symbolContainers.getHeight());
        symbolContainers.getTransforms().clear();
        symbolContainers.getTransforms().add(rotate);
    }

    private Point2D findHighestPoint(double startX, double startY, double controlX, double controlY, double endX, double endY, double t) {
        double x = (1 - t) * (1 - t) * startX + 2 * (1 - t) * t * controlX + t * t * endX;
        double y = (1 - t) * (1 - t) * startY + 2 * (1 - t) * t * controlY + t * t * endY;
        return new Point2D(x, y);
    }


    @Override
    public void move() {
        this.updateObjects();
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
