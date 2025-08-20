package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

public class MainController {

    @FXML private Pane paneConteudo;


    // Botões laterais
    @FXML private Button btnCadastrarProduto;
    @FXML private Button btnCadastrarCliente;
    @FXML private Button btnRelatorios;
    @FXML private Button btnVendas;
    @FXML private Button btnFiados;
    @FXML private Button btnSair;

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
            btnCadastrarProduto.setVisible(false);
            btnCadastrarCliente.setVisible(false);
            btnRelatorios.setVisible(false);
        }
        // ADMIN mantém todos os botões visíveis
        }

        private void configurarEventosBotoes() {
        btnVendas.setOnAction(e -> mainTabPane.getSelectionModel().select(vendasTab));
        btnCadastrarProduto.setOnAction(e -> mainTabPane.getSelectionModel().select(cadastrarProdutosTab));
        btnCadastrarCliente.setOnAction(e -> mainTabPane.getSelectionModel().select(cadastrarClientesTab));
        btnFiados.setOnAction(e -> mainTabPane.getSelectionModel().select(fiadosTab));
        btnRelatorios.setOnAction(e -> mainTabPane.getSelectionModel().select(relatoriosTab));
        btnSair.setOnAction(e -> exit());
        }

        // Lista de métodos para cada aba
        @FXML
        private void abrirVendas() {
        // Lógica para abrir a aba de vendas
        mainTabPane.getSelectionModel().select(vendasTab);
        }

        @FXML
        private void abrirCadastrarProduto() {
        // Lógica para cadastrar produto
        mainTabPane.getSelectionModel().select(cadastrarProdutosTab);
        }

        @FXML
        private void abrirCadastrarCliente() {
        // Lógica para cadastrar cliente   
        mainTabPane.getSelectionModel().select(cadastrarClientesTab);
        }

        @FXML
        private void abrirFiados() {
        // Lógica para visualizar fiados
        mainTabPane.getSelectionModel().select(fiadosTab);
        }

        @FXML
        private void abrirRelatorios() {
        // Lógica para gerar relatórios
        mainTabPane.getSelectionModel().select(relatoriosTab);
        }


        @FXML
        private void exit() {
        // Lógica para sair do sistema ou voltar para login
        Stage stage = (Stage) btnSair.getScene().getWindow();
        stage.close();
        System.exit(0);
        }

    }// Aqui você pode adicionar outros métodos relacionados às abas
