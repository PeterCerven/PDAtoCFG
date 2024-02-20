package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CFGRule {
    private SpecialNonTerminal leftSide;
    private MySymbol mySymbolLeftSide;
    private String terminal;
    private List<SpecialNonTerminal> rightSide;

    public CFGRule() {
    }

    @Override
    public String toString() {
        if (mySymbolLeftSide == null) {
            return leftSide + " -> " + (terminal == null ? "" : terminal)  + rightSide.stream().map(SpecialNonTerminal::toString).collect(Collectors.joining());
        } else {
            return mySymbolLeftSide + " -> " + (terminal == null ? "" : terminal) + rightSide.stream().map(SpecialNonTerminal::toString).collect(Collectors.joining());
        }
    }
}
