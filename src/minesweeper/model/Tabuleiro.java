package minesweeper.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import minesweeper.event.CampoEvento;
import minesweeper.event.ResultadoEvento;
import minesweeper.observer.CampoObservador;

public class Tabuleiro implements CampoObservador {

    private int linhas;
    private int colunas;
    private int quantidadeMinas;
    private int hintsGiven = 0;
    private static final int MAX_HINTS = 3;


    private final List<Campo> campos = new ArrayList<>();
    private List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int quantidadeMinas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.quantidadeMinas = quantidadeMinas;

        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    private void gerarCampos() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {

                Campo campo = new Campo(linha, coluna);
                campo.registrarObservador(this);
                campos.add(campo);
            }
        }

    }

    private void mostrarMinas() {
        campos.stream()
                .filter(c -> c.isMinado())
                .filter(c -> !c.isMarcado())
                .forEach(campo -> campo.setAberto(true));
    }

    private void associarVizinhos() {
        for (Campo campo1 : campos) {
            for (Campo campo2 : campos) {
                campo1.adicionarVizinho(campo2);
            }
        }

    }

    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = campo -> campo.isMinado();

        while (minasArmadas < this.quantidadeMinas){
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();
        }
    }

    private void notificarObservadores(Boolean resultado){
        observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
    }

    public void registrarObservador(Consumer<ResultadoEvento> observador){
        observadores.add(observador);
    }

    public boolean objetivoAlcancado() {
        return campos.stream().allMatch(campo -> campo.objetivoAlcancado());
    }

    public void reiniciar(){
        campos.stream().forEach(campo -> campo.reiniciar());
        sortearMinas();
        resetHints();
    }

    public void abrir(int linha, int coluna){
        campos.parallelStream().filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.abrir());
    }

    public void marcar(int linha, int coluna){
        campos.parallelStream().filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.alternarMarcacao());
    }

    //Getters and Setters
    public List<Campo> getCampos() {
        return campos;
    }

    public int getQuantidadeMinas() {
        return quantidadeMinas;
    }
    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public int getHintsGiven() {
        return hintsGiven;
    }

    public void resetHints() {
        hintsGiven = 0;
    }

    public Campo fornecerDica() {
        if (hintsGiven >= MAX_HINTS) {
            return null; // No more hints allowed
        }

        // Matriz do sistema linear
        double[][] matriz = new double[campos.size()][campos.size() + 1];
        // Preencher a matriz com os dados (simplificado)
        for (int i = 0; i < campos.size(); i++) {
            Campo campo = campos.get(i);
            matriz[i][i] = 1;
            matriz[i][campos.size()] = campo.isMinado() ? 1 : 0;
        }

        resolverSistemaLinear(matriz);

        // Coletar todos os campos minados
        List<Campo> minedFields = new ArrayList<>();
        for (int i = 0; i < campos.size(); i++) {
            if (matriz[i][campos.size()] == 1) {
                minedFields.add(campos.get(i));
            }
        }

        // Selecionar um campo aleatório
        if (!minedFields.isEmpty()) {
            hintsGiven++;
            int randomIndex = (int) (Math.random() * minedFields.size());
            Campo campoComMina = minedFields.get(randomIndex);
            campoComMina.fornecerDica();
            return campoComMina;
        }
        return null;
    }


    private void resolverSistemaLinear(double[][] matriz) {
        int n = matriz.length;
        for (int i = 0; i < n; i++) {
            // Normalizar a linha
            double coeficiente = matriz[i][i];
            for (int j = 0; j <= n; j++) {
                matriz[i][j] /= coeficiente;
            }

            // Eliminar as outras linhas
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double fator = matriz[k][i];
                    for (int j = 0; j <= n; j++) {
                        matriz[k][j] -= fator * matriz[i][j];
                    }
                }
            }
        }
    }

	@Override
	public void emitirEvento(Campo campo, CampoEvento evento) {
	        if(evento == CampoEvento.EXPLODIR) {
	           this.mostrarMinas();
	           this.notificarObservadores(false);
	        } else if (this.objetivoAlcancado()) {
	            this.notificarObservadores(true);
	        }
	}

	public static int getMaxHints() {
		return MAX_HINTS;
	}
	
	public void testarEliminacaoGaussiana() {
	    // Exibir o campo minado inicial
	    System.out.println("Campo Minado Inicial:");
	    imprimirCampo();

	    // Construir a matriz do sistema linear
	    double[][] matriz = new double[campos.size()][campos.size() + 1];
	    for (int i = 0; i < campos.size(); i++) {
	        Campo campo = campos.get(i);
	        matriz[i][i] = 1;
	        matriz[i][campos.size()] = campo.isMinado() ? 1 : 0;
	    }

	    // Exibir a matriz inicial
	    System.out.println("\nMatriz Inicial:");
	    imprimirMatriz(matriz);

	    // Aplicar eliminação gaussiana
	    resolverSistemaLinear(matriz);

	    // Exibir a matriz após eliminação gaussiana
	    System.out.println("\nMatriz Após Eliminação Gaussiana:");
	    imprimirMatriz(matriz);

	    // Coletar todos os campos minados
	    List<Campo> minedFields = new ArrayList<>();
	    for (int i = 0; i < campos.size(); i++) {
	        if (matriz[i][campos.size()] == 1) {
	            minedFields.add(campos.get(i));
	        }
	    }

	    // Exibir as minas calculadas
	    System.out.println("\nMinas Calculadas pelo Método Gaussiano:");
	    for (Campo campo : minedFields) {
	        System.out.println("Mina em (" + campo.getLinha() + ", " + campo.getColuna() + ")");
	    }
	}

	private void imprimirCampo() {
	    for (int linha = 0; linha < linhas; linha++) {
	        for (int coluna = 0; coluna < colunas; coluna++) {
	            Campo campo = campos.get(linha * colunas + coluna);
	            if (campo.isMinado()) {
	                System.out.print("M ");
	            } else {
	                System.out.print(". ");
	            }
	        }
	        System.out.println();
	    }
	}

	private void imprimirMatriz(double[][] matriz) {
	    for (int i = 0; i < matriz.length; i++) {
	        for (int j = 0; j < matriz[i].length; j++) {
	            System.out.printf("%.2f ", matriz[i][j]);
	        }
	        System.out.println();
	    }
	}


}
