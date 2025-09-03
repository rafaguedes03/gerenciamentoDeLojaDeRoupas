package com.loja.amor_de_mamae;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Amor de Mamãe - Sistema de Vendas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
