package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.MainController;
import com.example.bakalar.canvas.node.MyNode;
import javafx.scene.Group;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Getter
@Setter
public abstract class Arrow extends Group {
    public static final int ARROW_HEAD_SZIE = 15;
    public static final String LAMDA = "λ";
    protected static final Logger log = LogManager.getLogger(Arrow.class.getName());
    protected MyNode from;
    protected MyNode to;
    protected String read;
    protected String pop;
    protected String push;
    protected VBox symbolContainers;
    protected Polygon arrowHead;
    private Board board;


    public Arrow(MyNode from, MyNode to) {
        this.from = from;
        this.to = to;
        this.read = LAMDA;
        this.pop = LAMDA;
        this.push = LAMDA;
        this.symbolContainers = new VBox();
        this.setViewOrder(-1);
        createArrowHead();
        addSymbolContainer();
        this.getChildren().addAll(arrowHead, symbolContainers);
    }


    private void createArrowHead() {
        arrowHead = new Polygon();
        arrowHead.setStroke(Color.BLUE);
        arrowHead.setStrokeWidth(3);
        arrowHead.setFill(Color.BLUE);
        arrowHead.setViewOrder(-1);
    }

    public void addSymbolContainer() {
        HBox container = new HBox(5);
        Text readSymbol = new Text(read);
        Text popSymbol = new Text(pop);
        Text pushSymbol = new Text(push);
        container.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
        container.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                createTransitions(container);
            }
        });
        container.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            container.setLayoutX(newValue.getWidth());
            container.setLayoutX(newValue.getHeight());
            updateSymbolContainerPosition();
        });
        this.symbolContainers.getChildren().add(container);
        createTransitions(container);
    }

    public abstract void updateObjects();

    public abstract void updateSymbolContainerPosition();


    protected LineCoordinates getNodeEdgePoints(double startX, double startY, double endX, double endY) {
//        log.info("Start X:{} Y:{}", startX, startY);
//        log.info("End X:{} Y:{}", endX, endY);
        double side1 = startX - endX;
        double side2 = startY - endY;

        double angle1 = Math.atan(side1 / side2);

        double newDiffX = Math.sin(angle1) * (double) (MainController.NODE_RADIUS - 3);
        double newDiffY = Math.cos(angle1) * (double) (MainController.NODE_RADIUS - 3);

        if (startX >= endX && startY >= endY || startX < endX && startY >= endY) {
            newDiffX = -newDiffX;
            newDiffY = -newDiffY;
        }

        return new LineCoordinates(startX + newDiffX, startY + newDiffY, endX - newDiffX, endY - newDiffY);
    }

    protected ArrowHeadPoints getArrowHeadPoints(double startX, double startY, double endX, double endY, double endXAngled, double endYAngled) {
        double angle = Math.atan2((endY - endYAngled), (endX - endXAngled)) - Math.PI / 2.0;
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

    public abstract void updateArrowHead();


    public abstract void move();

    public void createTransitions(HBox container) {
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
                this.read = input1.getText().isBlank() ? LAMDA : input1.getText();
                this.pop = input2.getText().isBlank() ? LAMDA : input2.getText();
                this.push = input3.getText().isBlank() ? LAMDA : input3.getText();

                if (container != null) {
                    container.getChildren().setAll(new Text(read), new Text(pop), new Text(push));
                }
            }
        });
    }

}



