package pe.edu.ulima.ufood.interfaces;

import pe.edu.ulima.ufood.bean.Item;

public interface UFoodView {
    void showNotificaciones();
    void showPayment(Item item);
    void mostrarMensaje(String mensaje);
}
