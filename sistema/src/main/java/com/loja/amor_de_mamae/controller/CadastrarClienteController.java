package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.util.MaskUtil;
import com.loja.amor_de_mamae.dao.ClienteDAO;
import com.loja.amor_de_mamae.model.Cliente;
import com.loja.amor_de_mamae.model.Usuario;
import com.loja.amor_de_mamae.util.ConexaoMySQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class CadastrarClienteController {

    @FXML private TextField InputNome;
    @FXML private TextField inputCpf;
    @FXML private TextField inputEmail;
    @FXML private DatePicker dataNascimento;
    @FXML private Button btnCadastrarCliente;

    @FXML private TableView<Cliente> tableView;
    @FXML private TableColumn<Cliente, String> nomeTableView;
    @FXML private TableColumn<Cliente, LocalDate> dataTableView;
    @FXML private TableColumn<Cliente, String> emailTableView;
    @FXML private TableColumn<Cliente, String> cpfTableView;
    @FXML private TableColumn<Cliente, Void> actionTableView;

    private ClienteDAO clienteDAO;
    private ObservableList<Cliente> listaClientes;
    private Cliente clienteSelecionado;
    private Usuario usuarioLogado;

    public void initialize() throws Exception {
        MaskUtil.cpfField(inputCpf);
        MaskUtil.dateField(dataNascimento);

        Connection conn = ConexaoMySQL.getConnection();
        clienteDAO = new ClienteDAO(conn);

        nomeTableView.setCellValueFactory(new PropertyValueFactory<>("nome"));
        dataTableView.setCellValueFactory(new PropertyValueFactory<>("data_nascimento"));
        emailTableView.setCellValueFactory(new PropertyValueFactory<>("email"));
        cpfTableView.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        configurarColunaAcao();
        carregarClientes();
    }

    // Método para passar o usuário logado depois do FXML ser carregado
    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
        ajustarBotoesPorPerfil();
        // Atualiza a coluna de ação da tabela para desabilitar editar/excluir
        configurarColunaAcao();
    }

    private void ajustarBotoesPorPerfil() {
        if (usuarioLogado != null && "Funcionario".equalsIgnoreCase(usuarioLogado.getTipo())) {
            if (btnCadastrarCliente != null) btnCadastrarCliente.setDisable(true);
        }
    }

    private void configurarColunaAcao() {
        Callback<TableColumn<Cliente, Void>, TableCell<Cliente, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");

            {
                btnEditar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 10px; -fx-cursor: hand;");
                btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 10px; -fx-cursor: hand;");

                btnEditar.setOnAction(event -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    editarCliente(cliente);
                });

                btnExcluir.setOnAction(event -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    excluirCliente(cliente);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Desabilita botões se for funcionário
                    if (usuarioLogado != null && "Funcionario".equalsIgnoreCase(usuarioLogado.getTipo())) {
                        btnEditar.setDisable(true);
                        btnExcluir.setDisable(true);
                    } else {
                        btnEditar.setDisable(false);
                        btnExcluir.setDisable(false);
                    }
                    setGraphic(new HBox(5, btnEditar, btnExcluir));
                }
            }
        };

        actionTableView.setCellFactory(cellFactory);
    }

    private void editarCliente(Cliente cliente) {
        clienteSelecionado = cliente;
        InputNome.setText(cliente.getNome());
        inputCpf.setText(cliente.getCpf());
        inputEmail.setText(cliente.getEmail());
        dataNascimento.setValue(cliente.getData_nascimento());
    }

    private void excluirCliente(Cliente cliente) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Tem certeza que deseja excluir este cliente?");
        alert.setContentText("Cliente: " + cliente.getNome() + "\nCPF: " + cliente.getCpf());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                clienteDAO.deletar(cliente.getId_cliente());
                mostrarAlerta("Sucesso", "Cliente excluído com sucesso!");
                carregarClientes();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Erro ao excluir cliente: " + e.getMessage());
            }
        }
    }

    @FXML
    private void CadastrarCliente() {
        if (InputNome.getText().isEmpty() || inputCpf.getText().isEmpty() ||
            inputEmail.getText().isEmpty() || dataNascimento.getValue() == null) {
            mostrarAlerta("Atenção", "Por favor, preencha todos os campos!");
            return;
        }

        try {
            Cliente cliente;
            if (clienteSelecionado != null) {
                cliente = clienteSelecionado;
                cliente.setNome(InputNome.getText().trim());
                cliente.setCpf(inputCpf.getText().trim());
                cliente.setEmail(inputEmail.getText().trim());
                cliente.setData_nascimento(dataNascimento.getValue());
                clienteDAO.atualizar(cliente);
                mostrarAlerta("Sucesso", "Cliente atualizado com sucesso!");
                clienteSelecionado = null;
            } else {
                cliente = new Cliente();
                cliente.setNome(InputNome.getText().trim());
                cliente.setCpf(inputCpf.getText().trim());
                cliente.setEmail(inputEmail.getText().trim());
                cliente.setData_nascimento(dataNascimento.getValue());
                clienteDAO.inserir(cliente);
                mostrarAlerta("Sucesso", "Cliente cadastrado com sucesso!");
            }

            carregarClientes();
            limparCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao salvar cliente: " + e.getMessage());
        }
    }

    private void carregarClientes() {
        try {
            listaClientes = FXCollections.observableArrayList(clienteDAO.listarTodos());
            tableView.setItems(listaClientes);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void limparCampos() {
        InputNome.clear();
        inputCpf.clear();
        inputEmail.clear();
        dataNascimento.setValue(null);
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void cancelarEdicao() {
        clienteSelecionado = null;
        limparCampos();
    }
}
