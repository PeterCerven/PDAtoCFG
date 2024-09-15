package com.example.bakalar.logic.utility;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import com.example.bakalar.logic.conversion.SpecialNonTerminal;
import com.example.bakalar.logic.conversion.simplify.GrammarComponents;
import com.example.bakalar.logic.utility.sorters.SpecialNonTerminalSorter;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class DescribeCFG {
    private final VBox nonTerminals;
    private final TextField terminals;
    private final TextField startSymbol;
    private final VBox rulesContainer;
    
    public DescribeCFG() {
        this.nonTerminals = new VBox();
        this.terminals = new TextField();
        this.startSymbol = new TextField();
        this.rulesContainer = new VBox();
    }
    public void updateAllDescribeCFG(GrammarComponents gc) {
        updateNonTerminals(gc.getNonTerminals());
        updateTerminals(gc.getTerminals());
        updateStartSymbol(gc.getStartingSymbol());
        updateRules(gc.getRules());
    }
    private void updateRules(Set<CFGRule> rules) {
        if (!rules.isEmpty() && rules.stream().map(CFGRule::getRightSide).anyMatch(rightSide -> rightSide.stream().anyMatch(symbol -> symbol instanceof SpecialNonTerminal))) {
            rulesContainer.getChildren().clear();
            rules.stream()
                    .map(CFGRule::toString)
                    .map(TextField::new)
                    .peek(textField -> textField.setFont(new Font(18)))
                    .peek(textField -> textField.setEditable(false))
                    .forEach(rulesContainer.getChildren()::add);
            return;
        }
        rulesContainer.getChildren().clear();
        List<String> rulesList = getRulesList(rules);
        rulesList.stream()
                .map(TextField::new)
                .peek(textField -> textField.setFont(new Font(18)))
                .peek(textField -> textField.setEditable(false))
                .forEach(rulesContainer.getChildren()::add);
    }

    private List<String> getRulesList(Set<CFGRule> rules) {
        List<String> listOfRules  = new ArrayList<>();
        Map<NonTerminal, List<List<String>>> groupedRules = rules.stream()
                .collect(Collectors.groupingBy(
                        CFGRule::getLeftSide,
                        Collectors.mapping(CFGRule::getRightSideString, Collectors.toList())
                ));
        for (Map.Entry<NonTerminal, List<List<String>>> entry : groupedRules.entrySet()) {
            StringBuilder sb = new StringBuilder(entry.getKey() + " -> ");
            StringJoiner sj = new StringJoiner("|");

            for (List<String> list : entry.getValue()) {
                sj.add(String.join("",list));
            }
            sb.append(sj);
            listOfRules.add(sb.toString());
        }
        return listOfRules;
    }

    private void updateStartSymbol(NonTerminal startingS) {
        startSymbol.setText("Začiatočný symbol = " + startingS);
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

    private void updateNonTerminals(Set<NonTerminal> nonTerminals) {
        this.nonTerminals.getChildren().clear();
        nonTerminals.stream()
                .sorted(new SpecialNonTerminalSorter())
                .map(NonTerminal::toString)
                .map(TextField::new)
                .forEach(textField -> {
                    textField.setEditable(false);
                    textField.setStyle("-fx-background-color: #f4f4f4; ");
                    textField.setPrefWidth(200);
                    textField.setAlignment(Pos.CENTER);
                    textField.setFont(new Font(22));
                    this.nonTerminals.getChildren().add(textField);
                });

    }

}
