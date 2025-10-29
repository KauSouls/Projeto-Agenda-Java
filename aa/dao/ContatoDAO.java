package br.com.agenda.dao;

import br.com.agenda.dataBase.ConnectionFactory;
import br.com.agenda.model.Contato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {

    // ✅ Cria a tabela se não existir
    public static void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS contatos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                telefone TEXT NOT NULL,
                email TEXT
            );
            """;

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'contatos' verificada/criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    // ✅ Inserir contato
    public void inserir(Contato contato) {
        String sql = "INSERT INTO contatos (nome, telefone, email) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());
            stmt.executeUpdate();

            System.out.println("Contato adicionado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir contato: " + e.getMessage());
        }
    }

    // ✅ Listar todos os contatos
    public List<Contato> listar() {
        List<Contato> contatos = new ArrayList<>();
        String sql = "SELECT * FROM contatos ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Contato c = new Contato(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email")
                );
                contatos.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar contatos: " + e.getMessage());
        }

        return contatos;
    }

    // ✅ Atualizar contato
    public void atualizar(Contato contato) {
        String sql = "UPDATE contatos SET nome = ?, telefone = ?, email = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());
            stmt.setInt(4, contato.getId());

            stmt.executeUpdate();
            System.out.println("Contato atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar contato: " + e.getMessage());
        }
    }

    // ✅ Excluir contato
    public void excluir(int id) {
        String sql = "DELETE FROM contatos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Contato removido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir contato: " + e.getMessage());
        }
    }
}