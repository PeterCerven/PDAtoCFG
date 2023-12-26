package com.example.bakalar.character;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MySymbol {
    String name;

    public MySymbol(String name) {
        this.name = name;
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
}
