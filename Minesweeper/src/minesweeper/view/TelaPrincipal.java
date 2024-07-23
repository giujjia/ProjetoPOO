package minesweeper.view;

import javax.swing.*;

import minesweeper.model.Campo;
import minesweeper.model.Tabuleiro;

import java.awt.BorderLayout;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5, 5);
        PainelTabuleiro painelTabuleiro = new PainelTabuleiro(tabuleiro);

        getContentPane().add(painelTabuleiro);

        JButton btnDica = new JButton("Dica");
        btnDica.addActionListener(e -> fornecerDica(tabuleiro));
        getContentPane().add(btnDica, BorderLayout.NORTH);

        setTitle("Campo Minado");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void fornecerDica(Tabuleiro tabuleiro) {
        Campo campoComMina = tabuleiro.fornecerDica();
        if (campoComMina == null && tabuleiro.getHintsGiven() >= Tabuleiro.getMaxHints()) {
            JOptionPane.showMessageDialog(this, "Você já usou todas as suas dicas!");
        } else if (campoComMina == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma dica disponível!");
        }
    }

    public static void main(String[] args) {
        new TelaPrincipal();
    }
}
