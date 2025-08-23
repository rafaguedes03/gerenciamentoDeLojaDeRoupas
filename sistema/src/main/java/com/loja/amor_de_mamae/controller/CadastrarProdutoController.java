package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.EstoqueDAO;
import com.loja.amor_de_mamae.dao.ProdutoDAO;
import com.loja.amor_de_mamae.model.Estoque;
import com.loja.amor_de_mamae.model.Produto;
import com.loja.amor_de_mamae.dao.ProdutoEstoqueDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.Optional;

public class CadastrarProdutoController {

    @FXML private TextField inputCodigo;
    @FXML private TextField inputNome;
    @FXML private TextField inputPreco;
    @FXML private TextField inputTamanho;
    @FXML private TextField inputQuantidade;
    @FXML private Button btnCadastrarProduto;
    @FXML private Button btnCancelar;

    @FXML private TableView<ProdutoEstoqueDAO> tableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, String> nomeTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, String> codigoTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, Double> precoTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, String> tamanhoTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, Integer> quantidadeTableView;
    @FXML private TableColumn<ProdutoEstoqueDAO, Void> actionTableView;

    private ObservableList<ProdutoEstoqueDAO> listaProdutos;
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private EstoqueDAO estoqueDAO = new EstoqueDAO();
    private ProdutoEstoqueDAO produtoSelecionado;

    @FXML
    public void initialize() throws Exception {
        // Configuração das colunas da tabela
        nomeTableView.setCellValueFactory(new PropertyValueFactory<>("nome"));
        codigoTableView.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        precoTableView.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tamanhoTableView.setCellValueFactory(new PropertyValueFactory<>("tamanho"));
        quantidadeTableView.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        // Configurar coluna de ação
        configurarColunaAcao();

        // Esconder botão cancelar inicialmente
        btnCancelar.setVisible(false);

        // Carregar dados iniciais
        carregarProdutos();
    }

    private void configurarColunaAcao() {
        Callback<TableColumn<ProdutoEstoqueDAO, Void>, TableCell<ProdutoEstoqueDAO, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ProdutoEstoqueDAO, Void> call(final TableColumn<ProdutoEstoqueDAO, Void> param) {
                return new TableCell<>() {
                    private final Button btnEditar = new Button("Editar");
                    private final Button btnExcluir = new Button("Excluir");

                    {
                        // Estilizar botões
                        btnEditar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 10px;");
                        btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 10px;");
                        
                        btnEditar.setOnAction(event -> {
                            ProdutoEstoqueDAO produto = getTableView().getItems().get(getIndex());
                            editarProduto(produto);
                        });
                        
                        btnExcluir.setOnAction(event -> {
                            ProdutoEstoqueDAO produto = getTableView().getItems().get(getIndex());
                            excluirProduto(produto);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(new javafx.scene.layout.HBox(5, btnEditar, btnExcluir));
                        }
                    }
                };
            }
        };

        actionTableView.setCellFactory(cellFactory);
    }

    private void editarProduto(ProdutoEstoqueDAO produto) {
        produtoSelecionado = produto;
        
        // Preencher os campos com os dados do produto
        inputCodigo.setText(produto.getCodigo());
        inputNome.setText(produto.getNome());
        inputPreco.setText(String.valueOf(produto.getPreco()));
        inputTamanho.setText(produto.getTamanho());
        inputQuantidade.setText(String.valueOf(produto.getQuantidade()));
        
        // Mudar para modo edição
        btnCadastrarProduto.setText("ATUALIZAR");
        btnCancelar.setVisible(true);
    }

    private void excluirProduto(ProdutoEstoqueDAO produto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Tem certeza que deseja excluir este produto?");
        alert.setContentText("Produto: " + produto.getNome() + "\nCódigo: " + produto.getCodigo());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Primeiro excluir do estoque
                estoqueDAO.excluirPorProduto(produto.getId_produto());
                // Depois excluir o produto
                produtoDAO.excluir(produto.getId_produto());
                
                mostrarAlerta("Sucesso", "Produto excluído com sucesso!", Alert.AlertType.INFORMATION);
                carregarProdutos();
            } catch (Exception e) {
                mostrarAlerta("Erro", "Erro ao excluir produto: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnCadastrarProduto() {
        try {
            // Validar campos
            if (inputCodigo.getText().isEmpty() || inputNome.getText().isEmpty() || 
                inputPreco.getText().isEmpty() || inputTamanho.getText().isEmpty() || 
                inputQuantidade.getText().isEmpty()) {
                mostrarAlerta("Erro", "Preencha todos os campos!", Alert.AlertType.WARNING);
                return;
            }

            String codigo = inputCodigo.getText();
            String nome = inputNome.getText();
            double preco = Double.parseDouble(inputPreco.getText());
            String tamanho = inputTamanho.getText();
            int quantidade = Integer.parseInt(inputQuantidade.getText());

            if (produtoSelecionado != null) {
                // Modo edição
                Produto produto = new Produto(nome, codigo, preco);
                produto.setIdProduto(produtoSelecionado.getId_produto());
                
                Estoque estoque = new Estoque(produtoSelecionado.getId_produto(), tamanho, quantidade);
                
                produtoDAO.atualizar(produto);
                estoqueDAO.atualizar(estoque);
                
                mostrarAlerta("Sucesso", "Produto atualizado com sucesso!", Alert.AlertType.INFORMATION);
                cancelarEdicao();
            } else {
                // Modo cadastro
                Produto produto = new Produto(nome, codigo, preco);
                produtoDAO.salvar(produto);
                int idProduto = produto.getIdProduto();

                Estoque estoque = new Estoque(idProduto, tamanho, quantidade);
                estoqueDAO.salvar(estoque);
                
                mostrarAlerta("Sucesso", "Produto cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }

            limparCampos();
            carregarProdutos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Preço ou quantidade inválidos!", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao salvar produto: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelarEdicao() {
        produtoSelecionado = null;
        limparCampos();
        btnCadastrarProduto.setText("CADASTRAR");
        btnCancelar.setVisible(false);
    }

    private void carregarProdutos() throws Exception {
        listaProdutos = FXCollections.observableArrayList(produtoDAO.listarComEstoque());
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