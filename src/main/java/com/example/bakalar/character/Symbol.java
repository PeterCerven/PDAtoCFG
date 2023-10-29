package com.example.bakalar.character;

import java.util.Objects;

public abstract class Symbol {
    private Character name;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Symbol(Character name) {
        this.value = 0;
        this.name = name;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol symbol = (Symbol) o;
        return getName().equals(symbol.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }



    @Override
    public String toString() {
        return this.name.toString();
    }
}
