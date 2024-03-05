package com.example.bakalar.logic.utility;

import com.example.bakalar.logic.conversion.CFGRule;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Set;

public class DescribeCFG {
    private final TextField nonTerminals;
    private final TextField terminals;
    private final TextField startSymbol;
    private final VBox rulesContainer;
    
    public DescribeCFG(List<TextField> describeCFGFields, VBox rulesContainer) {
        this.nonTerminals = describeCFGFields.get(0);
        this.terminals = describeCFGFields.get(1);
        this.startSymbol = describeCFGFields.get(2);
        this.rulesContainer = rulesContainer;
    }
    public void updateAllDescribeCFG(Set<SpecialNonTerminal> allNonTerminals, Set<MySymbol> allTerminals, List<CFGRule> rules, String startingS) {
        updateNonTerminals(allNonTerminals);
        updateTerminals(allTerminals);
        updateStartSymbol(startingS);
        updateRules(rules);
    }
    private void updateRules(List<CFGRule> rules) {
        rulesContainer.getChildren().clear();
        for (CFGRule rule : rules) {
            rulesContainer.getChildren().add(new TextField(rule.toString()));
        }
    }

    private void updateStartSymbol(String startingS) {
        startSymbol.setText(startingS);
    }

    private void updateTerminals(Set<MySymbol> terminals) {
        StringBuilder text = new StringBuilder("T = {");
        for (MySymbol terminal : terminals) {
            text.append(terminal.getName()).append(", ");
        }
        if (!terminals.isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.terminals.setText(text.toString());
    }

    private void updateNonTerminals(Set<SpecialNonTerminal> nonTerminals) {
        StringBuilder text = new StringBuilder("N = {");
        for (SpecialNonTerminal nonTerminal : nonTerminals) {
            text.append(nonTerminal.toString()).append(", ");
        }
        if (!nonTerminals.isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.nonTerminals.setText(text.toString());
    }

}
