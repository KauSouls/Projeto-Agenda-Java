package br.com.agenda.view;

import br.com.agenda.dao.ContatoDAO;
import br.com.agenda.model.Contato;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.List;

public class AgendaFrame extends JFrame {

    private final JTextField txtNome = new JTextField(20);
    private final JFormattedTextField txtTelefone;
    private final JTextField txtEmail = new JTextField(20);
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Telefone", "Email"}, 0);
    private final JTable tabela = new JTable(tableModel);
    private final ContatoDAO dao = new ContatoDAO();

    public AgendaFrame() {
        setTitle("Agenda de Contatos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtTelefone = criarCampoTelefone();

        initComponents();
        atualizarTabela();
    }

    private JFormattedTextField criarCampoTelefone() {
        try {
            MaskFormatter mask = new MaskFormatter("(##) #####-####");
            mask.setPlaceholderCharacter('_');
            return new JFormattedTextField(mask);
        } catch (ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(txtNome);
        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(txtTelefone);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir Selecionado");

        btnAdicionar.addActionListener(this::adicionarContato);
        btnAtualizar.addActionListener(this::atualizarContato);
        btnExcluir.addActionListener(this::excluirContato);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnExcluir);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(tabela);

        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String validarCampos() {
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
        String email = txtEmail.getText().trim();

        if (nome.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e telefone são obrigatórios!");
            return null;
        }

        if (!telefone.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "O telefone deve conter exatamente 11 números!");
            return null;
        }

        return telefone;
    }

    private void adicionarContato(ActionEvent e) {
        String telefone = validarCampos();
        if (telefone == null) return;

        dao.inserir(new Contato(txtNome.getText().trim(), telefone, txtEmail.getText().trim()));
        limparCampos();
        atualizarTabela();
    }

    private void atualizarContato(ActionEvent e) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um contato para atualizar.");
            return;
        }

        String telefone = validarCampos();
        if (telefone == null) return;

        int id = (int) tabela.getValueAt(selectedRow, 0);
        dao.atualizar(new Contato(id, txtNome.getText().trim(), telefone, txtEmail.getText().trim()));

        limparCampos();
        atualizarTabela();
    }

    private void excluirContato(ActionEvent e) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um contato para excluir.");
            return;
        }

        int id = (int) tabela.getValueAt(selectedRow, 0);
        dao.excluir(id);
        atualizarTabela();
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Contato> contatos = dao.listar();
        for (Contato c : contatos) {
            tableModel.addRow(new Object[]{c.getId(), c.getNome(), c.getTelefone(), c.getEmail()});
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
    }
}