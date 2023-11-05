package com.example.bakalar.canvas;

import com.example.bakalar.pda.TranFun;
import javafx.util.Pair;

import java.util.ArrayList;

public class Arrow {
    private MyNode from;
    private MyNode to;
    private ArrayList<TranFun> tranFun;
    private Pair<Double, Double> start;
    private Pair<Double, Double> finish;

    public Arrow(MyNode from, MyNode to, Pair<Double, Double> start, Pair<Double, Double> finish) {
        this.from = from;
        this.to = to;
        this.start =start;
        this.finish = finish;
    }

    public MyNode getFrom() {
        return from;
    }

    public void setFrom(MyNode from) {
        this.from = from;
    }

    public MyNode getTo() {
        return to;
    }

    public void setTo(MyNode to) {
        this.to = to;
    }

    public ArrayList<TranFun> getTranFun() {
        return tranFun;
    }

    public void setTranFun(ArrayList<TranFun> tranFun) {
        this.tranFun = tranFun;
    }

    public Pair<Double, Double> getStart() {
        return start;
    }

    public void setStart(Pair<Double, Double> start) {
        this.start = start;
    }

    public Pair<Double, Double> getFinish() {
        return finish;
    }

    public void setFinish(Pair<Double, Double> finish) {
        this.finish = finish;
    }
}
