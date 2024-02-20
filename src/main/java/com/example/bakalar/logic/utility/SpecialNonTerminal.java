package com.example.bakalar.logic.utility;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialNonTerminal {
    private String stateSymbolFrom;
    private String stackSymbol;
    private String stateSymbolTo;

    public SpecialNonTerminal(String stateSymbolFrom, String stackSymbol, String stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? "" : stateSymbolFrom;
        this.stackSymbol = stackSymbol == null ? "" : stackSymbol;
        this.stateSymbolTo = stateSymbolTo == null ? "" : stateSymbolTo;
    }

    public SpecialNonTerminal(MySymbol stateSymbolFrom, MySymbol stackSymbol, String stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? "" : stateSymbolFrom.getName();
        this.stackSymbol = stackSymbol == null ? "" : stackSymbol.getName();
        this.stateSymbolTo = stateSymbolTo == null ? "" : stateSymbolTo;
    }

    public SpecialNonTerminal(MySymbol stateSymbolFrom, MySymbol stackSymbol, MySymbol stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? "" : stateSymbolFrom.getName();
        this.stackSymbol = stackSymbol == null ? "" : stackSymbol.getName();
        this.stateSymbolTo = stateSymbolTo == null ? "" : stateSymbolTo.getName();
    }



    @Override
    public String toString() {
        return "[" + stateSymbolFrom + ", " + stackSymbol + ", " + stateSymbolTo + "]";
    }
}
