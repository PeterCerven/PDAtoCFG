package com.example.bakalar.cfg;

import com.example.bakalar.character.NonTerminalSymbol;
import com.example.bakalar.character.Symbol;
import com.example.bakalar.character.TerminalSymbol;

import java.util.*;
import java.util.stream.Collectors;

public class ContextFreeGrammar {
    public static final Character EPSILON = 'ε';

    private Set<Rule> rules;
    private NonTerminalSymbol startSymbol;
    private Set<TerminalSymbol> terminals;
    private Set<NonTerminalSymbol> nonTerminals;
    private Grammar grammar;
    private GrammarLogic grammarLogic;

    public ContextFreeGrammar(Set<Rule> rules, Character startSymbol) {
        grammarLogic = new GrammarLogic();
        this.rules = rules;
        this.startSymbol = new NonTerminalSymbol(startSymbol);
        this.grammar = new Grammar(rules, this.startSymbol);
        findSymbols();
        grammarTransformation();
    }
    
    
    private void findSymbols() {
        nonTerminals = new LinkedHashSet<>();
        terminals = new LinkedHashSet<>();
        for (Rule rule : rules) {
            nonTerminals.add(rule.getLeftSide());
            for (Symbol s : rule.getRightSide()) {
                if (s instanceof TerminalSymbol) {
                    terminals.add((TerminalSymbol) s);
                } else if (s instanceof NonTerminalSymbol) {
                    nonTerminals.add((NonTerminalSymbol) s);
                }
            }
        }
    }

    public void printRules() {
        ArrayList<Rule> sortedRules = new ArrayList<>(rules);
        sortedRules.sort((o1, o2) -> {
            if (o1.getLeftSide().getName().equals(startSymbol.getName())) {
                return -1;
            } else if (o2.getLeftSide().getName().equals(startSymbol.getName())) {
                return 1;
            } else return o1.getLeftSide().getName().compareTo(o2.getLeftSide().getName());
        });
        sortedRules.forEach(System.out::println);
        System.out.println("--------------------");
    }

    public void grammarTransformation() {
        System.out.println("CFG");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        removalOfNullProduction();
        removeDuplicateRules();
        System.out.println("Removal of null production");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        removeUnitProduction();
        removeDuplicateRules();
        System.out.println("Remove unit production");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        newStartSymbol();
        removeDuplicateRules();
        System.out.println("New start symbol");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        removalOfNullProduction();
        removeDuplicateRules();
        System.out.println("Removal of null production");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        removeUnitProduction();
        removeDuplicateRules();
        System.out.println("Remove unit production");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        System.out.println("Reduction of CFG");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        maxTwoRightSides();
        System.out.println("Max two symbols");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        removeTerminalNonTerminalRule();
        System.out.println("A -> aB to A -> XB and X -> a");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        changeToGreibachNormalForm();
        System.out.println("Greibach Normal Form");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        removeLeftRecursion();
        System.out.println("Left recursion");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();
        addTerminals();
        System.out.println("Add terminals");
        System.out.println("terminals: " + terminals);
        System.out.println("non-terminals: " + nonTerminals);
        this.printRules();

    }

    public void reductionOfCFG() {
        removeNonTerminalSymbols();
        removeCycle();
        phase2();
    }

    private void removeNonTerminalSymbols() {
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
        nonTerminals = afterSet.stream().map(symbol -> (NonTerminalSymbol) symbol).collect(Collectors.toSet());
        removeNonUsedSymbols();
    }

    private void phase2() {
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

    private void removeCycle() {
        for (Rule rule : rules) {
            if (rule.getRightSide().size() == 1) {
                rule.getRightSide().removeIf(s -> rule.getLeftSide().equals(s));

            }
        }
    }


    private boolean onlyNonTerminals(List<Symbol> symbols) {
        for (Symbol symbol : symbols) {
            if (symbol instanceof TerminalSymbol) {
                return false;
            }
        }
        return true;
    }

    // NonTerminal -> NonTerminal
    public void removeUnitProduction() {
        boolean changed;
        do {
            Set<Rule> tempRules = new LinkedHashSet<>();
            changed = false;
            for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); ) {
                Rule rule = iterator.next();
                if (rule.getRightSide().size() == 1 && rule.getRightSide().get(0) instanceof NonTerminalSymbol nonTerminal) {
                    for (Rule r : rules) {
                        if (r.getLeftSide().equals(nonTerminal) && !r.getRightSide().equals(rule.getRightSide())) {
                            tempRules.add(new Rule(rule.getLeftSide(), r.getRightSide()));
                            changed = true;
                        }
                    }
                    iterator.remove();
                    rules.addAll(tempRules);
                    break;
                }
            }
        } while (changed);
        reductionOfCFG();
    }

    public void removalOfNullProduction() {
        boolean changed;
        do {
            Set<Rule> tempRules = new LinkedHashSet<>();
            changed = false;
            for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); ) {
                Rule rule = iterator.next();
                if (rule.getRightSide().size() == 1
                        && rule.getRightSide().get(0) instanceof TerminalSymbol terminalSymbol
                        && terminalSymbol.getName() == EPSILON) {
                    for (Rule r : rules) {
                        int occurrence = (int) r.howManyTimesIsSymbolPresent(rule.getLeftSide());
                        if (occurrence > 0) {
                            createNewRulesFromNull(occurrence, r, rule.getLeftSide(), tempRules);
                        }
                    }
                    iterator.remove();
                    rules.addAll(tempRules);
                    changed = true;
                    break;
                }
            }
        } while (changed);
        terminals.removeIf(s -> s.getName() == EPSILON);
        reductionOfCFG();
    }

    private void createNewRulesFromNull(int occurrence, Rule r, NonTerminalSymbol nonTerminal, Set<Rule> tempRules) {
        int numberOfRules = (int) Math.pow(2, occurrence);
        int[][] truthTable = createTruthTable(numberOfRules, occurrence);
        for (int i = 0; i < numberOfRules; i++) {
            List<Symbol> symbols = new ArrayList<>();
            int k = 0;
            for (int j = 0; j < r.getRightSide().size(); j++) {
                if (!r.getRightSide().get(j).equals(nonTerminal)) {
                    symbols.add(r.getRightSide().get(j));
                } else if (truthTable[i][k] == 1 && r.getRightSide().get(j).equals(nonTerminal)) {
                    symbols.add(r.getRightSide().get(j));
                    k++;
                } else {
                    k++;
                }
            }
            if (symbols.isEmpty()) {
                symbols.add(new TerminalSymbol(EPSILON));
            }
            tempRules.add(new Rule(r.getLeftSide(), symbols));
        }
        reductionOfCFG();
    }

    private int[][] createTruthTable(int rows, int occurrence) {
        int[][] truthTable = new int[rows][occurrence];
        for (int i = 0; i < rows; i++) {
            for (int j = occurrence - 1; j >= 0; j--) {
                truthTable[i][j] = (i / (int) Math.pow(2, j)) % 2;
            }
        }
        return truthTable;
    }

    public void newStartSymbol() {
        boolean changeStartSymbol = false;
        for (Rule rule : rules) {
            if (rule.getLeftSide().equals(startSymbol)) {
                for (Symbol symbol : rule.getRightSide()) {
                    if (symbol.equals(startSymbol)) {
                        changeStartSymbol = true;
                        break;
                    }
                }
            }
        }
        if (changeStartSymbol) {
            NonTerminalSymbol newSymbol = createNewRuleSymbol();
            for (Rule rule : rules) {
                if (rule.getLeftSide().equals(startSymbol)) {
                    rule.setLeftSide(newSymbol);
                }
                for (Symbol symbol : rule.getRightSide()) {
                    if (symbol.equals(startSymbol)) {
                        symbol.setName(newSymbol.getName());
                    }
                }
            }
            rules.add(new Rule(startSymbol, new ArrayList<>(List.of(newSymbol))));
        }
        reductionOfCFG();
    }

    private NonTerminalSymbol createNewRuleSymbol() {
        NonTerminalSymbol newSymbol = new NonTerminalSymbol('A');
        while (nonTerminals.contains(newSymbol)) {
            newSymbol.setName((char) (newSymbol.getName() + 1));
        }
        nonTerminals.add(newSymbol);
        return newSymbol;
    }


    private void removeDuplicateRules() {
        Set<Rule> tempRules = new LinkedHashSet<>(rules);
        rules.clear();
        rules.addAll(tempRules);
        reductionOfCFG();
    }

    public void maxTwoRightSides() {
        while (highestSizeRule() > 2) {
            Set<Rule> tempRules = new LinkedHashSet<>();
            for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); ) {
                Rule rule = iterator.next();
                if (rule.getRightSide().size() > 2 && onlyNonTerminals(rule.getRightSide().subList(1, rule.getRightSide().size()))) {
                    List<Symbol> tmpSymbols = rule.getRightSide().subList(1, rule.getRightSide().size());
                    Rule r = existingRightSide(tmpSymbols, tempRules);
                    if (r != null) {
                        tempRules.add(new Rule(rule.getLeftSide(), new ArrayList<>(List.of(rule.getRightSide().get(0), r.getLeftSide()))));
                        iterator.remove();
                    } else {
                        NonTerminalSymbol newSymbol = createNewRuleSymbol();
                        List<Symbol> symbols = new ArrayList<>(rule.getRightSide().subList(1, rule.getRightSide().size()));
                        tempRules.add(new Rule(newSymbol, symbols));
                        tempRules.add(new Rule(rule.getLeftSide(), new ArrayList<>(List.of(rule.getRightSide().get(0), newSymbol))));
                        iterator.remove();
                    }
                }
            }
            rules.addAll(tempRules);
        }
        reductionOfCFG();
    }

    private int highestSizeRule() {
        int highestSize = 0;
        for (Rule rule : rules) {
            if (rule.getRightSide().size() > highestSize) {
                highestSize = rule.getRightSide().size();
            }
        }
        return highestSize;
    }

    public void removeTerminalNonTerminalRule() {
        Set<Rule> tempRules = new LinkedHashSet<>();
        for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); ) {
            Rule rule = iterator.next();
            if (rule.getRightSide().size() == 2 &&
                    rule.getRightSide().get(0) instanceof TerminalSymbol terminalSymbol
                    && rule.getRightSide().get(1) instanceof NonTerminalSymbol nonTerminalSymbol) {
                List<Symbol> tmpSymbols = List.of(terminalSymbol);
                Rule r = existingRightSide(tmpSymbols, tempRules);
                if (r != null) {
                    tempRules.add(new Rule(rule.getLeftSide(), new ArrayList<>(List.of(r.getLeftSide(), nonTerminalSymbol))));
                    iterator.remove();
                } else {
                    NonTerminalSymbol newSymbol = createNewRuleSymbol();
                    List<Symbol> symbols = new ArrayList<>(List.of(terminalSymbol));
                    tempRules.add(new Rule(newSymbol, symbols));
                    tempRules.add(new Rule(rule.getLeftSide(), new ArrayList<>(List.of(newSymbol, nonTerminalSymbol))));
                    iterator.remove();
                }
            }
        }
        rules.addAll(tempRules);
        reductionOfCFG();
    }

    private Rule existingRightSide(List<Symbol> symbols, Set<Rule> tmpSymbols) {
        int count;
        Set<Rule> tempRules = new LinkedHashSet<>(rules);
        tempRules.addAll(tmpSymbols);
        for (Rule rule : tempRules) {
            if (rule.getRightSide().equals(symbols)) {
                count = 0;
                for (Rule r : tempRules) {
                    if (rule.getLeftSide().equals(r.getLeftSide())) {
                        count++;
                        if (count > 1) {
                            break;
                        }
                    }
                }
                if (count == 1) {
                    return rule;
                }
            }
        }
        return null;
    }

    public void changeToGreibachNormalForm() {
        assignSymbolsValues();
        Set<Rule> tempRules;
        boolean change;
        do {
            tempRules = new LinkedHashSet<>();
            change = false;
            for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); ) {
                Rule rule = iterator.next();
                if (rule.getRightSide().get(0) instanceof NonTerminalSymbol nonTerminalSymbol
                        && rule.getLeftSide().getValue() > nonTerminalSymbol.getValue()) {
                    List<Symbol> symbols = new ArrayList<>();
                    if (rule.getRightSide().size() > 1) {
                        symbols.addAll(rule.getRightSide().subList(1, rule.getRightSide().size()));
                    }
                    for (Rule r : rules) {
                        if (r.getLeftSide().equals(nonTerminalSymbol)) {
                            List<Symbol> symbols1 = new ArrayList<>(r.getRightSide());
                            symbols1.addAll(symbols);
                            tempRules.add(new Rule(rule.getLeftSide(), symbols1));
                            change = true;
                        }
                    }
                    iterator.remove();
                }
                if (change) {
                    break;
                }
            }
            rules.addAll(tempRules);
        } while (change);
        reductionOfCFG();
    }

    private void assignSymbolsValues() {
        int value = 1;
        startSymbol.setValue(value);
        changeSymbolsValue(startSymbol, value);
        value++;
        Queue<Symbol> queue = new ArrayDeque<>();
        queue.add(startSymbol);
        while (!queue.isEmpty()) {
            for (Rule rule : rules) {
                if (!queue.isEmpty() && rule.getLeftSide().getValue() == queue.peek().getValue()) {
                    for (Symbol symbol : rule.getRightSide()) {
                        if (symbol instanceof NonTerminalSymbol nonTerminalSymbol && nonTerminalSymbol.getValue() == 0) {
                            changeSymbolsValue(nonTerminalSymbol, value);
                            value++;
                            queue.add(nonTerminalSymbol);
                        }
                    }
                }
            }
            queue.poll();
        }
    }

    private void changeSymbolsValue(NonTerminalSymbol nonTerminalSymbol, int value) {
        for (Rule rule : rules) {
            if (rule.getLeftSide().equals(nonTerminalSymbol)) {
                rule.getLeftSide().setValue(value);
            }
            for (Symbol symbol : rule.getRightSide()) {
                if (symbol instanceof NonTerminalSymbol nonTerminalSymbol1) {
                    if (nonTerminalSymbol1.equals(nonTerminalSymbol)) {
                        nonTerminalSymbol1.setValue(value);
                    }
                }
            }
        }

    }

    private void removeLeftRecursion() {
        Set<Rule> tempRules = new LinkedHashSet<>();
        for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); ) {
            Rule rule = iterator.next();
            if (rule.getRightSide().get(0) instanceof NonTerminalSymbol nonTerminalSymbol
                    && rule.getLeftSide().equals(nonTerminalSymbol)) {
                NonTerminalSymbol newSymbol = createNewRuleSymbol();
                List<Symbol> symbols = new ArrayList<>(rule.getRightSide().subList(1, rule.getRightSide().size()));
                tempRules.add(new Rule(newSymbol, symbols));
                List<Symbol> symbols2 = new ArrayList<>(rule.getRightSide().subList(1, rule.getRightSide().size()));
                symbols2.add(newSymbol);
                tempRules.add(new Rule(newSymbol, symbols2));
                for (Rule r : rules) {
                    if (r.getLeftSide().equals(nonTerminalSymbol) && !r.equals(rule)) {
                        List<Symbol> symbols1 = new ArrayList<>(r.getRightSide());
                        symbols1.add(newSymbol);
                        tempRules.add(new Rule(r.getLeftSide(), symbols1));
                    }
                }
                iterator.remove();
            }

        }
        rules.addAll(tempRules);
        reductionOfCFG();
    }

    private void addTerminals() {
        boolean change;
        do {
            change = false;
            Set<Rule> tempRules = new LinkedHashSet<>();
            for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext(); ) {
                Rule rule = iterator.next();
                if (rule.getRightSide().get(0) instanceof NonTerminalSymbol nonTerminalSymbol) {
                    List<Symbol> symbols2 = new ArrayList<>();
                    if (rule.getRightSide().size() > 1) {
                        symbols2.addAll(rule.getRightSide().subList(1, rule.getRightSide().size()));
                    }
                    for (Rule r : rules) {
                        if (r.getLeftSide().equals(nonTerminalSymbol)) {
                            List<Symbol> symbols3 = new ArrayList<>(r.getRightSide());
                            symbols3.addAll(symbols2);
                            tempRules.add(new Rule(rule.getLeftSide(), symbols3));
                            change = true;
                        }
                    }
                    if (change) {
                        iterator.remove();
                        rules.addAll(tempRules);
                        break;
                    }
                }
            }
        } while (change);
    }

    public List<Rule> getJoinedRules() {
        List<Rule> newRules = new ArrayList<>();
        for (NonTerminalSymbol symbol : nonTerminals) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Rule r : rules) {
                if (symbol.equals(r.getLeftSide())) {
                    stringBuilder.append(r.getRightSide()).append("|");
                }
            }
            if (!stringBuilder.isEmpty()) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                newRules.add(new Rule(symbol.getName(), stringBuilder.toString(), 0));
            }
        }
        newRules.sort((o1, o2) -> {
            if (o1.getLeftSide().getName().equals(startSymbol.getName())) {
                return -1;
            } else if (o2.getLeftSide().getName().equals(startSymbol.getName())) {
                return 1;
            } else return o1.getLeftSide().getName().compareTo(o2.getLeftSide().getName());
        });
        return newRules;
    }
}
