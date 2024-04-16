package com.example.bakalar.logic.conversion.window;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.ConversionLogic;
import com.example.bakalar.logic.utility.DescribeCFG;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class InformationWindow {
    private final DescribeCFG describeCFG;
    private final BorderPane informationPane;
    private final Button downloadBtn;
    public InformationWindow() {
        describeCFG = new DescribeCFG();
        VBox nonTerminals = describeCFG.getNonTerminals();
        TextField terminals = describeCFG.getTerminals();
        TextField startSymbol = describeCFG.getStartSymbol();
        VBox rulesContainer = describeCFG.getRulesContainer();

        ScrollPane nonTerminalsScrollPane = new ScrollPane();
        nonTerminalsScrollPane.setFitToWidth(true);
        nonTerminalsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        nonTerminalsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        nonTerminalsScrollPane.setPadding(new Insets(10));
        nonTerminalsScrollPane.setStyle("-fx-background-color: #f4f4f4; ");
        nonTerminalsScrollPane.setContent(nonTerminals);

        downloadBtn = new Button("Stiahni pravidlá");
        downloadBtn.setAlignment(Pos.BOTTOM_RIGHT);
        downloadBtn.setFont(new Font(22));

        HBox hBox = new HBox();
        hBox.getChildren().add(downloadBtn);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);

        ScrollPane ruleBoxScrollPane = new ScrollPane();
        ruleBoxScrollPane.setFitToWidth(true);
        ruleBoxScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        ruleBoxScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ruleBoxScrollPane.setPadding(new Insets(10));
        ruleBoxScrollPane.setStyle("-fx-background-color: #f4f4f4; ");
        ruleBoxScrollPane.setContent(rulesContainer);

        Label label = new Label("Neterminálne symboly");
        label.getStyleClass().add("box-label");

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(10));
        vBox.setPrefWidth(250);
        vBox.getChildren().add(startSymbol);
        vBox.getChildren().add(terminals);
        vBox.getChildren().add(label);
        vBox.getChildren().add(nonTerminalsScrollPane);


        informationPane = new BorderPane();
        informationPane.setCenter(ruleBoxScrollPane);
        informationPane.setRight(vBox);
        informationPane.setBottom(hBox);


        terminals.setEditable(false);
        startSymbol.setEditable(false);

        nonTerminals.setPrefWidth(200);
        terminals.setPrefWidth(200);
        startSymbol.setPrefWidth(200);

        rulesContainer.setPrefWidth(200);

        terminals.setPromptText("T = { }");
        startSymbol.setPromptText("S = ");

        rulesContainer.setSpacing(5);

        informationPane.setPrefWidth(400);

        terminals.setPrefHeight(50);
        startSymbol.setPrefHeight(50);

        terminals.setFont(new Font(18));
        startSymbol.setFont(new Font(18));

        rulesContainer.setPrefHeight(400);
    }



    public BorderPane getInformationPane(Set<SpecialNonTerminal> allNonTerminals, Set<MySymbol> allTerminals, List<CFGRule> rules, String startingS) {
        describeCFG.updateAllDescribeCFG(allNonTerminals, allTerminals, rules, startingS);
        return informationPane;
    }
}
