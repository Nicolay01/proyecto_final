package modelo.bollo;

/**
 *
 * @author Nicolay, David, Joshep, Sebastian.
 */
public abstract class Bollo {

    private int bolloTraido;
    private int bolloQuedado;
    private int costoBollo;
    
    public Bollo() {}

    public Bollo(int bolloTraido, int bolloQuedado) {
        this.bolloTraido = bolloTraido;
        this.bolloQuedado = bolloQuedado;
        this.costoBollo = costoBollo();
    }

    /**
     * Devuelve el total de bollos traido del mismo tipo.
     *
     * @return El total de bollos traido del mismo tipo.
     */
    public int getBolloTraido() {
        return bolloTraido;
    }

    /**
     * Total de bollos que se quedaron.
     *
     * @return total de bollo que se quedaron.
     */
    public int getBolloQuedado() {
        return bolloQuedado;
    }
    
    //DOCUMENTAR

    public void setBolloTraido(int bolloTraido) {
        this.bolloTraido = bolloTraido;
    }

    public void setBolloQuedado(int bolloQuedado) {
        this.bolloQuedado = bolloQuedado;
    }   

    /**
     * Asigna el valor del bollo dependiendo del tipo. Cada bollo puede tener un
     * valor igual al otro. Este metodo el cliente puede cambiar su valor si el
     * precio del bollo sube o vaja.
     *
     * @return El costo del bollo.
     */
    public abstract int costoBollo();

    /**
     * Calcula el total de bollos.
     * @return El total de bollos vendidos.
     */
    public abstract int total();
}
