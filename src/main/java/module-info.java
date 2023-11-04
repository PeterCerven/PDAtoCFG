module com.example.bakalar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;

    opens com.example.bakalar to javafx.fxml;
    exports com.example.bakalar;
    exports com.example.bakalar.pda;
    exports com.example.bakalar.character;

    opens com.example.bakalar.pda to javafx.fxml;

    exports com.example.bakalar.cfg;
    opens com.example.bakalar.cfg to tests;
    exports com.example.bakalar.canvas;
    opens com.example.bakalar.canvas to javafx.fxml;


}
