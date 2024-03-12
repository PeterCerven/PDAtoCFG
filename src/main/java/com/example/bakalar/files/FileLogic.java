package com.example.bakalar.files;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.ArrowModel;
import com.example.bakalar.canvas.arrow.TransitionInputs;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.MyNodeModel;
import com.example.bakalar.logic.transitions.Transition;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileLogic {

//    saveToJson(nodes, arrows, "myDiagramState.pda");


    public FileLogic() {
    }

    public void saveToJson(List<MyNode> nodes, List<Arrow> arrows, Stage primaryStage) {
        File file = showSaveFileDialog(primaryStage);
        if (file != null) {
            List<MyNodeModel> myNodeModels = nodes.stream().map(node -> {
                MyNodeModel myNodeModel = new MyNodeModel();
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
                    arrowModel.setFromNodeId(arrow.getFrom().getNodeId());
                    arrowModel.setToNodeId(arrow.getTo().getNodeId());
                    arrowModel.setInput(inputs.getRead());
                    arrowModel.setStackTop(inputs.getPop());
                    arrowModel.setStackPush(inputs.getPush());
                    arrowModels.add(arrowModel);
                }
            }

            saveToJson(myNodeModels, arrowModels, file.getAbsolutePath());
        }
    }

    private void saveToJson(List<MyNodeModel> nodes, List<ArrowModel> arrows, String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Create a wrapper object that holds both lists
            AppState state = new AppState(nodes, arrows);
            // Write JSON to the file
            mapper.writeValue(new File(filePath), state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public AppState loadFromJson(Stage stage) {
        try {
            File file = showOpenFileDialog(stage);
            if (file == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            AppState state = mapper.readValue(file, AppState.class);
            return state;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File showSaveFileDialog(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MyDiagram files (*.pda)", "pda");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);

        return file;
    }

    public File showOpenFileDialog(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        // Set extension filter for your custom file type
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MyDiagram files (*.pda)", "*.pda");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(primaryStage);

        return file; // Can be null if the dialog is canceled
    }
}