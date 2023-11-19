package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.node.MyNode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;

public class LineArrow extends Arrow {

    private Line line;

    public LineArrow(MyNode from, MyNode to, Board board) {
        super(from, to, board);

        createLine();
        createSymbolContainer();
        this.getChildren().addAll(line, super.getArrowHead(), super.getSymbolContainer());
        board.addObject(this);
        this.updateObjects();
    }

    @Override
    public void updateObjects() {
        LineCoordinates lineCr = getNodeEdgePoints(from.getAbsoluteCentrePosX(), from.getAbsoluteCentrePosY(),
                to.getAbsoluteCentrePosX(), to.getAbsoluteCentrePosY());

        line.setStartX(lineCr.getStartX());
        line.setStartY(lineCr.getStartY());
        line.setEndX(lineCr.getEndX());
        line.setEndY(lineCr.getEndY());

//        log.info("Line Start X:{} Y:{}", line.getStartX(), line.getEndX());
//        log.info("Line End X:{} Y:{}", line.getStartY(), line.getEndY());

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

        double midX = (startX + endX) / 2.0;
        double midY = (startY + endY) / 2.0;

        // Adjust position to place container above the line
        double offsetX = -symbolContainer.getWidth() / 2.0;
        double offsetY = -symbolContainer.getHeight(); // 10 is the offset above the line

        symbolContainer.setLayoutX(midX + offsetX);
        symbolContainer.setLayoutY(midY + offsetY);

        // Calculate the angle for rotation
        double angle = Math.toDegrees(Math.atan2(endY - startY, endX - startX));

        // Rotate around the center of the container
        Rotate rotate = new Rotate(angle, symbolContainer.getWidth() / 2.0, symbolContainer.getHeight());
        symbolContainer.getTransforms().clear();
        symbolContainer.getTransforms().add(rotate);
    }

    @Override
    public void move() {
        this.updateObjects();
    }

    private void createLine() {
        line = new Line();
        line.setStrokeType(StrokeType.OUTSIDE);
        line.setStrokeWidth(2);
        line.setStroke(Color.BLACK);
    }

}
