package com.example.bakalar.logic.history;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeHistory {
    private String name;
    private double x;
    private double y;
    private boolean starting;
    private boolean ending;
    private int nodeId;

    public NodeHistory(String name, double x, double y, boolean starting, boolean ending, int nodeId) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.starting = starting;
        this.ending = ending;
        this.nodeId = nodeId;

    }
}
