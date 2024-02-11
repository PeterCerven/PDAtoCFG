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

    opens com.example.bakalar to javafx.fxml;
    exports com.example.bakalar;
    exports com.example.bakalar.logic.character;
    exports com.example.bakalar.logic.button;



    opens com.example.bakalar.logic to lombok, javafx.fxml; // Open the package to Lombok
    exports com.example.bakalar.logic;
    exports com.example.bakalar.canvas.arrow;
    opens com.example.bakalar.canvas.arrow to javafx.fxml, lombok;
    exports com.example.bakalar.canvas.node;
    opens com.example.bakalar.canvas.node to javafx.fxml, lombok;
    exports com.example.bakalar.logic.transitions;
    opens com.example.bakalar.logic.transitions to javafx.fxml, lombok;
    exports com.example.bakalar.logic.conversion;
    opens com.example.bakalar.logic.conversion to javafx.fxml, lombok;
    exports com.example.bakalar.logic.history;
    opens com.example.bakalar.logic.history to javafx.fxml, lombok;
}
