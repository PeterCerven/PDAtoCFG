package com.example.bakalar.logic.conversion.window;

import com.example.bakalar.logic.conversion.simplify.GrammarComponents;
import com.example.bakalar.logic.utility.DescribeCFG;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import lombok.Getter;

@Getter
public class InformationWindow {
    private final DescribeCFG describeCFG;
    private final BorderPane informationPane;
    private final Button downloadBtn;
    private final Button reduceBtn;
    private final ScrollPane nonTerminalsScrollPane;
    private final VBox rightPanel;
    private final Label label;
    private final ScrollPane ruleBoxScrollPane;

    private boolean isReduced = false;

    public InformationWindow() {
        describeCFG = new DescribeCFG();
        VBox nonTerminals = describeCFG.getNonTerminals();
        TextField terminals = describeCFG.getTerminals();
        TextField startSymbol = describeCFG.getStartSymbol();
        VBox rulesContainer = describeCFG.getRulesContainer();

        nonTerminalsScrollPane = new ScrollPane();
        nonTerminalsScrollPane.setFitToWidth(true);
        nonTerminalsScrollPane.setPadding(new Insets(5));
        nonTerminalsScrollPane.setStyle("-fx-background-color: #f4f4f4; ");
        nonTerminalsScrollPane.setContent(nonTerminals);

        downloadBtn = new Button("Stiahni pravidlá");
        downloadBtn.setAlignment(Pos.BOTTOM_RIGHT);
        downloadBtn.setFont(new Font(22));

        reduceBtn = new Button("Ukáž zredukovanú gramatiku");
        reduceBtn.setAlignment(Pos.BOTTOM_LEFT);
        reduceBtn.setFont(new Font(22));

        Region spacer = new Region();

        HBox hBox = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBox.getChildren().addAll(reduceBtn, spacer, downloadBtn);

        ruleBoxScrollPane = new ScrollPane();
        ruleBoxScrollPane.setFitToWidth(true);
        ruleBoxScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ruleBoxScrollPane.setPadding(new Insets(10));
        ruleBoxScrollPane.setStyle("-fx-background-color: #f4f4f4; ");
        ruleBoxScrollPane.setContent(rulesContainer);


        VBox contentPane = new VBox();
        contentPane.getChildren().addAll(ruleBoxScrollPane);


        label = new Label("Neterminálne symboly:");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.getStyleClass().add("box-label");

        rightPanel = new VBox();
        rightPanel.setSpacing(2);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(5));
        rightPanel.setPrefWidth(230);
        rightPanel.getChildren().add(startSymbol);
        rightPanel.getChildren().add(terminals);
        rightPanel.getChildren().add(label);
        rightPanel.getChildren().add(nonTerminalsScrollPane);


        informationPane = new BorderPane();
        informationPane.setCenter(contentPane);
        informationPane.setRight(rightPanel);
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
    }

    public void swapCFGtoReduceAndBack(GrammarComponents simplifiedGrammar, GrammarComponents gc) {
        if (isReduced) {
            reduceBtn.setText("Ukáž zredukovanú gramatiku");
            isReduced = false;
            describeCFG.updateAllDescribeCFG(gc);
        } else {
            reduceBtn.setText("Ukáž pôvodnú gramatiku");
            isReduced = true;
            describeCFG.updateAllDescribeCFG(simplifiedGrammar);
        }
    }


}
