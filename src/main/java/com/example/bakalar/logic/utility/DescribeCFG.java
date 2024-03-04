package com.example.bakalar.logic.utility;

import com.example.bakalar.logic.conversion.CFGRule;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

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
    
    public void updateAllDescribeCFG(List<CFGRule> rules) {
        updateNonTerminals(rules);
        updateTerminals(rules);
        updateStartSymbol();
        updateRules(rules);
    }

    private void updateRules(List<CFGRule> rules) {
    }

    private void updateStartSymbol() {
    }

    private void updateTerminals(List<CFGRule> rules) {
    }

    private void updateNonTerminals(List<CFGRule> rules) {
    }
}
