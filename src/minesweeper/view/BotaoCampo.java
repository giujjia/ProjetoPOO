package minesweeper.view;

import java.awt.Color;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import minesweeper.event.CampoEvento;
import minesweeper.model.Campo;
import minesweeper.observer.CampoObservador;

@SuppressWarnings({ "serial", "unused" })
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

	private Campo campo;
	private final Cores cores = new Cores();
	
    private final Icon iconBomba = new ImageIcon("src/minesweeper/img/bomb.png");
    private final Icon iconFlag = new ImageIcon("src/minesweeper/img/flag.png");
    private final Icon iconHint = new ImageIcon("src/minesweeper/img/hint.png");

	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBorder(BorderFactory.createBevelBorder(0));
		setBackground(this.cores.getPadrao());
		setOpaque(true);
		addMouseListener(this);
		campo.registrarObservador(this);
	}

	@Override
	public void emitirEvento(Campo campo, CampoEvento event) {
		switch (event) {
		case ABRIR:
			this.aplicarEstiloAbrir();
			break;
		case MARCAR:
			this.aplicarEstiloMarcar();
			break;
		case EXPLODIR:
			this.aplicarEstiloExplodir();
			break;
		case DICA:
            this.aplicarEstiloDica(); // Add this line
            break;	
		default:
			this.aplicarEstiloPadrao();
			break;
		}
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	private void aplicarEstiloPadrao() {
		setBackground(this.cores.getPadrao());
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
		setIcon(null);
	}

	private void aplicarEstiloAbrir() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		if(this.campo.isMinado()) {
			this.aplicarEstiloExplodir();
			return;
		}
		
		setBackground(this.cores.getPadrao());
		
		switch (this.campo.minasNaVizinhanca()) {
		case 1:
			setForeground(this.cores.getTextoVerde());
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}

		String valor = !this.campo.vizinhancaSegura() ? this.campo.minasNaVizinhanca() + "" : "";
		setText(valor);

	}

	private void aplicarEstiloMarcar() {
		setBackground(this.cores.getMarcar());
		setIcon(iconFlag);
	}

	private void aplicarEstiloExplodir() {
		setBackground(this.cores.getExplodir());
		setIcon(iconBomba);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			this.campo.abrir();
		} else {
			this.campo.alternarMarcacao();
		}

	}
	
	 private void aplicarEstiloDica() { // Add this method
	        setBackground(this.cores.getPadrao());
	        setIcon(iconHint);
	    }

	public void mouseClicked(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

}
