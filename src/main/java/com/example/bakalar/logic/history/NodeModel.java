package com.example.bakalar.logic.history;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NodeModel implements Serializable {
    private String name;
    private double x;
    private double y;
    private boolean starting;
    private boolean ending;
    private Long nodeId;

    public NodeModel(String name, double x, double y, boolean starting, boolean ending, Long nodeId) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.starting = starting;
        this.ending = ending;
        this.nodeId = nodeId;
    }

    public NodeModel() {
    }
}
