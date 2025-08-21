package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.ProdutoDAO;
import com.loja.amor_de_mamae.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CadastrarProdutoController {

    @FXML private TextField inputCodigo;
    @FXML private TextField inputNome;
    @FXML private TextField inputPreco;
    @FXML private TextField inputTamanho;
    @FXML private TextField inputQuantidade;

    @FXML private TableView<Produto> tableView;
    @FXML private TableColumn<Produto, String> nomeTableView;
    @FXML private TableColumn<Produto, String> codigoTableView;
    @FXML private TableColumn<Produto, Double> precoTableView;
    @FXML private TableColumn<Produto, String> tamanhoTableView;
    @FXML private TableColumn<Produto, Integer> quantidadeTableView;

    private ObservableList<Produto> listaProdutos;
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    @FXML
    public void initialize() throws Exception {
        // Configuração das colunas da tabela
        nomeTableView.setCellValueFactory(new PropertyValueFactory<>("nome"));
        codigoTableView.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        precoTableView.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tamanhoTableView.setCellValueFactory(new PropertyValueFactory<>("tamanho"));
        quantidadeTableView.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        // Carregar dados iniciais
        carregarProdutos();
    }

    @FXML
    private void btnCadastrarProduto() {
        try {
            String codigo = inputCodigo.getText();
            String nome = inputNome.getText();
            double preco = Double.parseDouble(inputPreco.getText());
            String tamanho = inputTamanho.getText();
            int quantidade = Integer.parseInt(inputQuantidade.getText());

            if (codigo.isEmpty() || nome.isEmpty() || tamanho.isEmpty()) {
                mostrarAlerta("Erro", "Preencha todos os campos!", Alert.AlertType.WARNING);
                return;
            }

            Produto p = new Produto();
            p.setCodigo(codigo);
            p.setNome(nome);
            p.setPreco(preco);
            p.setTamanho(tamanho);
            p.setQuantidade(quantidade);

            produtoDAO.salvar(p);

            mostrarAlerta("Sucesso", "Produto cadastrado com sucesso!", Alert.AlertType.INFORMATION);

            limparCampos();
            carregarProdutos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Preço inválido!", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao cadastrar produto: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void carregarProdutos() throws Exception {
        listaProdutos = FXCollections.observableArrayList(produtoDAO.listar());
        tableView.setItems(listaProdutos);
    }

    private void limparCampos() {
        inputCodigo.clear();
        inputNome.clear();
        inputPreco.clear();
        inputTamanho.clear();
        inputQuantidade.clear();
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
