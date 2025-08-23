package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.*;
import com.loja.amor_de_mamae.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VendasController {

    // Componentes FXML
    @FXML private TextField searchProduto;
    @FXML private TextField quantidade;
    @FXML private TextField clienteNome;
    @FXML private TextField clienteCpf;
    @FXML private TextField exibirtTotal;
    @FXML private ChoiceBox<String> formaDePagamento;
    @FXML private ChoiceBox<Integer> numParcelas;
    @FXML private TableView<ItemVenda> listarProdutosAdicionados;
    @FXML private TableColumn<ItemVenda, String> colunaNome;
    @FXML private TableColumn<ItemVenda, String> colunaCodigo;
    @FXML private TableColumn<ItemVenda, Integer> colunaQuantidade;
    @FXML private TableColumn<ItemVenda, Double> colunaPreco;
    @FXML private TableColumn<ItemVenda, Double> colunaTotal;
    @FXML private TableColumn<ItemVenda, Void> colunaAcao;

    private MainController mainController;
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // DAOs
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO(null);
    private final VendasDAO vendaDAO = new VendasDAO();
    private final CaixaDAO caixaDAO = new CaixaDAO();

    // Listas e variáveis de estado
    private ObservableList<ProdutoEstoqueDAO> produtosDisponiveis = FXCollections.observableArrayList();
    private ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    private ObservableList<Cliente> clientesDisponiveis = FXCollections.observableArrayList();
    private Cliente clienteSelecionado;
    private Caixa caixaAtual;
    private double totalVenda = 0.0;

    @FXML
    public void initialize() {
        try {
            carregarCaixaAtual();
            configurarTabela();
            configurarAutoComplete();
            configurarFormasPagamento();
            configurarParcelas();
            atualizarTotal();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao inicializar: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void carregarCaixaAtual() throws Exception {
        caixaAtual = caixaDAO.obterCaixaAberto();
        if (caixaAtual == null) {
            mostrarAlerta("Atenção", "Nenhum caixa aberto encontrado!", Alert.AlertType.WARNING);
        }
    }

    private void configurarTabela() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colunaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoProduto"));
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));
        colunaTotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        
        configurarColunaAcao();
        listarProdutosAdicionados.setItems(itensVenda);
    }

    private void configurarColunaAcao() {
        Callback<TableColumn<ItemVenda, Void>, TableCell<ItemVenda, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ItemVenda, Void> call(final TableColumn<ItemVenda, Void> param) {
                return new TableCell<>() {
                    private final Button btnRemover = new Button("Remover");

                    {
                        btnRemover.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 10px;");
                        btnRemover.setOnAction(event -> {
                            ItemVenda item = getTableView().getItems().get(getIndex());
                            removerItem(item);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnRemover);
                        }
                    }
                };
            }
        };

        colunaAcao.setCellFactory(cellFactory);
    }

    private void configurarAutoComplete() {
        // Auto-complete para produtos
        searchProduto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 2) {
                try {
                    produtosDisponiveis.setAll(produtoDAO.buscarProdutosComEstoque(newValue));
                    
                    // Criar sugestões (poderia ser um ListView popup)
                    if (!produtosDisponiveis.isEmpty()) {
                        // Aqui você pode implementar um popup de sugestões
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Auto-complete para clientes
        clienteNome.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 2) {
                try {
                    clientesDisponiveis.setAll(clienteDAO.buscarClientes(newValue));
                    
                    // Preencher automaticamente o CPF quando selecionar um cliente
                    if (clientesDisponiveis.size() == 1) {
                        Cliente cliente = clientesDisponiveis.get(0);
                        clienteSelecionado = cliente;
                        clienteCpf.setText(cliente.getCpf());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Buscar cliente por CPF
        clienteCpf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 11) { // CPF completo
                try {
                    Cliente cliente = clienteDAO.buscarClientePorCpf(newValue);
                    if (cliente != null) {
                        clienteSelecionado = cliente;
                        clienteNome.setText(cliente.getNome());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void configurarFormasPagamento() {
        formaDePagamento.setItems(FXCollections.observableArrayList("Dinheiro", "Cartao", "Pix", "Fiado"));
        formaDePagamento.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            numParcelas.setVisible("Fiado".equals(newValue));
        });
    }

    private void configurarParcelas() {
        ObservableList<Integer> parcelas = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            parcelas.add(i);
        }
        numParcelas.setItems(parcelas);
        numParcelas.setValue(1);
        numParcelas.setVisible(false);
    }

    @FXML
    private void adicionarProduto() {
        try {
            String termo = searchProduto.getText().trim();
            if (termo.isEmpty()) {
                mostrarAlerta("Atenção", "Digite um produto para buscar!", Alert.AlertType.WARNING);
                return;
            }

            int qtd;
            try {
                qtd = Integer.parseInt(quantidade.getText());
                if (qtd <= 0) {
                    mostrarAlerta("Erro", "Quantidade deve ser maior que zero!", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Quantidade inválida!", Alert.AlertType.ERROR);
                return;
            }

            List<ProdutoEstoqueDAO> produtos = produtoDAO.buscarProdutosComEstoque(termo);
            if (produtos.isEmpty()) {
                mostrarAlerta("Atenção", "Nenhum produto encontrado!", Alert.AlertType.WARNING);
                return;
            }

            // Para simplificar, pega o primeiro produto encontrado
            ProdutoEstoqueDAO produto = produtos.get(0);
            
            if (qtd > produto.getQuantidade()) {
                mostrarAlerta("Erro", "Quantidade indisponível em estoque!", Alert.AlertType.ERROR);
                return;
            }

            // Verificar se o produto já está na lista
            Optional<ItemVenda> itemExistente = itensVenda.stream()
                .filter(item -> item.getId_estoque() == produto.getId_estoque())
                .findFirst();

            if (itemExistente.isPresent()) {
                ItemVenda item = itemExistente.get();
                item.setQuantidade(item.getQuantidade() + qtd);
            } else {
                ItemVenda novoItem = new ItemVenda(produto.getId_estoque(), qtd, produto.getPreco());
                novoItem.setNomeProduto(produto.getNome());
                novoItem.setCodigoProduto(produto.getCodigo());
                itensVenda.add(novoItem);
            }

            atualizarTotal();
            limparCamposProduto();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao adicionar produto: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void removerItem(ItemVenda item) {
        itensVenda.remove(item);
        atualizarTotal();
    }

    private void atualizarTotal() {
        totalVenda = itensVenda.stream()
            .mapToDouble(ItemVenda::getSubtotal)
            .sum();
        exibirtTotal.setText(String.format("R$ %.2f", totalVenda));
    }

    @FXML
    private void registrarVenda() {
        try {
            if (itensVenda.isEmpty()) {
                mostrarAlerta("Atenção", "Adicione pelo menos um produto!", Alert.AlertType.WARNING);
                return;
            }

            if (formaDePagamento.getValue() == null) {
                mostrarAlerta("Atenção", "Selecione a forma de pagamento!", Alert.AlertType.WARNING);
                return;
            }

            if (caixaAtual == null) {
                mostrarAlerta("Erro", "Nenhum caixa aberto encontrado!", Alert.AlertType.ERROR);
                return;
            }

            // Confirmar venda
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Venda");
            confirmacao.setHeaderText("Deseja finalizar a venda?");
            confirmacao.setContentText(String.format("Total: R$ %.2f\nForma de pagamento: %s", totalVenda, formaDePagamento.getValue()));
            
            Optional<ButtonType> result = confirmacao.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Vendas venda = new Vendas();
                venda.setId_cliente(clienteSelecionado != null ? clienteSelecionado.getId_cliente() : 0);
                venda.setId_caixa(caixaAtual.getId_caixa());
                venda.setTotal_venda(totalVenda);
                venda.setForma_pagamento(formaDePagamento.getValue());
                venda.setData_venda(LocalDateTime.now());

                if ("Fiado".equals(formaDePagamento.getValue())) {
                    venda.setStatus_parcelado("sim");
                    venda.setNum_parcelas(numParcelas.getValue());
                } else {
                    venda.setStatus_parcelado("nao");
                    venda.setNum_parcelas(1);
                }

                // Converter ObservableList para List
                List<ItemVenda> itensList = new ArrayList<>(itensVenda);
                
                int idVenda = vendaDAO.salvarVenda(venda, itensList);
                
                mostrarAlerta("Sucesso", "Venda registrada com sucesso! ID: " + idVenda, Alert.AlertType.INFORMATION);
                limparVenda();
            }

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao registrar venda: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void limparVenda() {
        itensVenda.clear();
        clienteSelecionado = null;
        clienteNome.clear();
        clienteCpf.clear();
        formaDePagamento.getSelectionModel().clearSelection();
        numParcelas.setValue(1);
        numParcelas.setVisible(false);
        atualizarTotal();
    }

    private void limparCamposProduto() {
        searchProduto.clear();
        quantidade.setText("1");
    }

    @FXML
    private void fecharCaixa() {
        // Implementação existente do fechamento de caixa
        try {
            if (caixaAtual == null) {
                mostrarAlerta("Atenção", "Não há caixa aberto para fechar!", Alert.AlertType.WARNING);
                return;
            }

            // Abrir tela de fechar caixa
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/FecharCaixa.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Fechar Caixa");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            FecharCaixaController controller = loader.getController();
            controller.setCaixaAtual(caixaAtual);
            controller.setMainController(mainController);

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir tela de fechar caixa", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}