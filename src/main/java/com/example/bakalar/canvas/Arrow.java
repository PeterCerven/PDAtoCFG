package com.example.bakalar.canvas;

import com.example.bakalar.pda.TranFun;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
public class Arrow extends Line {
    private MyNode from;
    private MyNode to;


    public Arrow(MyNode from, MyNode to, LineCoordinates lineCr, AnchorPane mainPane) {
        super(lineCr.getStartX(), lineCr.getStartY(), lineCr.getEndX(), lineCr.getEndY());
        this.setStrokeType(StrokeType.OUTSIDE);
        this.setStrokeWidth(2);
        this.setStroke(Color.BLACK);
        this.from = from;
        this.to = to;

        Polygon arrowhead = createArrowhead(lineCr.startX, lineCr.startY, lineCr.getEndX(), lineCr.getEndY());

        // Add the arrowhead to the scene
        mainPane.getChildren().add(this);
        mainPane.getChildren().add(arrowhead);
    }

    private Polygon createArrowhead(double startX, double startY, double endX, double endY) {
        double arrowSize = 20; // Adjust the arrowhead size as needed
        Polygon arrowhead = new Polygon();

        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        //point1
        double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowSize + endX;
        double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowSize + endY;

        arrowhead.setStroke(Color.BLUE);
        arrowhead.setStrokeWidth(5);
        arrowhead.getPoints().addAll(
                endX, endY,
                x1, y1,
                x2, y2
        );
        return arrowhead;
    }


}
