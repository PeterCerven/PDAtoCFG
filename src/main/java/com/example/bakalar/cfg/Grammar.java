package com.example.bakalar.cfg;

import com.example.bakalar.character.NonTerminalSymbol;
import com.example.bakalar.character.TerminalSymbol;

import java.util.Set;

public class Grammar {
    private Set<Rule> rules;
    private NonTerminalSymbol startSymbol;
    private Set<TerminalSymbol> terminals;
    private Set<NonTerminalSymbol> nonTerminals;
    private GrammarLogic grammarLogic;

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

    public void setStartSymbol(NonTerminalSymbol startSymbol) {
        this.startSymbol = startSymbol;
    }

    public void setTerminals(Set<TerminalSymbol> terminals) {
        this.terminals = terminals;
    }

    public void setNonTerminals(Set<NonTerminalSymbol> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public void setGrammarLogic(GrammarLogic grammarLogic) {
        this.grammarLogic = grammarLogic;
    }

    public Grammar(Set<Rule> rules, NonTerminalSymbol startSymbol) {
        grammarLogic = new GrammarLogic();
        this.rules = rules;
        this.startSymbol = startSymbol;
        this.terminals = grammarLogic.terminalsInRules(rules);
        this.nonTerminals = grammarLogic.nonTerminalsInRules(rules);
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public NonTerminalSymbol getStartSymbol() {
        return startSymbol;
    }

    public Set<TerminalSymbol> getTerminals() {
        return terminals;
    }

    public Set<NonTerminalSymbol> getNonTerminals() {
        return nonTerminals;
    }
}
