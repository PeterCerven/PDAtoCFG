package com.example.bakalar.logic.utility;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MySymbol implements Comparable<MySymbol> {
    public static final Color DEFAULT_COLOR = Color.BLACK;
    private String name;
    private Color color;
    private int index;

    public MySymbol(String name) {
        this.name = name;
        this.color = Color.BLACK;
    }

    public MySymbol(MySymbol mySymbol, Color color) {
        this.name = mySymbol.getName();
        this.color = color;
    }

    public MySymbol(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public MySymbol(String name, Color color, int index) {
        this.name = name;
        this.color = color;
        this.index = index;
    }

    public MySymbol getDeepCopy() {
        return new MySymbol(this.name, this.color, this.index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MySymbol mySymbol)) return false;

        return getName().equals(mySymbol.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(MySymbol o) {
        return this.getName().compareTo(o.getName());
    }
}
