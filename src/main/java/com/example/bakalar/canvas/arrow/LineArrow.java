package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.MainLogic;
import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;
import javafx.scene.transform.Rotate;
import lombok.Getter;

import java.util.List;

import static com.example.bakalar.logic.MainLogic.NODE_RADIUS;

@Getter
public class LineArrow extends Arrow {

    private final QuadCurve line;
    private final Circle controlIndicator;
    private double controlX;
    private double controlY;
    private double startX;
    private double startY;
    private double controlPointChangeX;
    private double controlPointChangeY;
    private double endX;
    private double endY;

    public LineArrow(MyNode from, MyNode to, MainLogic mainLogic, List<TransitionInputs> transitionInputs) {
        super(from, to, mainLogic, transitionInputs);

        this.line = createLine();
        resetLine();
        this.controlIndicator = createControlIndicator();
        setLinePoints(true);

        this.getChildren().addAll(line, controlIndicator);
        this.updateObjects(true);
    }


    public LineArrow(MyNode from, MyNode to, double change, MainLogic mainLogic, List<TransitionInputs> transitionInputs) {
        this(from, to, mainLogic, transitionInputs);
        Point2D thirdPoint = getThirdPoint(change);
        this.controlPointChangeX = thirdPoint.getX() - (startX + endX) / 2.0;
        this.controlPointChangeY = thirdPoint.getY() - (startY + endY) / 2.0;
        this.updateObjects(true);
    }

    public LineArrow(MyNode from, MyNode to, double controlPointChangeX, double controlPointChangeY, MainLogic mainLogic, List<TransitionInputs> transitionInputs) {
        this(from, to, mainLogic, transitionInputs);
        this.controlPointChangeX = controlPointChangeX;
        this.controlPointChangeY = controlPointChangeY;
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
        startX = nodeFrom.getAbsoluteCentrePosX();
        startY = nodeFrom.getAbsoluteCentrePosY();
        endX = nodeTo.getAbsoluteCentrePosX();
        endY = nodeTo.getAbsoluteCentrePosY();

        if (toEdge) {
            Point2D toEdgePoint = getNodeEdgePoint(nodeTo, startX, startY);
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
        this.symbolContainers = updateStackPanePosition(this.symbolContainers);
    }

    public void moveControlPoint(double controlIndicatorPointX, double controlIndicatorPointY) {
        this.controlPointChangeX = (startX + endX) / 2.0 - controlIndicatorPointX;
        this.controlPointChangeY = (startY + endY) / 2.0 - controlIndicatorPointY;
        updateControlPointPos(this.controlPointChangeX, this.controlPointChangeY);
        updateArrowHead();
        this.symbolContainers = updateStackPanePosition(this.symbolContainers);
    }

    private void updateControlPointPos(double controlIndicatorPointX, double controlIndicatorPointY) {
        double controlXZero = (startX + endX) / 2.0 - controlIndicatorPointX;
        double controlYZero = (startY + endY) / 2.0 - controlIndicatorPointY;
        Point2D controlPoint = findControlPoint(startX, startY, controlXZero, controlYZero, endX, endY);
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
    public VBox updateStackPanePosition(VBox symbolContainers) {
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();

        Point2D highestPoint = findHighestPoint(startX, startY, controlX, controlY, endX, endY, 0.5);
        double midX = highestPoint.getX();
        double midY = highestPoint.getY();

        double offsetX = -symbolContainers.getWidth() / 2.0;
        double offsetY = -(symbolContainers.getChildren().size() * (NODE_RADIUS / 1.8)) - (NODE_RADIUS / 4.0);

        symbolContainers.setLayoutX(midX + offsetX);
        symbolContainers.setLayoutY(midY + offsetY);

        double angle = Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        if (angle > 90 && angle < 270 || angle < -90 && angle > -180) {
            angle += 180;
        }
        Rotate rotate = new Rotate(angle, symbolContainers.getWidth() / 2.0, symbolContainers.getHeight());
        symbolContainers.getTransforms().clear();
        symbolContainers.getTransforms().add(rotate);
        return symbolContainers;
    }

    private Point2D findHighestPoint(double startX, double startY, double controlX, double controlY, double endX, double endY, double t) {
        double x = (1 - t) * (1 - t) * startX + 2 * (1 - t) * t * controlX + t * t * endX;
        double y = (1 - t) * (1 - t) * startY + 2 * (1 - t) * t * controlY + t * t * endY;
        return new Point2D(x, y);
    }

    private Point2D findControlPoint(double startX, double startY, double controlIndicatorX, double controlIndicatorY, double endX, double endY) {
        double controlX = (controlIndicatorX - (1 - 0.5) * (1 - 0.5) * startX - 0.5 * 0.5 * endX) / (2 * (1 - 0.5) * 0.5);
        double controlY = (controlIndicatorY - (1 - 0.5) * (1 - 0.5) * startY - 0.5 * 0.5 * endY) / (2 * (1 - 0.5) * 0.5);
        return new Point2D(controlX, controlY);
    }

    @Override
    public void erase(MainLogic mainLogic) {
        mainLogic.getArrows().remove(this);
        this.getNodeFrom().removeArrow(this);
        this.getNodeTo().removeArrow(this);
    }

    @Override
    public void move(boolean toEdge) {
        this.updateObjects(toEdge);
    }

    private QuadCurve createLine() {
        QuadCurve line = new QuadCurve();
        line.setViewOrder(1);
        line.setStrokeWidth(NODE_RADIUS / 13.0);
        line.setMouseTransparent(true);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        return line;
    }

    public void resetControlPoint() {
        resetLine();
        updateControlPointPos(this.controlPointChangeX, this.controlPointChangeY);
        updateArrowHead();
        this.symbolContainers = updateStackPanePosition(this.symbolContainers);
    }

    private void resetLine() {
        this.startX = nodeFrom.getAbsoluteCentrePosX();
        this.startY = nodeFrom.getAbsoluteCentrePosY();

        Point2D toEdgePoint = getNodeEdgePoint(nodeTo, startX, startY);
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

    private Circle createControlIndicator() {
        Circle controlIndicator = new Circle(NODE_RADIUS / 6.0, Color.WHITE);
        controlIndicator.setStyle("-fx-cursor: hand");
        controlIndicator.setStroke(Color.BLACK);
        controlIndicator.setTranslateX(controlX);
        controlIndicator.setTranslateY(controlY);
        return controlIndicator;
    }

}
