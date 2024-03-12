module com.example.bakalar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;
    requires lombok;
    requires modelmapper;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;


    opens com.example.bakalar.files to com.fasterxml.jackson.databind, lombok, javafx.fxml, com.fasterxml.jackson.core;
    opens com.example.bakalar to javafx.fxml;
    exports com.example.bakalar;
    opens com.example.bakalar.logic to lombok, javafx.fxml;
    exports com.example.bakalar.logic;
    exports com.example.bakalar.canvas.arrow;
    exports com.example.bakalar.canvas.node;
    exports com.example.bakalar.logic.transitions;
    opens com.example.bakalar.logic.transitions to javafx.fxml, lombok;
    exports com.example.bakalar.logic.conversion;
    opens com.example.bakalar.logic.conversion to javafx.fxml, lombok;
    exports com.example.bakalar.logic.history;
    opens com.example.bakalar.logic.history to javafx.fxml, lombok;
    exports com.example.bakalar.logic.utility;
    opens com.example.bakalar.logic.utility to javafx.fxml, lombok;
    opens com.example.bakalar.canvas.arrow to com.fasterxml.jackson.core, com.fasterxml.jackson.databind, javafx.fxml, lombok;
    opens com.example.bakalar.canvas.node to com.fasterxml.jackson.core, com.fasterxml.jackson.databind, javafx.fxml, lombok;
}
