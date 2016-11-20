package pe.edu.ulima.ufood.interfaces;

import java.util.List;

import pe.edu.ulima.ufood.bean.Item;

public interface JugoDesayunoView {
    void obtenerLista(List<Item> items, int division);
}
