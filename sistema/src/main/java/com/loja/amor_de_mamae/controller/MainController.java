package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainController {

    @FXML private Button btnVendas;
    @FXML private Button btnCadastrarProduto;
    @FXML private Button btnCadastrarCliente;
    @FXML private Button btnFiados;
    @FXML private Button btnRelatorios;
    @FXML private Button btnSair;

    @FXML private Pane paneConteudo;

    private Usuario usuarioLogado;

    // Recebe o usuário logado
    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
        ajustarBotoesPorPerfil();
    }

    // Esconde botões se for funcionário
    private void ajustarBotoesPorPerfil() {
        if (usuarioLogado != null && "Funcionario".equalsIgnoreCase(usuarioLogado.getTipo())) {
            btnCadastrarProduto.setVisible(false);
            btnCadastrarCliente.setVisible(false);
            btnRelatorios.setVisible(false);
        }
    }

    // 🔹 Funções de navegação
    @FXML
    private void abrirVendas() {
        carregarTela("/com/loja/amor_de_mamae/view/vendas.fxml");
    }

    @FXML
    private void abrirCadastrarProduto() {
        carregarTela("/com/loja/amor_de_mamae/view/cadastrar_produto.fxml");
    }

    @FXML
    private void abrirCadastrarCliente() {
        carregarTela("/com/loja/amor_de_mamae/view/cadastrar_cliente.fxml");
    }

    @FXML
    private void abrirFiados() {
        carregarTela("/com/loja/amor_de_mamae/view/fiados.fxml");
    }

    @FXML
    private void abrirRelatorios() {
        carregarTela("/com/loja/amor_de_mamae/view/relatorios.fxml");
    }

    @FXML
    private void exit() {
        // Fecha a aplicação
        System.exit(0);
    }

    // Método genérico para carregar telas no paneConteudo
    private void carregarTela(String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Node node = loader.load();

            // substitui conteúdo do Pane
            paneConteudo.getChildren().setAll(node);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao carregar tela");
            alert.setHeaderText("Não foi possível abrir a tela");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
