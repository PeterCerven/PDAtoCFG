package com.example.bakalar.character;

import com.example.bakalar.cfg.Rule;

public class TerminalSymbol extends Symbol {

    public TerminalSymbol(Character name) {
        super(name);
        super.setValue(-1);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
