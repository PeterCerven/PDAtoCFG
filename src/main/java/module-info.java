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
    exports com.example.bakalar.pda;
    exports com.example.bakalar.character;

    opens com.example.bakalar.pda to javafx.fxml;

    opens com.example.bakalar.cfg to lombok, javafx.fxml;
    exports com.example.bakalar.cfg;

    opens com.example.bakalar.utils to lombok, javafx.fxml;
    exports  com.example.bakalar.utils;

    opens com.example.bakalar.canvas to lombok, javafx.fxml; // Open the package to Lombok

    exports com.example.bakalar.canvas;
}
