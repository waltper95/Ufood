package pe.edu.ulima.ufood.bean;

import java.io.Serializable;

public class Item implements Serializable{
    public String nombre;
    public float precio;
    public float calorias;

    public Item(String nombre, float precio, float calorias) {
        this.nombre = nombre;
        this.precio = precio;
        this.calorias = calorias;
    }

    public Item() {
    }
}
