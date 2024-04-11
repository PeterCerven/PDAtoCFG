package com.example.bakalar.logic.conversion.window;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.ConversionLogic;
import com.example.bakalar.logic.utility.DescribeCFG;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class InformationWindow {
    private DescribeCFG describeCFG;
    private BorderPane informationPane;
    public InformationWindow() {
        super();
        describeCFG = new DescribeCFG();
        TextField nonTerminals = describeCFG.getNonTerminals();
        TextField terminals = describeCFG.getTerminals();
        TextField startSymbol = describeCFG.getStartSymbol();
        VBox rulesContainer = describeCFG.getRulesContainer();

        Button downloadBtn = new Button("Stiahni");
        downloadBtn.setAlignment(Pos.BOTTOM_RIGHT);
        downloadBtn.setFont(new Font(22));
        downloadBtn.setOnAction(event -> {
//            ConversionLogic.downloadCFG(nonTerminals.getText(), terminals.getText(), rulesContainer.getChildren(), startSymbol.getText());
        });

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

        VBox vBox = new VBox();
        vBox.getChildren().add(nonTerminals);
        vBox.getChildren().add(terminals);
        vBox.getChildren().add(startSymbol);


        informationPane = new BorderPane();
        informationPane.setCenter(ruleBoxScrollPane);
        informationPane.setRight(vBox);
        informationPane.setBottom(hBox);


        nonTerminals.setEditable(false);
        terminals.setEditable(false);
        startSymbol.setEditable(false);

        nonTerminals.setPrefWidth(200);
        terminals.setPrefWidth(200);
        startSymbol.setPrefWidth(200);

        rulesContainer.setPrefWidth(200);

        nonTerminals.setPromptText("N = { }");
        terminals.setPromptText("T = { }");
        startSymbol.setPromptText("S = ");

        rulesContainer.setSpacing(5);

        informationPane.setPrefWidth(400);

        nonTerminals.setPrefHeight(50);
        terminals.setPrefHeight(50);
        startSymbol.setPrefHeight(50);

        nonTerminals.setFont(new Font(18));
        terminals.setFont(new Font(18));
        startSymbol.setFont(new Font(18));

        rulesContainer.setPrefHeight(400);
    }



    public BorderPane getInformationPane(Set<SpecialNonTerminal> allNonTerminals, Set<MySymbol> allTerminals, List<CFGRule> rules, String startingS) {
        describeCFG.updateAllDescribeCFG(allNonTerminals, allTerminals, rules, startingS);
        return informationPane;
    }
}
