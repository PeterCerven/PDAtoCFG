package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public record GrammarComponents(List<CFGRule> rules, NonTerminal startingSymbol, Set<MySymbol> terminals,
                                Set<NonTerminal> nonTerminals) {
}
