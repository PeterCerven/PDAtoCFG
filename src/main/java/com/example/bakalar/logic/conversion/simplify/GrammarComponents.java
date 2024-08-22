package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class GrammarComponents {
    private final List<CFGRule> rules;
    private final MySymbol startingSymbol;
    private final Set<MySymbol> terminals;
    private final Set<NonTerminal> nonTerminals;

    public GrammarComponents(List<CFGRule> rules, MySymbol startingSymbol, Set<MySymbol> terminals, Set<NonTerminal> nonTerminals) {
        this.rules = rules;
        this.startingSymbol = startingSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
    }
}
