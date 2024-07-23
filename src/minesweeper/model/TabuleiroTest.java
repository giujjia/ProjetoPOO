package minesweeper.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TabuleiroTest {

	@Test
	void test() {
	    Tabuleiro tabuleiro = new Tabuleiro(4, 4, 5); // Crie um tabuleiro com dimensões e minas desejadas
	    tabuleiro.testarEliminacaoGaussiana();
	}

}
