package com.example.bakalar.canvas;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@Setter
public class Arrow extends Line {
    private static final Logger log = LogManager.getLogger(Arrow.class.getName());
    public static int ARROW_HEAD_SZIE = 20;
    private MyNode from;
    private MyNode to;
    private Polygon arrowHead;


    public Arrow(MyNode from, MyNode to, Board board) {
        super();
        LineCoordinates lineCr = getNodeEdgePoints(from.getAbsoluteCentrePosX(), from.getAbsoluteCentrePosY(),
                to.getAbsoluteCentrePosX(), to.getAbsoluteCentrePosY());
        super.setStartX(lineCr.getStartX());
        super.setStartY(lineCr.getStartY());
        super.setEndX(lineCr.getEndX());
        super.setEndY(lineCr.getEndY());
        this.setStrokeType(StrokeType.OUTSIDE);
        this.setStrokeWidth(2);
        this.setStroke(Color.BLACK);
        this.from = from;
        this.to = to;
        arrowHead = createArrowhead(lineCr.startX, lineCr.startY, lineCr.getEndX(), lineCr.getEndY());
        board.addObject(this);
        board.addObject(arrowHead);
    }

    private LineCoordinates getNodeEdgePoints(double startX, double startY, double endX, double endY) {
        log.info("Start X:{} Y:{}", startX, startY);
        log.info("End X:{} Y:{}", endX, endY);
        double side1 = startX - endX;
        double side2 = startY - endY;

        double angle1 = Math.atan(side1 / side2);

        double newDiffX = Math.sin(angle1) * (double) MainController.NODE_RADIUS;
        double newDiffY = Math.cos(angle1) * (double) MainController.NODE_RADIUS;

        if (startX > endX && startY > endY || startX < endX && startY > endY) {
            newDiffX = -newDiffX;
            newDiffY = -newDiffY;
        }

        return new LineCoordinates(startX + newDiffX, startY + newDiffY, endX - newDiffX, endY - newDiffY);
    }

    private ArrowHeadPoints getArrowHeadPoints(double startX, double startY, double endX, double endY) {
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        //point1
        double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SZIE + endX;
        double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SZIE + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SZIE + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SZIE + endY;
        return new ArrowHeadPoints(endX, endY, x1, y1, x2, y2);
    }

    private Polygon createArrowhead(double startX, double startY, double endX, double endY) {
        Polygon arrowhead = new Polygon();
        ArrowHeadPoints arrowHeadPoints = getArrowHeadPoints(startX, startY, endX, endY);

        arrowhead.setStroke(Color.BLUE);
        arrowhead.setStrokeWidth(5);
        arrowhead.getPoints().addAll(
                endX, endY,
                arrowHeadPoints.getSecondPointX(), arrowHeadPoints.getSecondPointY(),
                arrowHeadPoints.getThirdPointX(), arrowHeadPoints.getThirdPointY()
        );
        return arrowhead;
    }


    public void move() {
        // move arrow
        LineCoordinates lineCoordinates = getNodeEdgePoints(this.from.getAbsoluteCentrePosX(), this.from.getAbsoluteCentrePosY(),
                this.to.getAbsoluteCentrePosX(), this.to.getAbsoluteCentrePosY());
        super.setStartX(lineCoordinates.getStartX());
        super.setStartY(lineCoordinates.getStartY());
        super.setEndX(lineCoordinates.getEndX());
        super.setEndY(lineCoordinates.getEndY());

        // move arrow head
        ArrowHeadPoints arrowHeadPoints = getArrowHeadPoints(lineCoordinates.getStartX(), lineCoordinates.getStartY(),
                lineCoordinates.getEndX(), lineCoordinates.getEndY());
        this.arrowHead.getPoints().set(0, arrowHeadPoints.getFirstPointX());
        this.arrowHead.getPoints().set(1, arrowHeadPoints.getFirstPointY());
        this.arrowHead.getPoints().set(2, arrowHeadPoints.getSecondPointX());
        this.arrowHead.getPoints().set(3, arrowHeadPoints.getSecondPointY());
        this.arrowHead.getPoints().set(4, arrowHeadPoints.getThirdPointX());
        this.arrowHead.getPoints().set(5, arrowHeadPoints.getThirdPointY());
    }


}
