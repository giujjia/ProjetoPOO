package minesweeper.observer;

import minesweeper.event.CampoEvento;
import minesweeper.model.Campo;

public interface CampoObservador {
    public void emitirEvento(Campo campo, CampoEvento evento);
}