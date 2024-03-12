package com.example.bakalar.canvas.node;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class MyNodeModel implements Serializable {
    private UUID nodeId;
    private double x;
    private double y;
    private String name;
    private boolean starting;
    private boolean ending;
}
