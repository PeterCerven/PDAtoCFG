package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.node.MyNode;
import javafx.scene.layout.HBox;
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

    public LineArrow(MyNode from, MyNode to, Board board) {
        super(from, to, board);

        createLine();
        addSymbolContainer();
        createControlIndicator();
        createControlPoint();
        this.getChildren().addAll(line, super.getArrowHead(), controlIndicator);
        this.getChildren().addAll(super.getSymbolContainers());
        board.addObject(this);
        this.updateObjects();
    }

    private void createControlPoint() {
        LineCoordinates lineCr = getNodeEdgePoints(from.getAbsoluteCentrePosX(), from.getAbsoluteCentrePosY(),
                to.getAbsoluteCentrePosX(), to.getAbsoluteCentrePosY());
        this.controlX = (lineCr.getStartX() + lineCr.getEndX()) / 2.0;
        this.controlY = (lineCr.getStartY() + lineCr.getEndY()) / 2.0;
        this.line.setControlX(controlX);
        this.line.setControlY(controlY);
    }

    @Override
    public void updateObjects() {
        LineCoordinates lineCr = getNodeEdgePoints(from.getAbsoluteCentrePosX(), from.getAbsoluteCentrePosY(),
                to.getAbsoluteCentrePosX(), to.getAbsoluteCentrePosY());

        this.controlX = (lineCr.getStartX() + lineCr.getEndX()) / 2.0;
        this.controlY = (lineCr.getStartY() + lineCr.getEndY()) / 2.0;

        line.setStartX(lineCr.getStartX());
        line.setStartY(lineCr.getStartY());
        this.line.setControlX(controlX);
        this.line.setControlY(controlY);
        line.setEndX(lineCr.getEndX());
        line.setEndY(lineCr.getEndY());


//        log.info("Line Start X:{} Y:{}", line.getStartX(), line.getEndX());
//        log.info("Line End X:{} Y:{}", line.getStartY(), line.getEndY());

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
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();
        ArrowHeadPoints arrowHeadPoints = getArrowHeadPoints(startX, startY, endX, endY);

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

        double highestY = findHighestPointY(startX, startY, controlX, controlY, endX, endY);
        double midX = (startX + endX) / 2.0;
        double midY = highestY;

        for (HBox container : symbolContainers) {

            double offsetX = -container.getWidth() / 2.0;
            double offsetY = -container.getHeight();

            container.setLayoutX(midX + offsetX);
            container.setLayoutY(midY + offsetY);


            double angle = Math.toDegrees(Math.atan2(endY - startY, endX - startX));
            Rotate rotate = new Rotate(angle, container.getWidth() / 2.0, container.getHeight());
            container.getTransforms().clear();
            container.getTransforms().add(rotate);

        }

    }

    private double findHighestPointY(double startX, double startY, double controlX, double controlY, double endX, double endY) {
        // Calculate the t value at which the derivative of y-coordinate is zero
        double t = (startY - controlY) / (startY - 2 * controlY + endY);

        // Check if t is within the valid range
        if (t < 0 || t > 1) {
            // The highest point is one of the end points
            return Math.min(startY, endY);
        }

        // Calculate the y-coordinate of the highest point using the Bezier curve equation
        return Math.pow(1 - t, 2) * startY + 2 * (1 - t) * t * controlY + Math.pow(t, 2) * endY;
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
        LineCoordinates lineCr = getNodeEdgePoints(from.getAbsoluteCentrePosX(), from.getAbsoluteCentrePosY(),
                to.getAbsoluteCentrePosX(), to.getAbsoluteCentrePosY());
        this.controlX = (lineCr.getStartX() + lineCr.getEndX()) / 2.0;
        this.controlY = (lineCr.getStartY() + lineCr.getEndY()) / 2.0;
        moveControlPoint(controlX, controlY);
    }

    private void createControlIndicator() {
        controlIndicator = new Circle(5, Color.WHITE);
        controlIndicator.setStroke(Color.BLACK);
    }

}
