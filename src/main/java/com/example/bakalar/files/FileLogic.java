package com.example.bakalar.files;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.history.AppState;
import com.example.bakalar.logic.utility.ErrorPopUp;
import com.example.bakalar.logic.utility.sorters.RuleSorter;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.example.bakalar.logic.utility.ErrorPopUp.showErrorDialog;

public class FileLogic {


    public FileLogic() {

    }

    public void saveToJson(AppState appState, Stage primaryStage) {
        File file = showSaveFileDialog(primaryStage);
        if (file != null) {
            file = file.getName().endsWith(".pda") ? file : new File(file.getAbsolutePath() + ".pda");
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(new File(file.getAbsolutePath()), appState);
            } catch (Exception e) {
                showErrorDialog("Error pri ukladaní súboru");
            }
        }
    }




    public AppState loadFromJson(Stage stage) {
        try {
            File file = showOpenFileDialog(stage);
            if (file == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, AppState.class);
        } catch (Exception e) {
            showErrorDialog("Nepodarilo sa načítať súbor");
            return null;
        }
    }

    public File showSaveFileDialog(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MyDiagram files (*.pda)", "pda");
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showSaveDialog(primaryStage);
    }

    public File showOpenFileDialog(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MyDiagram files (*.pda)", "*.pda");
        fileChooser.getExtensionFilters().add(extFilter);


        return fileChooser.showOpenDialog(primaryStage);
    }

    public void saveToTextFile(List<CFGRule> allRules, Stage informationStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(informationStage);
        if (file != null) {
            file = file.getName().endsWith(".txt") ? file : new File(file.getAbsolutePath() + ".txt");
            try {
                PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);

                for (CFGRule rule : allRules.stream().sorted(new RuleSorter()).toList()) {
                    writer.println(rule.toString());
                }
                writer.close();
            } catch (Exception e) {
                ErrorPopUp.showErrorDialog("Error pri ukladaní súboru");
            }
        }
    }
}
