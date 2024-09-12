package com.example.bakalar.logic.utility;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import com.example.bakalar.logic.conversion.simplify.GrammarComponents;
import com.example.bakalar.logic.utility.sorters.SpecialNonTerminalSorter;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.Getter;

import java.util.Set;

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
        rulesContainer.getChildren().clear();
        rules.stream()
                .map(CFGRule::toString)
                .map(TextField::new)
                .peek(textField -> textField.setFont(new Font(18)))
                .peek(textField -> textField.setEditable(false))
                .forEach(rulesContainer.getChildren()::add);
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
