module com.example.bakalar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires lombok;

    opens com.example.bakalar to javafx.fxml;
    exports com.example.bakalar;
    exports com.example.bakalar.character;

    opens com.example.bakalar.cfg to lombok, javafx.fxml;
    exports com.example.bakalar.cfg;

    opens com.example.bakalar.canvas to lombok, javafx.fxml; // Open the package to Lombok

    exports com.example.bakalar.canvas;
    exports com.example.bakalar.canvas.arrow;
    opens com.example.bakalar.canvas.arrow to javafx.fxml, lombok;
    exports com.example.bakalar.canvas.node;
    opens com.example.bakalar.canvas.node to javafx.fxml, lombok;
    exports com.example.bakalar.canvas.transitions;
    opens com.example.bakalar.canvas.transitions to javafx.fxml, lombok;
    exports com.example.bakalar.canvas.conversion;
    opens com.example.bakalar.canvas.conversion to javafx.fxml, lombok;
}
