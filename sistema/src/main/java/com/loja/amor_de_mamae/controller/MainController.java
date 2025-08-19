package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainController {

    // Botões laterais
    @FXML private Button btn_cadastrar_produto;
    @FXML private Button btn_cadastrar_cliente;
    @FXML private Button btn_relatorios;
    @FXML private Button btn_vendas;
    @FXML private Button btn_visualizar_fiados;
    @FXML private Button btn_exit;

    // TabPane e abas
    @FXML private TabPane mainTabPane;
    @FXML private Tab vendasTab;
    @FXML private Tab cadastrarProdutosTab;
    @FXML private Tab cadastrarClientesTab;
    @FXML private Tab fiadosTab;
    @FXML private Tab relatoriosTab;

    private Usuario usuarioLogado;

    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
        ajustarBotoesPorPerfil();
        configurarEventosBotoes();
    }

    private void ajustarBotoesPorPerfil() {
        if (usuarioLogado.getPerfil().equals("FUNCIONARIO")) {
            btn_cadastrar_produto.setVisible(false);
            btn_cadastrar_cliente.setVisible(false);
            btn_relatorios.setVisible(false);
        }
        // ADMIN mantém todos os botões visíveis
    }

    private void configurarEventosBotoes() {
        btn_vendas.setOnAction(e -> mainTabPane.getSelectionModel().select(vendasTab));
        btn_cadastrar_produto.setOnAction(e -> mainTabPane.getSelectionModel().select(cadastrarProdutosTab));
        btn_cadastrar_cliente.setOnAction(e -> mainTabPane.getSelectionModel().select(cadastrarClientesTab));
        btn_visualizar_fiados.setOnAction(e -> mainTabPane.getSelectionModel().select(fiadosTab));
        btn_relatorios.setOnAction(e -> mainTabPane.getSelectionModel().select(relatoriosTab));
        btn_exit.setOnAction(e -> sair());
    }

    @FXML
    private void sair() {
        // Lógica para sair do sistema ou voltar para login
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    // Aqui você pode adicionar outros métodos relacionados às abas
}
