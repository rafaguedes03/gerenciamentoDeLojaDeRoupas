module com.loja.amor_de_mamae {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires itextpdf;
    requires jbcrypt;

    opens com.loja.amor_de_mamae to javafx.fxml;
    opens com.loja.amor_de_mamae.controller to javafx.fxml;
    opens com.loja.amor_de_mamae.model to javafx.base, javafx.fxml;
    opens com.loja.amor_de_mamae.dao to javafx.base;
    opens com.loja.amor_de_mamae.view to javafx.fxml;

    exports com.loja.amor_de_mamae;
    exports com.loja.amor_de_mamae.controller;
}
