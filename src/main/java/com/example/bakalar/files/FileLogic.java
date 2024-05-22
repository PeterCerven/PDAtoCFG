package com.example.bakalar.files;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.LineArrow;
import com.example.bakalar.canvas.arrow.TransitionInputs;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.history.AppState;
import com.example.bakalar.logic.history.ArrowModel;
import com.example.bakalar.logic.history.NodeModel;
import com.example.bakalar.logic.utility.ErrorPopUp;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.bakalar.logic.utility.ErrorPopUp.showErrorDialog;

public class FileLogic {


    public FileLogic() {
    }

    public void saveToJson(List<MyNode> nodes, List<Arrow> arrows, int nodeCounter, Long idCounter, Stage primaryStage) {
        File file = showSaveFileDialog(primaryStage);
        if (file != null) {
            file = file.getName().endsWith(".pda") ? file : new File(file.getAbsolutePath() + ".pda");
            List<NodeModel> myNodeModels = nodes.stream().map(node -> {
                NodeModel myNodeModel = new NodeModel();
                myNodeModel.setNodeId(node.getNodeId());
                myNodeModel.setX(node.getAbsoluteCentrePosX());
                myNodeModel.setY(node.getAbsoluteCentrePosY());
                myNodeModel.setName(node.getName());
                myNodeModel.setStarting(node.isStarting());
                myNodeModel.setEnding(node.isEnding());
                return myNodeModel;
            }).toList();

            List<ArrowModel> arrowModels = new ArrayList<>();
            for (Arrow arrow : arrows) {
                for (TransitionInputs inputs : arrow.getTransitions()) {
                    ArrowModel arrowModel = new ArrowModel();
                    arrowModel.setArrowId(arrow.getArrowId());
                    arrowModel.setFromNodeId(arrow.getFrom().getNodeId());
                    arrowModel.setToNodeId(arrow.getTo().getNodeId());
                    arrowModel.setTransition(inputs);
                    arrowModels.add(arrowModel);
                    if (arrow instanceof LineArrow lineArrow) {
                        arrowModel.setLineArrow(true);
                        arrowModel.setControlPointChangeX(lineArrow.getControlPointChangeX());
                        arrowModel.setControlPointChangeY(lineArrow.getControlPointChangeY());
                    }
                }
            }

            saveToJson(myNodeModels, arrowModels, nodeCounter, idCounter, file.getAbsolutePath());
        }
    }

    private void saveToJson(List<NodeModel> nodes, List<ArrowModel> arrows, int nodeCounter, Long idCounter, String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AppState state = new AppState(nodes, arrows, nodeCounter, idCounter);
            mapper.writeValue(new File(filePath), state);
        } catch (Exception e) {
            showErrorDialog("Error pri ukladaní súboru");
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
                for (CFGRule rule : allRules) {
                    writer.println(rule.toString());
                }
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
                ErrorPopUp.showErrorDialog("Error pri ukladaní súboru");
            }
        }
    }
}
