package minesweeper.view;

import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import minesweeper.model.Tabuleiro;

public class PainelTabuleiro extends JPanel{

    private static final long serialVersionUID = 6713172556750090784L;

	public PainelTabuleiro(Tabuleiro tabuleiro) {

        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        tabuleiro.getCampos().forEach(c -> add(new BotaoCampo(c)));
        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {
                if(e.isGanhou()){
                    JOptionPane.showMessageDialog(this, "Ganhou!!!");
                } else {
                    JOptionPane.showMessageDialog(this, "Perdeu");
                }
                tabuleiro.reiniciar();
            });
        });
    }
}
