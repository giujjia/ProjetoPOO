package minesweeper.model;

import java.util.ArrayList;
import java.util.List;

import minesweeper.event.CampoEvento;
import minesweeper.observer.CampoObservador;

public class Campo {

    private boolean minado;
    private boolean aberto;
    private boolean marcado;

    private final int coluna;
    private final int linha;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();

    private void notificarObservadores(CampoEvento evento){
        observadores.stream().forEach(o -> o.emitirEvento(this, evento));
    }

    Campo (int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    boolean adicionarVizinho(Campo vizinho) {
    	int diagonal = (int) (Math.sqrt(Math.pow(linha - vizinho.linha, 2) + Math.pow(coluna - vizinho.coluna, 2)));
		if (diagonal == 1) {
			vizinhos.add(vizinho);
			return true;
		}
		return false;
    }

    public void alternarMarcacao(){
        if (!this.aberto) {
            this.marcado = !marcado;
            if (marcado){
                notificarObservadores(CampoEvento.MARCAR);
            } else {
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    public boolean abrir() {
        if(!aberto && !marcado) {
            if(minado){
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }
            setAberto(true);
            notificarObservadores(CampoEvento.ABRIR);
            if(vizinhancaSegura()){
               vizinhos.forEach(vizinho -> vizinho.abrir());
            }
            return true;
        } else {
            return false;
        }

    }

    public boolean vizinhancaSegura(){
        return vizinhos.stream()
                .noneMatch(vizinho -> vizinho.minado);
    }

    void minar(){
        this.minado = true;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
        if(aberto){
            notificarObservadores(CampoEvento.ABRIR);
        }
    }

    public void registrarObservador(CampoObservador observador){
        observadores.add(observador);
    }
    
    public boolean isMarcado(){
        return this.marcado;
    }

    public boolean isAberto(){
        return this.aberto;
    }

    public boolean isMinado(){
        return this.minado;
    }

    public int getColuna() {
        return coluna;
    }

    public int getLinha() {
        return linha;
    }

    public List<Campo> getVizinhos() {
        return vizinhos;
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado & aberto;
        boolean protegido = minado & marcado;

        return desvendado || protegido;
    }

    public int minasNaVizinhanca() {
        return (int) vizinhos.stream().filter(vizinho -> vizinho.minado).count();
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }
    
    public void fornecerDica() {
        if (!aberto && !marcado) {
            notificarObservadores(CampoEvento.DICA);
        }
    }

}