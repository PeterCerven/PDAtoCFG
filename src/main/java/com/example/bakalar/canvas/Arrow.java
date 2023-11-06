package com.example.bakalar.canvas;

import com.example.bakalar.pda.TranFun;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Arrow extends Line {
    private MyNode from;
    private MyNode to;
    private ArrayList<TranFun> tranFun;


    public Arrow(MyNode from, MyNode to, double startX, double startY, double endX, double endY, int nodeRadius) {
        super(startX, startY, endX, endY);
        this.from = from;
        this.to = to;
        this.tranFun = new ArrayList<>();
    }

}
