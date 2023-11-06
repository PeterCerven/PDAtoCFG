package com.example.bakalar.canvas;

import com.example.bakalar.pda.TranFun;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Arrow extends Line {
    private MyNode from;
    private MyNode to;
    private ArrayList<TranFun> tranFun;


    public Arrow(MyNode from, MyNode to, LineCoordinates lineCr) {
        super(lineCr.getStartX(), lineCr.getStartY(), lineCr.getEndX(), lineCr.getEndY());
        this.setStrokeWidth(10);
        this.from = from;
        this.to = to;
        this.tranFun = new ArrayList<>();
    }

}
