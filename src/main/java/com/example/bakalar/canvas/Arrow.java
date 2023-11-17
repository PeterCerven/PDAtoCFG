package com.example.bakalar.canvas;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@Setter
public class Arrow extends Group {
    private static final Logger log = LogManager.getLogger(Arrow.class.getName());
    public static int ARROW_HEAD_SZIE = 20;
    private MyNode from;
    private MyNode to;
    private Line line;
    private Polygon arrowHead;
    private String read;
    private String pop;
    private String push;
    private HBox symbolContainer;


    public Arrow(MyNode from, MyNode to, Board board, String read, String pop, String push) {
        this.from = from;
        this.to = to;
        this.read = read;
        this.pop = pop;
        this.push = push;

        createLine();
        createArrowHead();
        createSymbolContainer();

        this.getChildren().addAll(line, arrowHead, symbolContainer);
        board.addObject(this);
        updateObjects();
    }

    private void createLine() {
        line = new Line();
        line.setStrokeType(StrokeType.OUTSIDE);
        line.setStrokeWidth(2);
        line.setStroke(Color.BLACK);
    }

    private void createArrowHead() {
        arrowHead = new Polygon();
        arrowHead.setStroke(Color.BLUE);
        arrowHead.setStrokeWidth(5);
    }

    private void createSymbolContainer() {
        symbolContainer = new HBox(5);
        Text readSymbol = new Text(read);
        Text popSymbol = new Text(pop);
        Text pushSymbol = new Text(push);
        symbolContainer.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
        symbolContainer.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            symbolContainer.setLayoutX(newValue.getWidth());
            symbolContainer.setLayoutX(newValue.getHeight());
            updateSymbolContainerPosition();
        });
    }

    private void updateObjects() {
        LineCoordinates lineCr = getNodeEdgePoints(from.getAbsoluteCentrePosX(), from.getAbsoluteCentrePosY(),
                to.getAbsoluteCentrePosX(), to.getAbsoluteCentrePosY());

        line.setStartX(lineCr.getStartX());
        line.setStartY(lineCr.getStartY());
        line.setEndX(lineCr.getEndX());
        line.setEndY(lineCr.getEndY());

        log.info("Line Start X:{} Y:{}", line.getStartX(), line.getEndX());
        log.info("Line End X:{} Y:{}", line.getStartY(), line.getEndY());

        updateArrowHead(lineCr.startX, lineCr.startY, lineCr.getEndX(), lineCr.getEndY());
        updateSymbolContainerPosition();
    }

    private void updateSymbolContainerPosition() {
        double midX = (line.getStartX() + line.getEndX()) / 2.0;
        double midY = (line.getStartY() + line.getEndY()) / 2.0;

        // Adjust position to place container above the line
        symbolContainer.setLayoutX(midX - symbolContainer.getWidth() / 2.0);
        symbolContainer.setLayoutY(midY - symbolContainer.getHeight()); // 10 is the offset above the line

        log.info("Symbol X:{} Y:{}", symbolContainer.getLayoutX(), symbolContainer.getLayoutY());
        // Calculate the angle for rotation
        double angle = Math.toDegrees(Math.atan2(line.getEndY() - line.getStartY(),
                line.getEndX() - line.getStartX()));

        Rotate rotate = new Rotate(angle, 0, 0); // Rotate around the top-left corner of the container
        symbolContainer.getTransforms().clear(); // Clear any previous transformations
        symbolContainer.getTransforms().add(rotate);
    }

    private LineCoordinates getNodeEdgePoints(double startX, double startY, double endX, double endY) {
//        log.info("Start X:{} Y:{}", startX, startY);
//        log.info("End X:{} Y:{}", endX, endY);
        double side1 = startX - endX;
        double side2 = startY - endY;

        double angle1 = Math.atan(side1 / side2);

        double newDiffX = Math.sin(angle1) * (double) MainController.NODE_RADIUS;
        double newDiffY = Math.cos(angle1) * (double) MainController.NODE_RADIUS;

        if (startX >= endX && startY >= endY || startX < endX && startY >= endY) {
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


    private void updateArrowHead(double startX, double startY, double endX, double endY) {
        ArrowHeadPoints arrowHeadPoints = getArrowHeadPoints(startX, startY, endX, endY);

        arrowHead.getPoints().setAll(
                endX, endY,
                arrowHeadPoints.getSecondPointX(), arrowHeadPoints.getSecondPointY(),
                arrowHeadPoints.getThirdPointX(), arrowHeadPoints.getThirdPointY()
        );
    }


    public void move() {
        updateObjects();
    }


}
