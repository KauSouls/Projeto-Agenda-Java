package br.com.agenda;

import br.com.agenda.dao.ContatoDAO;
import br.com.agenda.view.AgendaFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ContatoDAO.criarTabela();

        SwingUtilities.invokeLater(() -> {
            new AgendaFrame().setVisible(true);
        });
    }
}