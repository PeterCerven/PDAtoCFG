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
            return (NodeTransition) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeTransition that)) return false;

        if (getRead() != null ? !getRead().equals(that.getRead()) : that.getRead() != null) return false;
        if (getPop() != null ? !getPop().equals(that.getPop()) : that.getPop() != null) return false;
        return getPush() != null ? getPush().equals(that.getPush()) : that.getPush() == null;
    }

    @Override
    public int hashCode() {
        int result = getRead() != null ? getRead().hashCode() : 0;
        result = 31 * result + (getPop() != null ? getPop().hashCode() : 0);
        result = 31 * result + (getPush() != null ? getPush().hashCode() : 0);
        return result;
    }
}
