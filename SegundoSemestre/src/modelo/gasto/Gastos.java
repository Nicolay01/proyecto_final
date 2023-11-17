package modelo.gasto;

/**
 *
 * @author decar
 */
public class Gastos {

    private int servilletas;
    private int bolsas;
    private int pasajes;

    public Gastos() {
    }

    public Gastos(int servilletas, int bolsas, int pasajes) {
        this.servilletas = servilletas;
        this.bolsas = bolsas;
        this.pasajes = pasajes;
    }

    public int getServilletas() {
        return servilletas;
    }

    public void setServilletas(int servilletas) {
        this.servilletas = servilletas;
    }

    public int getBolsas() {
        return bolsas;
    }

    public void setBolsas(int bolsas) {
        this.bolsas = bolsas;
    }

    public int getPasajes() {
        return pasajes;
    }

    public void setPasajes(int pasajes) {
        this.pasajes = pasajes;
    }

}
