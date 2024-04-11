package com.example.bakalar.logic.utility;

import com.example.bakalar.logic.conversion.CFGRule;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class DescribeCFG {
    private final TextField nonTerminals;
    private final TextField terminals;
    private final TextField startSymbol;
    private final VBox rulesContainer;
    
    public DescribeCFG() {
        this.nonTerminals = new TextField();
        this.terminals = new TextField();
        this.startSymbol = new TextField();
        this.rulesContainer = new VBox();
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
            TextField textField = new TextField();
            textField.setText(rule.toString());
            textField.setFont(new Font(18));
            rulesContainer.getChildren().add(textField);
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
