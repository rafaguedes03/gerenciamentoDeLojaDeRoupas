module com.loja.amor_de_mamae {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.loja.amor_de_mamae to javafx.fxml;
    opens com.loja.amor_de_mamae.controller to javafx.fxml;
    opens com.loja.amor_de_mamae.model to javafx.base, javafx.fxml;


    exports com.loja.amor_de_mamae;
    exports com.loja.amor_de_mamae.controller;
}
