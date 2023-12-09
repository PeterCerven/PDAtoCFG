package com.example.bakalar.canvas.node;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeTransition {
    private String read;
    private String pop;
    private String push;

    public NodeTransition(String read, String pop, String push) {
        this.read = read;
        this.pop = pop;
        this.push = push;
    }
}
