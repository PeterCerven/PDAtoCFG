package com.example.bakalar.cfg;

import com.example.bakalar.character.NonTerminalSymbol;
import com.example.bakalar.character.Symbol;
import com.example.bakalar.character.TerminalSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

// Context Free Grammar Rule
public class Rule {
    private NonTerminalSymbol leftSide;
    private List<Symbol> rightSide;
    private String combinedRightSide;



    public Rule(Character leftSide, String rightSide) {
        this.leftSide = new NonTerminalSymbol(leftSide);
        divideRule(rightSide);
    }

    public Rule(NonTerminalSymbol leftSide, List<Symbol> rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public String getTerminalsRight() {
        StringBuilder sb = new StringBuilder();
        for (Symbol s : rightSide) {
            if (s instanceof TerminalSymbol) {
                sb.append(s.getName());
            }
        }
        return sb.toString();
    }

    public String getNonTerminalsRight() {
        StringBuilder sb = new StringBuilder();
        for (Symbol s : rightSide) {
            if (s instanceof NonTerminalSymbol) {
                sb.append(s.getName());
            }
        }
        return sb.toString();
    }

    public Rule(Character leftSide, String rightSide, int index) {
        this.leftSide = new NonTerminalSymbol(leftSide);
        divideRule(rightSide);
        this.combinedRightSide = rightSide.replaceAll("[\\[\\]\\s+,]", "");
    }

    public String getCombinedRightSide() {
        return combinedRightSide;
    }

    public void divideRule(String string) {
        rightSide = new ArrayList<>();
        for (Character character : string.toCharArray()) {
            if (character.equals('ε')) {
                rightSide.add(new TerminalSymbol(character));
            } else if (character.toString().matches("[A-Z0-9]")) {
                rightSide.add(new NonTerminalSymbol(character));
            } else if (character.toString().matches("[a-z]")) {
                rightSide.add(new TerminalSymbol(character));
            }
        }
    }

    public void setLeftSide(NonTerminalSymbol leftSide) {
        this.leftSide = leftSide;
    }

    public void removePart(Set<TerminalSymbol> terminals, Set<NonTerminalSymbol> nonTerminals) {
        rightSide.removeIf(symbol -> {
            if (symbol instanceof TerminalSymbol) {
                return !terminals.contains(symbol);
            } else if (symbol instanceof NonTerminalSymbol) {
                return !nonTerminals.contains(symbol);
            }
            return true;
        });
    }


    public long howManyTimesIsSymbolPresent(NonTerminalSymbol nonTerminalSymbol) {
        return this.getRightSide().stream().filter(symbol -> symbol.equals(nonTerminalSymbol)).count();
    }



    public NonTerminalSymbol getLeftSide() {
        return leftSide;
    }

    public void setRightSide(List<Symbol> rightSide) {
        this.rightSide = rightSide;
    }

    public List<Symbol> getRightSide() {
        return rightSide;
    }

    @Override
    public String toString() {
        return leftSide + " -> " + rightSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;
        Rule rule = (Rule) o;
        for (int i = 0; i < rightSide.size(); i++) {
            if (!rightSide.get(i).equals(rule.rightSide.get(i))) {
                return false;
            }
        }

        return getLeftSide().equals(rule.getLeftSide()) && getRightSide().size() == (rule.getRightSide().size());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeftSide(), getRightSide());
    }

}

