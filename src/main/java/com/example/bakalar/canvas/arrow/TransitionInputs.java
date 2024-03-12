package com.example.bakalar.canvas.arrow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransitionInputs implements Serializable {
    private String read;
    private String pop;
    private String push;

    public TransitionInputs(String read, String pop, String push) {
        this.read = read;
        this.pop = pop;
        this.push = push;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransitionInputs that)) return false;

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
