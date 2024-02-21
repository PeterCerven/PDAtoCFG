package com.example.bakalar.logic.utility;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialNonTerminal {
    private String stateSymbolFrom;
    private String stackSymbol;
    private String stateSymbolTo;

    public SpecialNonTerminal() {
        this.stateSymbolFrom = "_";
        this.stackSymbol = "_";
        this.stateSymbolTo = "_";
    }

    public SpecialNonTerminal(String stateSymbolFrom, String stackSymbol, String stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? "_" : stateSymbolFrom;
        this.stackSymbol = stackSymbol == null ? "_" : stackSymbol;
        this.stateSymbolTo = stateSymbolTo == null ? "_" : stateSymbolTo;
    }

    public SpecialNonTerminal(MySymbol stateSymbolFrom, MySymbol stackSymbol, String stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? "_" : stateSymbolFrom.getName();
        this.stackSymbol = stackSymbol == null ? "_" : stackSymbol.getName();
        this.stateSymbolTo = stateSymbolTo == null ? "_" : stateSymbolTo;
    }

    public SpecialNonTerminal(MySymbol stateSymbolFrom, MySymbol stackSymbol, MySymbol stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? "_" : stateSymbolFrom.getName();
        this.stackSymbol = stackSymbol == null ? "_" : stackSymbol.getName();
        this.stateSymbolTo = stateSymbolTo == null ? "_" : stateSymbolTo.getName();
    }



    @Override
    public String toString() {
        return "[" + stateSymbolFrom + ", " + stackSymbol + ", " + stateSymbolTo + "]";
    }
}
