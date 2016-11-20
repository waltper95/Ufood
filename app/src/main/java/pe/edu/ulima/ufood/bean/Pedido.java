package pe.edu.ulima.ufood.bean;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Pedido {
    public long fecha;
    public String item;
    public float precio;
    public boolean listo;
    public boolean cancelado;
    public long cancelacion;
    public String codigo;

    public Pedido() {
    }

    public Pedido(long fecha, String item, float precio, boolean listo) {
        this.fecha = fecha;
        this.item = item;
        this.precio = precio;
        this.listo = listo;
    }
}
