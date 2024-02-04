package com.example.bakalar.canvas.node;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeTransition implements Cloneable {
    private String read;
    private String pop;
    private String push;

    public NodeTransition(String read, String pop, String push) {
        this.read = read;
        this.pop = pop;
        this.push = push;
    }

    @Override
    public NodeTransition clone() {
        try {
            // As String is immutable, shallow copying the fields is adequate
            return (NodeTransition) super.clone();
        } catch (CloneNotSupportedException e) {
            // This should never happen since we're Cloneable
            throw new AssertionError(e);
        }
    }


}
