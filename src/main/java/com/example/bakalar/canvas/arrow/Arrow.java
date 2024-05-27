package com.example.bakalar.canvas.arrow;

import com.example.bakalar.canvas.MyObject;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.Board;
import com.example.bakalar.logic.utility.ButtonState;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.example.bakalar.logic.Board.NODE_RADIUS;

@Getter
@Setter
public abstract class Arrow extends MyObject {
    protected MyNode from;
    protected MyNode to;
    protected List<TransitionInputs> transitions;
    protected VBox symbolContainers;
    protected Polygon arrowHead;
    protected Board board;
    protected Long fromId;
    protected Long toId;


    public Arrow(MyNode from, MyNode to, Board board, List<TransitionInputs> transitionInputs, Long ID) {
        super(ID);
        this.transitions = new ArrayList<>();
        this.from = from;
        this.to = to;
        this.fromId = from.getID();
        this.toId = to.getID();
        this.board = board;


        this.symbolContainers = new VBox();
        VBox.setVgrow(symbolContainers, Priority.ALWAYS);
        symbolContainers.setAlignment(Pos.BOTTOM_CENTER);


        StackPane.setAlignment(symbolContainers, Pos.BOTTOM_CENTER);
        symbolContainers.setViewOrder(1);
        this.setViewOrder(1);
        this.arrowHead = createArrowHead();
        for (TransitionInputs transition : transitionInputs) {
            addSymbolContainer(transition);
        }
        this.getChildren().addAll(arrowHead, symbolContainers);
    }


    private Polygon createArrowHead() {
        Polygon arrowHead = new Polygon();
        arrowHead.setStroke(Color.BLACK);
        arrowHead.setStrokeWidth(3);
        arrowHead.setFill(Color.BLACK);
        arrowHead.setViewOrder(-1);
        return arrowHead;
    }

    public void addSymbolContainer(TransitionInputs transitionInputs) {
        HBox container = new HBox(NODE_RADIUS / 5.0);
        container.setAlignment(Pos.BOTTOM_CENTER);
        Text readSymbol = new Text(transitionInputs.getRead());
        readSymbol.setFont(new Font("Arial",NODE_RADIUS / 2.0));
        Text popSymbol = new Text(transitionInputs.getPop());
        popSymbol.setFont(new Font("Arial", NODE_RADIUS / 2.0));
        Text pushSymbol = new Text(transitionInputs.getPush());
        pushSymbol.setFont(new Font("Arial",NODE_RADIUS / 2.0));
        container.getChildren().addAll(readSymbol, popSymbol, pushSymbol);
        container.setOnMouseEntered(e -> {
            if (board.getBtnBeh().getCurrentState().equals(ButtonState.ERASE)) {
                container.setCursor(Cursor.HAND);
            }
        });
        container.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                TransitionInputs newTransitionInputs = board.createArrowTransition(transitionInputs.getRead(), transitionInputs.getPop(), transitionInputs.getPush());
                if (newTransitionInputs != null) {
                    TransitionInputs transitionToChange = findTransition(transitionInputs);
                    if (transitionToChange != null) {
                        board.saveCurrentStateToHistory();
                        transitionToChange.setRead(newTransitionInputs.getRead());
                        transitionToChange.setPop(newTransitionInputs.getPop());
                        transitionToChange.setPush(newTransitionInputs.getPush());
                    }
                    readSymbol.setText(newTransitionInputs.getRead());
                    popSymbol.setText(newTransitionInputs.getPop());
                    pushSymbol.setText(newTransitionInputs.getPush());
                    this.board.updateAllDescribePDA();
                }
                event.consume();
            }
            if (event.getButton() == MouseButton.PRIMARY && board.getBtnBeh().getCurrentState().equals(ButtonState.ERASE)) {
                board.saveCurrentStateToHistory();
                this.transitions.remove(transitionInputs);
                this.symbolContainers.getChildren().remove(container);
                if (symbolContainers.getChildren().isEmpty()) {
                    board.getArrows().remove(this);
                    this.getFrom().removeArrow(this);
                    this.getTo().removeArrow(this);
                    board.getMainPane().getChildren().remove(this);
                }
                this.updateStackPanePosition(this.symbolContainers);
                this.board.updateAllDescribePDA();
            }
        });
        container.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            container.setLayoutX(newValue.getWidth());
            container.setLayoutX(newValue.getHeight());
            this.symbolContainers = updateStackPanePosition(this.symbolContainers);
        });
        this.transitions.add(transitionInputs);
        this.symbolContainers.getChildren().add(container);
        this.board.updateAllDescribePDA();
    }


    private TransitionInputs findTransition(TransitionInputs transitionInputs) {
        for (TransitionInputs transition : transitions) {
            if (transition.equals(transitionInputs)) {
                return transition;
            }
        }
        return null;
    }

    public abstract void updateObjects(boolean toEdge);

    public abstract VBox updateStackPanePosition(VBox symbolContainers);


    protected Point2D getNodeEdgePoint(MyNode node, double targetX, double targetY) {

        double side1 = targetX - node.getAbsoluteCentrePosX();
        double side2 = targetY - node.getAbsoluteCentrePosY();

        double angle = Math.atan2(side2, side1);
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        double edgeX = node.getAbsoluteCentrePosX() + cosAngle * node.getCircle().getRadius();
        double edgeY = node.getAbsoluteCentrePosY() + sinAngle * node.getCircle().getRadius();

        return new Point2D(edgeX, edgeY);
    }

    protected ArrowHeadPoints getArrowHeadPoints(double endX, double endY, double endXAngled, double endYAngled) {
        double angle = Math.atan2((endY - endYAngled), (endX - endXAngled)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        //point1
        double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * (NODE_RADIUS  / 2.0) + endX;
        double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * (NODE_RADIUS  / 2.0) + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * (NODE_RADIUS  / 2.0) + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * (NODE_RADIUS  / 2.0) + endY;
        return new ArrowHeadPoints(endX, endY, x1, y1, x2, y2);
    }

    public abstract void updateArrowHead();


    public abstract void move(boolean toEdge);


    public boolean sameTransitionExists(TransitionInputs transitionInputs) {
        for (TransitionInputs transition : transitions) {
            if (transition.equals(transitionInputs)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arrow arrow)) return false;

        if (getFromId() != null ? !getFromId().equals(arrow.getFromId()) : arrow.getFromId() != null) return false;
        if (getToId() != null ? !getToId().equals(arrow.getToId()) : arrow.getToId() != null) return false;
        return getID() != null ? getID().equals(arrow.getID()) : arrow.getID() == null;
    }

    @Override
    public int hashCode() {
        int result = getFromId() != null ? getFromId().hashCode() : 0;
        result = 31 * result + (getToId() != null ? getToId().hashCode() : 0);
        result = 31 * result + (getID() != null ? getID().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Arrow{" +
                "transitions=" + transitions +
                ", fromId=" + fromId +
                ", toId=" + toId +
                '}';
    }
}



