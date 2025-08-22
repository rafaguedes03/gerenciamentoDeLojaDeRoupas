package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.EstoqueDAO;
import com.loja.amor_de_mamae.dao.ProdutoDAO;
import com.loja.amor_de_mamae.model.Estoque;
import com.loja.amor_de_mamae.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.loja.amor_de_mamae.dao.ProdutoEstoqueDAO;

public class CadastrarProdutoController {

    @FXML private TextField inputCodigo;
    @FXML private TextField inputNome;
    @FXML private TextField inputPreco;
    @FXML private TextField inputTamanho;
    @FXML private TextField inputQuantidade;

    @FXML private TableView<ProdutoEstoqueDAO> tableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, String> nomeTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, String> codigoTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, Double> precoTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, String> tamanhoTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, Integer> quantidadeTableView;

    private ObservableList<ProdutoEstoqueDAO> listaProdutos;
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private EstoqueDAO estoqueDAO = new EstoqueDAO();

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
            // coletar dados dos campos
            String codigo = inputCodigo.getText();
            String nome = inputNome.getText();
            double preco = Double.parseDouble(inputPreco.getText());
            String tamanho = inputTamanho.getText();
            int quantidade = Integer.parseInt(inputQuantidade.getText());

            if (codigo.isEmpty() || nome.isEmpty() || tamanho.isEmpty()) {
                mostrarAlerta("Erro", "Preencha todos os campos!", Alert.AlertType.WARNING);
                return;
            }

            Produto p = new Produto(nome, codigo, preco);
            produtoDAO.salvar(p); // metodo salvar do ProdutoDAO e retorna o id do produto
            int idProduto = p.getIdProduto(); // pega o id do produto salvo

            Estoque estoque = new Estoque(idProduto, tamanho, quantidade);
            estoqueDAO.salvar(estoque); // salva o estoque com o id do produto

            
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
        // O método listar() do ProdutoDAO precisará ser alterado para fazer um JOIN entre Produtos e Estoque e retornar uma lista de ProdutoEstoqueDTO
        listaProdutos = FXCollections.observableArrayList(produtoDAO.listarComEstoque());
        // Atualizar a tabela com os dados
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
