package minesweeper.view;

import java.awt.Color;

public class Cores {
	
	private Color padrao;
	private Color marcar;
	private Color explodir;
	private Color textoVerde;
	
	public Cores() {
		this.setPadrao(new Color(184, 184, 184));
		this.setMarcar(new Color(184, 184, 184));
		this.setExplodir(new Color(189, 66, 68));
		this.setTextoVerde(new Color(0, 100, 0));
	}
	
	public Cores(Color padrao, Color marcar, Color explodir, Color textoVerde) {
		this.setPadrao(padrao);
		this.setMarcar(marcar);
		this.setExplodir(explodir);
		this.setTextoVerde(textoVerde);
	}
	
	public Cores(Color padrao, Color marcar, Color explodir) {
		this.setPadrao(padrao);
		this.setMarcar(marcar);
		this.setExplodir(explodir);
		this.setTextoVerde(new Color(0, 100, 0));
	}

	public Color getPadrao() {
		return padrao;
	}

	public void setPadrao(Color padrao) {
		this.padrao = padrao;
	}

	public Color getTextoVerde() {
		return textoVerde;
	}

	public void setTextoVerde(Color textoVerde) {
		this.textoVerde = textoVerde;
	}

	public Color getMarcar() {
		return marcar;
	}

	public void setMarcar(Color marcado) {
		this.marcar = marcado;
	}

	public Color getExplodir() {
		return explodir;
	}

	public void setExplodir(Color minado) {
		this.explodir = minado;
	}

}