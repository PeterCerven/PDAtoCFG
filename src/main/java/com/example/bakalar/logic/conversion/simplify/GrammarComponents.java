package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static com.example.bakalar.logic.MainLogic.EPSILON;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class GrammarComponents {
    private Set<CFGRule> rules;
    private NonTerminal startingSymbol;
    private Set<MySymbol> terminals;
    private Set<NonTerminal> nonTerminals;

    public GrammarComponents(Set<CFGRule> rules, NonTerminal startingSymbol) {
        this.rules = rules;
        this.startingSymbol = startingSymbol;
        this.terminals = setTerminalsFromRules();
        this.nonTerminals = setNonTerminalsFromRules();
    }

    private Set<MySymbol> setTerminalsFromRules() {
        Set<MySymbol> terminals = new HashSet<>();
        for (CFGRule rule : rules) {
            if (rule.getTerminal() != null && !rule.getTerminal().getName().equals(EPSILON)) {
                terminals.add(rule.getTerminal().getDeepCopy());
            }
        }
        return terminals;
    }

    private Set<NonTerminal> setNonTerminalsFromRules() {
        Set<NonTerminal> nonTerminals = new HashSet<>();
            for (CFGRule rule : rules) {
                nonTerminals.add(rule.getLeftSide().getDeepCopy());
                nonTerminals.addAll(rule.getRightSide().stream().map(NonTerminal::getDeepCopy).toList());
            }
        return nonTerminals;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrammarComponents that)) return false;

        return getRules().equals(that.getRules()) && getStartingSymbol().equals(that.getStartingSymbol());
    }

    @Override
    public int hashCode() {
        int result = getRules().hashCode();
        result = 31 * result + getStartingSymbol().hashCode();
        return result;
    }

    public static class GrammarComponentsBuilder {
        public GrammarComponents build() {
            return new GrammarComponents(rules, startingSymbol);
        }
    }

    @Override
    public String toString() {
        return "GrammarComponents{" +
                "rules=" + rules +
                ", startingSymbol=" + startingSymbol +
                ", terminals=" + terminals +
                ", nonTerminals=" + nonTerminals +
                '}';
    }
}
