package modelo.bollo;

/**
 *
 * @author Nicolay, David, Joshep, Sebastian.
 */
public class BolloLimpio extends Bollo {

    public BolloLimpio() {}
    
    public BolloLimpio(int bolloTraido, int bolloQuedado) {
        super(bolloTraido, bolloQuedado);
    }

    @Override
    public int costoBollo() {
        return 2000;
    }

    @Override
    public int total() {
        return (super.getBolloTraido() - super.getBolloQuedado()) * this.costoBollo();
    }
    
}
