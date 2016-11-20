package pe.edu.ulima.ufood.interfaces;

import java.util.List;

import pe.edu.ulima.ufood.bean.Pedido;

public interface HistoriaView {
    void obtenerLista(List<Pedido> pedidos);
}
