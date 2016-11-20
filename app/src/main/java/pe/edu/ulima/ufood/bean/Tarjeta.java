package pe.edu.ulima.ufood.bean;

public class Tarjeta {
    public long numero;
    public int mm;
    public int yy;
    public int cvv;

    public Tarjeta() {
    }

    public Tarjeta(long numero, int mm, int yy, int cvv) {
        this.numero = numero;
        this.mm = mm;
        this.yy = yy;
        this.cvv = cvv;
    }
}
