package com.example.bakalar.canvas;

import javafx.scene.Group;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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

import java.util.Optional;

@Getter
@Setter
public class Arrow extends Group {
    private static final Logger log = LogManager.getLogger(Arrow.class.getName());
    public static int ARROW_HEAD_SZIE = 20;
    private static final String LAMDA = "λ";
    private MyNode from;
    private MyNode to;
    private Line line;
    private Polygon arrowHead;
    private String read;
    private String pop;
    private String push;
    private HBox symbolContainer;


    public Arrow(MyNode from, MyNode to, Board board) {
        this.from = from;
        this.to = to;
        this.read = LAMDA;
        this.pop = LAMDA;
        this.push = LAMDA;

        createTransitions();

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
            updateSymbolContainerPosition(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        });
    }

    private void updateObjects() {
        LineCoordinates lineCr = getNodeEdgePoints(from.getAbsoluteCentrePosX(), from.getAbsoluteCentrePosY(),
                to.getAbsoluteCentrePosX(), to.getAbsoluteCentrePosY());

        line.setStartX(lineCr.getStartX());
        line.setStartY(lineCr.getStartY());
        line.setEndX(lineCr.getEndX());
        line.setEndY(lineCr.getEndY());

//        log.info("Line Start X:{} Y:{}", line.getStartX(), line.getEndX());
//        log.info("Line End X:{} Y:{}", line.getStartY(), line.getEndY());

        updateArrowHead(lineCr.startX, lineCr.startY, lineCr.getEndX(), lineCr.getEndY());
        updateSymbolContainerPosition(lineCr.startX, lineCr.startY, lineCr.getEndX(), lineCr.getEndY());
    }

    private void updateSymbolContainerPosition(double startX, double startY, double endX, double endY) {
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

    public void createTransitions() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Custom Dialog");
        dialog.setHeaderText("Enter Three Values");

        // Add buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the input fields
        TextField input1 = new TextField();
        input1.setPromptText("Read");
        TextField input2 = new TextField();
        input2.setPromptText("Pop");
        TextField input3 = new TextField();
        input3.setPromptText("Push");

        // Layout the dialog's fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(input1, 0, 0);
        grid.add(input2, 0, 1);
        grid.add(input3, 0, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                this.read = input1.getText();
                this.pop = input2.getText();
                this.push = input3.getText();
            }
        });
    }

}



