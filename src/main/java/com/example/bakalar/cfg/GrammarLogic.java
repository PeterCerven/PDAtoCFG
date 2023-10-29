package com.example.bakalar.cfg;

import com.example.bakalar.character.NonTerminalSymbol;
import com.example.bakalar.character.Symbol;
import com.example.bakalar.character.TerminalSymbol;

import java.util.*;
import java.util.stream.Collectors;

public class GrammarLogic {

    public Set<TerminalSymbol> terminalsInRules(Set<Rule> rules) {
        Set<TerminalSymbol> terminals = new LinkedHashSet<>();
        for (Rule rule : rules) {
            for (Symbol s : rule.getRightSide()) {
                if (s instanceof TerminalSymbol terminalSymbol) {
                    terminals.add(terminalSymbol);
                }
            }
        }
        return terminals;
    }

    public Set<NonTerminalSymbol> nonTerminalsInRules(Set<Rule> rules) {
        Set<NonTerminalSymbol> nonTerminals = new LinkedHashSet<>();
        for (Rule rule : rules) {
            nonTerminals.add(rule.getLeftSide());
            for (Symbol s : rule.getRightSide()) {
                if (s instanceof NonTerminalSymbol nonTerminalSymbol) {
                   nonTerminals.add(nonTerminalSymbol);
                }
            }
        }
        return nonTerminals;
    }

    public void printSortedRules(Set<Rule> rules, NonTerminalSymbol startSymbol) {
            List<Rule> sortedRules = sortedGrammarRules(rules, startSymbol);
            for (Rule rule : sortedRules) {
                System.out.println(rule);
            }
    }

    public List<Rule> sortedGrammarRules(Set<Rule> rules, NonTerminalSymbol startSymbol) {
        List<Rule> sortedRules = new ArrayList<>(rules);
        sortedRules.sort((o1, o2) -> {
            if (o1.getLeftSide().getName().equals(startSymbol.getName())) {
                return -1;
            } else if (o2.getLeftSide().getName().equals(startSymbol.getName())) {
                return 1;
            } else return o1.getLeftSide().getName().compareTo(o2.getLeftSide().getName());
        });
        return sortedRules;
    }

    public void reduceGrammar(List<TerminalSymbol> terminals, List<NonTerminalSymbol> nonTerminals, Set<Rule> rules, NonTerminalSymbol startSymbol) {
        Grammar grammar = removeNonTerminalSymbols(terminals, rules, startSymbol);
        Set<Rule> newRules = removeCycle(rules);
        phase2();
    }

    private Grammar removeNonTerminalSymbols(List<TerminalSymbol> terminals, Set<Rule> rules, NonTerminalSymbol startSymbol) {
        Set<Symbol> beforeSet = new LinkedHashSet<>(terminals);
        Set<Symbol> afterSet = new LinkedHashSet<>();
        while (true) {
            for (Rule rule : rules) {
                for (Symbol symbol : beforeSet) {
                    if (rule.getRightSide().contains(symbol)) {
                        afterSet.add(rule.getLeftSide());
                    }
                }
            }
            if (beforeSet.equals(afterSet)) {
                break;
            }
            beforeSet = new LinkedHashSet<>(afterSet);
        }
        removeNonUsedSymbols();
        return new Grammar(rules, startSymbol);
    }

    private void phase2(List<TerminalSymbol> terminals, Set<Rule> rules, NonTerminalSymbol startSymbol) {
        Set<Symbol> beforeSet = new LinkedHashSet<>();
        beforeSet.add(startSymbol);
        Set<Symbol> afterSet = new LinkedHashSet<>();
        afterSet.add(startSymbol);
        while (true) {
            for (Rule rule : rules) {
                for (Symbol symbol : beforeSet) {
                    if (rule.getLeftSide().equals(symbol)) {
                        afterSet.addAll(rule.getRightSide());
                    }
                }
            }
            if (beforeSet.equals(afterSet)) {
                break;
            }
            beforeSet = new LinkedHashSet<>(afterSet);
        }
        terminals = afterSet.stream().filter(symbol -> symbol instanceof TerminalSymbol).map(s -> (TerminalSymbol) s).collect(Collectors.toSet());
        nonTerminals = afterSet.stream().filter(symbol -> symbol instanceof NonTerminalSymbol).map(s -> (NonTerminalSymbol) s).collect(Collectors.toSet());
        removeNonUsedSymbols();
    }

    private void removeNonUsedSymbols() {
        for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); ) {
            Rule rule = iterator.next();
            if (!nonTerminals.contains(rule.getLeftSide())) {
                iterator.remove();
                continue;
            }
            rule.removePart(terminals, nonTerminals);
            if (rule.getRightSide().isEmpty()) {
                iterator.remove();
            }
        }


    }

    private Set<Rule> removeCycle(Set<Rule> rules) {
        for (Rule rule : rules) {
            if (rule.getRightSide().size() == 1) {
                rule.getRightSide().removeIf(s -> rule.getLeftSide().equals(s));

            }
        }
        return rules;
    }


    private boolean onlyNonTerminals(List<Symbol> symbols) {
        for (Symbol symbol : symbols) {
            if (symbol instanceof TerminalSymbol) {
                return false;
            }
        }
        return true;
    }
}
