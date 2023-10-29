module tests {
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires org.junit.jupiter.params;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.engine;

    requires com.example.bakalar;
    opens tests to org.junit.platform.commons;
}