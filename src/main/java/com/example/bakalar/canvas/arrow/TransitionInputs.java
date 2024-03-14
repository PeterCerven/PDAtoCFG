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

    public TransitionInputs() {
    }

    public TransitionInputs copy() {
        return new TransitionInputs(this.read, this.pop, this.push);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransitionInputs that)) return false;

        if (!getRead().equals(that.getRead())) return false;
        if (!getPop().equals(that.getPop())) return false;
        return getPush().equals(that.getPush());
    }

    @Override
    public int hashCode() {
        int result = getRead() != null ? getRead().hashCode() : 0;
        result = 31 * result + (getPop() != null ? getPop().hashCode() : 0);
        result = 31 * result + (getPush() != null ? getPush().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TransitionInputs{" +
                "read='" + read + '\'' +
                ", pop='" + pop + '\'' +
                ", push='" + push + '\'' +
                '}';
    }
}
