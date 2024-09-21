module com.example.bakalar {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    exports com.example.bakalar;
    exports com.example.bakalar.canvas;
    exports com.example.bakalar.files;
    exports com.example.bakalar.exceptions;
    exports com.example.bakalar.logic;
    exports com.example.bakalar.logic.conversion;
    exports com.example.bakalar.logic.history;
    exports com.example.bakalar.logic.utility;
    exports com.example.bakalar.canvas.arrow;
    exports com.example.bakalar.canvas.node;
    exports com.example.bakalar.logic.conversion.window;
    exports com.example.bakalar.logic.utility.sorters;
    exports com.example.bakalar.logic.conversion.simplify;

    opens com.example.bakalar.files to com.fasterxml.jackson.databind, lombok, javafx.fxml, com.fasterxml.jackson.core;
    opens com.example.bakalar to javafx.fxml;
    opens com.example.bakalar.logic to lombok, javafx.fxml;
    opens com.example.bakalar.logic.conversion to javafx.fxml, lombok;
    opens com.example.bakalar.logic.utility to javafx.fxml, lombok;
    opens com.example.bakalar.canvas.arrow to com.fasterxml.jackson.core, com.fasterxml.jackson.databind, javafx.fxml, lombok;
    opens com.example.bakalar.canvas.node to com.fasterxml.jackson.core, com.fasterxml.jackson.databind, javafx.fxml, lombok;
    opens com.example.bakalar.logic.history to com.fasterxml.jackson.core, com.fasterxml.jackson.databind, javafx.fxml, lombok;
    opens com.example.bakalar.logic.conversion.window to javafx.fxml, lombok;
    opens com.example.bakalar.logic.utility.sorters to javafx.fxml, lombok;
    opens com.example.bakalar.logic.conversion.simplify to javafx.fxml, lombok;
}
