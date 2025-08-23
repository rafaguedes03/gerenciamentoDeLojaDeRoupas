package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.UsuarioDAO;
import com.loja.amor_de_mamae.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;

    public void entrar(ActionEvent e) {
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.login(txtUsuario.getText(), txtSenha.getText());

        if (u != null) {
            try {
                Stage stage = (Stage) txtUsuario.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/Main.fxml"));

                stage.setScene(new Scene(loader.load()));

                MainController mainController = loader.getController();
                mainController.setUsuario(u);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Usuário ou senha inválidos!");
        }
    }

    public void cadastrarUsuario(ActionEvent e) {
        try {
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/CadastrarUsuario.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
