package com.example.bakalar.canvas.arrow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ArrowModel implements Serializable {
    private UUID fromNodeId;
    private UUID toNodeId;
    private String input;
    private String stackTop;
    private String stackPush;


}
